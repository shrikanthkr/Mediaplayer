package com.mediaplayer.ui.now.playing

import androidx.lifecycle.ViewModel
import com.mediaplayer.app.di.scopes.FragmentScope
import com.mediaplayer.player.PlayerController
import javax.inject.Inject

@FragmentScope
class NowPlayingViewModel @Inject constructor(private val playerController: PlayerController) : ViewModel() {

    val playerStateLiveData = playerController.playerState
    val currentSongLiveData = playerController.currentSong

    fun resume() {
        playerController.resume()
    }

    fun pause() {
        playerController.pause()
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}
