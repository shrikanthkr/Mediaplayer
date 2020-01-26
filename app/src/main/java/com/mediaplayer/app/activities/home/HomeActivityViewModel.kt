package com.mediaplayer.app.activities.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mediaplayer.app.models.PlayerState
import com.mediaplayer.player.PlayerController

class HomeActivityViewModel(private val playerController: PlayerController) : ViewModel() {
    private val _playingFragmentState = MutableLiveData<Int>()
    val playingFragmentState = _playingFragmentState
    val playerStateLiveData = playerController.playerState
    val currentSong = playerController.currentSong

    fun updateState(@BottomSheetBehavior.State state: Int) {
        _playingFragmentState.value = state
    }

    fun togglePlay() {
        if (playerStateLiveData.value is PlayerState.Playing) {
            playerController.pause()
        } else {
            playerController.resume()
        }
    }
}

