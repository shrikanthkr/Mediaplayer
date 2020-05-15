package com.mediaplayer.app.models

import com.mediaplayer.repository.Song

sealed class PlayerState {
    object Erred : PlayerState()
    object Idle : PlayerState()
    data class Started(val song: Song) : PlayerState()
    data class Paused(val song: Song, val progress: Long) : PlayerState()
    data class Playing(val song: Song, val progress: Long = 0) : PlayerState()
    data class Completed(val song: Song) : PlayerState()
}

