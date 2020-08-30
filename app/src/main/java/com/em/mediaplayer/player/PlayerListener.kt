package com.em.mediaplayer.player

interface PlayerListener {
    fun onStart()
    fun onLoading()
    fun onPause()
    fun onProgress(progress: Long)
    fun onEnd()
    fun onError()
}