package com.em.app.activities.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.em.app.models.PlayerState
import com.em.player.PlayerController
import com.google.android.material.bottomsheet.BottomSheetBehavior

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

