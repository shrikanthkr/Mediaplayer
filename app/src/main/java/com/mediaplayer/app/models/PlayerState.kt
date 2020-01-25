package com.mediaplayer.app.models

import com.mediaplayer.repository.Song

sealed class PlayerState {
    object Errored : PlayerState()
    data class Paused(val song: Song, val progress: Long) : PlayerState()
    data class Playing(val song: Song, val progress: Long = 0) : PlayerState()
    data class Completed(val song: Song) : PlayerState()
}

