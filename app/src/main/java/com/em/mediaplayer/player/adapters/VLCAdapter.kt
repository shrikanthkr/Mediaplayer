package com.em.mediaplayer.player.adapters

import android.app.Application
import android.net.Uri
import android.util.Log
import com.em.mediaplayer.app.utils.toFileDescriptor
import com.em.repository.Song
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.MediaPlayer.Event.*
import java.io.FileDescriptor
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class VLCAdapter @Inject constructor(private val application: Application) : PlayerAdapter() {
    private val libvlc by lazy { LibVLC(application) }
    private val mediaPlayer by lazy { MediaPlayer(libvlc) }

    init {
        mediaPlayer.setEventListener { event ->
            when (event.type) {
                Playing -> {
                    dispatchStart()
                }
                Buffering -> {
                    Log.d(TAG, " Bufferring")
                }
                Opening -> {
                    Log.d(TAG, " Opening")
                }
                Paused -> {
                    dispatchPause()
                }
                Stopped -> {
                    Log.d(TAG, " Stopped")
                }
                EndReached -> {
                    Log.d(TAG, " End")
                    dispatchEnd()
                }
                EncounteredError -> {
                    Log.d(TAG, " Error")
                    dispatchError()
                }
                TimeChanged -> {
                    dispatchProgress(event.timeChanged)
                }
                PositionChanged -> {
                    Log.d(TAG, " Position Changed")
                }
                SeekableChanged -> {
                    Log.d(TAG, " Seekable Changed")
                }
                PausableChanged -> {
                    Log.d(TAG, " Pausable Changed")
                }
                LengthChanged -> {
                    Log.d(TAG, " Length Changed")
                }
            }
        }
    }

    override fun play(song: Song) {
        val media = Media(libvlc, application.toFileDescriptor(song.uri))
        mediaPlayer.play(media)
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun resume() {
        mediaPlayer.play()
    }

    override fun seek(position: Long) {
        mediaPlayer.time = position
    }

    override fun clear() {
        mediaPlayer.pause()
    }

    companion object {
        const val TAG = "VLCAdapter"
    }
}
