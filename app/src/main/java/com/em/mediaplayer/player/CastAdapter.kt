package com.em.mediaplayer.player

import android.util.Log
import com.em.repository.Song
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastSession

class CastAdapter(session: CastSession) : PlayerAdapter() {

    private val remoteMediaClient = session.remoteMediaClient

    companion object {
        const val TAG = "CastAdapter"
    }

    override fun play(song: Song) {
        val metaData = MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK)
        val info = MediaInfo.Builder(song.uri.toString())
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("audio/mpeg")
                .setMetadata(metaData)
                .setStreamDuration(song.duration)
                .build()
        remoteMediaClient.load(MediaLoadRequestData.Builder()
                .setMediaInfo(info)
                .build())
                .setResultCallback {
                    Log.d(TAG, it.mediaError.reason)
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

    }

}