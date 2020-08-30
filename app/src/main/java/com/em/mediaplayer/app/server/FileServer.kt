package com.em.mediaplayer.app.server

import android.app.Application
import android.content.Context.WIFI_SERVICE
import android.net.Uri
import android.net.wifi.WifiManager
import android.util.Log
import com.em.mediaplayer.app.utils.IOUtils
import com.em.mediaplayer.app.utils.toFile
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.Response.Status
import java.io.File
import javax.inject.Inject


class FileServer @Inject constructor(context: Application, private val ioUtils: IOUtils) : NanoHTTPD(8089) {

    private val wm = context.getSystemService(WIFI_SERVICE) as WifiManager?
    private val ipAddress: Int = wm!!.connectionInfo!!.ipAddress
    private val contentResolver = context.contentResolver
    val ip: String = String.format("%d.%d.%d.%d:8089", ipAddress and 0xff, ipAddress shr 8 and 0xff, ipAddress shr 16 and 0xff, ipAddress shr 24 and 0xff)
    private var uri: Uri? = null
    override fun serve(session: IHTTPSession): Response {
        Log.d(TAG, "${session.headers}")
        val uri = this.uri
        return if (uri != null) {
            try {
                val filePath = ioUtils.cacheDirPath("sample.mp3")
                contentResolver.openInputStream(uri)?.toFile(filePath)
                val file = File(filePath)
                val mimeType = contentResolver.getType(uri)
                val lengthIndex = file.length()
                val stream = file.inputStream()
                val range = parseRange(session.headers, lengthIndex)
                Log.d(TAG, "Computed Request $range")
                stream.skip(range.first)

                Log.d(TAG, "Serving Fulls")
                newFixedLengthResponse(Status.OK, mimeType, stream, lengthIndex).apply {
                    addHeader("Accept-Ranges", "bytes")
                    addHeader("Content-Length", "" + (range.last - range.first + 1))
                    addHeader("Content-Range", "bytes " + range.first + "-" + range.last + "/" + lengthIndex)
                }
            } catch (e: Throwable) {
                Log.e(TAG, "ERROR WHILE SERVING", e)
                newFixedLengthResponse(Status.FORBIDDEN, "", "")
            } finally {
            }
        } else {
            newFixedLengthResponse(Status.NOT_FOUND, "", "")
        }
    }

    private fun parseRange(header: Map<String, String>, lengthIndex: Long): LongProgression {
        val range = header[RANGE]
        return if (range != null) {
            val start: Long
            val end: Long
            if (range.startsWith(BYTES_PREFIX)) {
                val ranges = range.split("=")[1].split("-")
                Log.d(TAG, "Server Request: $ranges")
                if (ranges.isNotEmpty()) {
                    start = ranges[0].toLong()
                    end = try {
                        ranges[1].toLong()
                    } catch (e: NumberFormatException) {
                        lengthIndex
                    }
                } else {
                    start = 0L
                    end = lengthIndex
                }
            } else {
                start = 0L
                end = lengthIndex
            }
            start..end
        } else {
            0 downTo lengthIndex
        }
    }


    fun serve(uri: Uri) {
        this.uri = uri
    }

    companion object {
        private const val TAG = "FileServer"
        private const val BYTES_PREFIX = "bytes="
        private const val LOCAL_FILE = "sample.mp3"
        private const val RANGE = "range"
    }
}
