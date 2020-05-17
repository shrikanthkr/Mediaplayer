package com.em.ui.now.playing

import androidx.lifecycle.ViewModel
import com.em.app.di.scopes.FragmentScope
import com.em.app.models.PlayerState
import com.em.player.PlayerController
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
