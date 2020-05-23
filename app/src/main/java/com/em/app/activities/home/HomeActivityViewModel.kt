package com.em.app.activities.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.em.app.models.PlayerState
import com.em.db.SongsRepository
import com.em.player.PlayerController
import com.em.repository.Song
import com.google.android.material.bottomsheet.BottomSheetBehavior

class HomeActivityViewModel(private val playerController: PlayerController, songsRepository: SongsRepository) : ViewModel() {
    private val _playingFragmentState = MutableLiveData<Int>()
    val playingFragmentState = _playingFragmentState
    val playerStateLiveData = playerController.playerState
    val currentSong = MediatorLiveData<Song>()

    private val _scrollTo = MutableLiveData<Int>()
    val scrollTo = _scrollTo

    init {
        currentSong.addSource(songsRepository.currentSong) {
            playerController.play(it)
            currentSong.value = it
        }
    }

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

    fun scrollTop(position: Int) {
        _scrollTo.value = position
    }
}

