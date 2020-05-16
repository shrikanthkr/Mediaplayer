package com.mediaplayer.player

import android.net.Uri

abstract class PlayerAdapter {
    private val list = mutableListOf<PlayerListener>()

    abstract fun play(path: Uri)
    abstract fun pause()
    abstract fun resume()
    abstract fun seek(position: Long)
    abstract fun clear()

    fun addListener(listener: PlayerListener) {
        list.add(listener)
    }

    @Suppress("unused")
    fun removeListener(listener: PlayerListener) {
        list.remove(listener)
    }

    fun dispatchStart() {
        list.forEach {
            it.onStart()
        }
    }

    fun dispatchPause() {
        list.forEach {
            it.onPause()
        }
    }

    fun dispatchEnd() {
        list.forEach {
            it.onEnd()
        }
    }

    fun dispatchError() {
        list.forEach {
            it.onError()
        }
    }

    fun dispatchProgress(progress: Long) {
        list.forEach {
            it.onProgress(progress)
        }
    }


}