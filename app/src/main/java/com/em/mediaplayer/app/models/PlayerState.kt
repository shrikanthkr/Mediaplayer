package com.em.mediaplayer.app.models

import com.em.repository.Song

sealed class PlayerState {
    object Erred : PlayerState()
    object Idle : PlayerState()
    object Loading : PlayerState()
    data class Started(val song: Song) : PlayerState()
    data class Paused(val song: Song, val progress: Long) : PlayerState()
    data class Playing(val song: Song, val progress: Long = 0) : PlayerState()
    data class Completed(val song: Song) : PlayerState()
}

