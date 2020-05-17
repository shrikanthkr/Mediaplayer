package com.em.player

interface PlayerListener {
    fun onStart()
    fun onPause()
    fun onProgress(progress: Long)
    fun onEnd()
    fun onError()
}