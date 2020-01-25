package com.mediaplayer.player

abstract class PlayerAdapter {
    private val list = mutableListOf<PlayerListener>()

    abstract fun play(path: String)
    abstract fun pause()
    abstract fun resume()
    abstract fun seek(position: Long)

    fun addListener(listener: PlayerListener) {
        list.add(listener)
    }

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