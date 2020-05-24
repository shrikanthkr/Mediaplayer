package com.em.mediaplayer.player

import com.em.mediaplayer.app.server.FileServer
import com.em.repository.Song
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastSession

class CastAdapter(private val server: FileServer, session: CastSession) : PlayerAdapter() {

    private val remoteMediaClient = session.remoteMediaClient

    companion object {
        const val TAG = "CastAdapter"
    }

    init {
        server.start()
    }

    override fun play(song: Song) {
        if (!server.isAlive) {
            server.start()
        }
        server.serve(song.uri)

        val metaData = MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK)
        val info = MediaInfo.Builder("http://${server.ip}/sample.mp3")
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("audio/mp3")
                .setMetadata(metaData)
                .setStreamDuration(song.duration)
                .build()
        remoteMediaClient.load(MediaLoadRequestData.Builder()
                .setMediaInfo(info)
                .build())
                .setResultCallback {

                }
        remoteMediaClient.play()
    }

    override fun pause() {
        remoteMediaClient.pause()
    }

    override fun resume() {
        remoteMediaClient.play()
    }

    override fun seek(position: Long) {

    }

    override fun clear() {
        server.stop()
    }

}