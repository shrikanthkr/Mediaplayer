package com.mediaplayer.player

import android.app.Application
import android.util.Log
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.MediaPlayer.Event.*
import javax.inject.Inject

class VLCAdapter @Inject constructor(application: Application) : PlayerAdapter() {
    private val libvlc = LibVLC(application)
    private val mediaPlayer = MediaPlayer(libvlc)

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

    override fun play(path: String) {
        mediaPlayer.play(path)
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


    companion object {
        const val TAG = "VLCAdapter"
    }
}
