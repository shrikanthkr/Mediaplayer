package com.em.app.activities.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.app.models.PlayerState
import com.em.db.SongsRepository
import com.em.player.PlayerController
import com.em.repository.Song
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch

class HomeActivityViewModel(private val playerController: PlayerController, private val songsRepository: SongsRepository) : ViewModel() {
    private val _playingFragmentState = MutableLiveData<Int>()
    val playingFragmentState = _playingFragmentState
    val currentSong = MediatorLiveData<Song>()
    val playerStateLiveData = MediatorLiveData<PlayerState>()

    private val _scrollTo = MutableLiveData<Int>()
    val scrollTo = _scrollTo

    init {
        currentSong.addSource(songsRepository.currentSong) {
            playerController.play(it)
            currentSong.value = it
        }

        playerStateLiveData.addSource(playerController.playerState) {
            playerStateLiveData.value = it
            when (it) {
                is PlayerState.Completed -> next()
                else -> Unit
            }
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

    fun next() = viewModelScope.launch {
        songsRepository.next()
    }
}

