package com.mediaplayer.ui.now.playing

import androidx.lifecycle.ViewModel
import com.mediaplayer.app.di.scopes.FragmentScope
import com.mediaplayer.app.models.PlayerState
import com.mediaplayer.player.PlayerController
import javax.inject.Inject

@FragmentScope
class NowPlayingViewModel @Inject constructor(private val playerController: PlayerController) : ViewModel() {

    val playerStateLiveData = playerController.playerState
    val currentSongLiveData = playerController.currentSong


    fun seekTo(progress: Int) {
        playerController.seek(progress.toLong())
    }

    fun togglePlay() {
        if (playerStateLiveData.value is PlayerState.Playing) {
            playerController.pause()
        } else {
            playerController.resume()
        }
    }

}
