package com.em.mediaplayer.app.server

import android.app.Application
import android.content.Context.WIFI_SERVICE
import android.net.Uri
import android.net.wifi.WifiManager
import android.provider.OpenableColumns
import android.util.Log
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.Response.Status
import javax.inject.Inject


class FileServer @Inject constructor(private val context: Application) : NanoHTTPD(8089) {

    private val wm = context.getSystemService(WIFI_SERVICE) as WifiManager?
    private val ipAddress: Int = wm!!.connectionInfo!!.ipAddress
    private val contentResolver = context.contentResolver
    val ip: String = String.format("%d.%d.%d.%d:8089", ipAddress and 0xff, ipAddress shr 8 and 0xff, ipAddress shr 16 and 0xff, ipAddress shr 24 and 0xff)

    private var uri: Uri? = null
    override fun serve(session: IHTTPSession): Response {
        val uri = this.uri
        return if (uri != null) {
            try {
                val mimeType = contentResolver.getType(uri)
                val lengthIndex = contentResolver.query(uri, null, null, null, null)?.run {
                    moveToFirst()
                    getLong(getColumnIndex(OpenableColumns.SIZE)) - 1
                } ?: 0
                val stream = context.contentResolver.openInputStream(uri)
                val range = parseRange(session.headers, lengthIndex)
                Log.d(TAG, "Server Request $range")
                stream?.skip(range.first)

                Log.d(TAG, "Serving Fulls")
                newFixedLengthResponse(Status.OK, mimeType, stream, lengthIndex).apply {
                    addHeader("Accept-Ranges", "bytes")
                    addHeader("Content-Length", "" + (range.last - range.first + 1))
                    addHeader("Content-Range", "bytes " + range.first + "-" + range.last + "/" + lengthIndex)
                }
            } catch (e: Exception) {
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
