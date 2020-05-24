package com.em.mediaplayer.app.server

import android.app.Application
import android.content.Context.WIFI_SERVICE
import android.net.Uri
import android.net.wifi.WifiManager
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.Response.Status
import java.io.InputStream
import javax.inject.Inject


class FileServer @Inject constructor(private val context: Application) : NanoHTTPD(8089) {

    private val wm = context.getSystemService(WIFI_SERVICE) as WifiManager?
    private val ipAddress: Int = wm!!.connectionInfo!!.ipAddress
    val ip: String = String.format("%d.%d.%d.%d:8089", ipAddress and 0xff, ipAddress shr 8 and 0xff, ipAddress shr 16 and 0xff, ipAddress shr 24 and 0xff)

    private var uri: Uri? = null
    override fun serve(session: IHTTPSession?): Response {

        val uri = this.uri
        return if (uri != null) {
            try {
                val fis: InputStream? = context.contentResolver.openInputStream(uri)
                newChunkedResponse(Status.OK, "audio/mpeg", fis)
            } catch (e: Exception) {
                newFixedLengthResponse(Status.BAD_REQUEST, "", "")
            } finally {
            }
        } else {
            newFixedLengthResponse(Status.NOT_FOUND, "", "")
        }
    }


    fun serve(uri: Uri) {
        this.uri = uri
    }
}