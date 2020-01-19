package com.mediaplayer.player

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerController @Inject constructor(private val playerAdapter: PlayerAdapter) {
    fun play(path: String) {
        playerAdapter.play(path)
    }

    fun pause() {
        playerAdapter.pause()
    }

    fun seek(position: Long) {
        playerAdapter.seek(position)
    }

    fun addListener(playerListener: PlayerListener) {
        playerAdapter.addListener(playerListener)
    }

    fun removeListener(playerListener: PlayerListener) {
        playerAdapter.removeListener(playerListener)
    }
}