package com.em.app.activities.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.app.models.PlayerState
import com.em.app.models.PlayerState.Completed
import com.em.db.SongsRepository
import com.em.player.PlayerController
import com.em.repository.Song
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class HomeActivityViewModel(private val playerController: PlayerController, private val songsRepository: SongsRepository) : ViewModel() {
    private val _playingFragmentState = MutableLiveData<Int>()
    val playingFragmentState = _playingFragmentState

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    private val _currentSong = MutableLiveData<Song>()
    val currentSong: LiveData<Song> = _currentSong

    private val _scrollTo = MutableLiveData<Int>()
    val scrollTo = _scrollTo

    init {
        viewModelScope.launch {
            playerController.channel.collect {
                _playerState.value = it
                when (it) {
                    is Completed -> next()
                    else -> Unit
                }
            }
        }

        viewModelScope.launch {
            songsRepository.currentSongChannel.collect {
                playerController.play(it)
                _currentSong.value = it
            }
        }
    }

    fun updateState(@BottomSheetBehavior.State state: Int) {
        _playingFragmentState.value = state
    }

    fun togglePlay() {
        if (playerState.value is PlayerState.Playing) {
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

