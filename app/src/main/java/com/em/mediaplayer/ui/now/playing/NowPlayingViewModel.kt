package com.em.mediaplayer.ui.now.playing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.db.SongsRepository
import com.em.mediaplayer.app.di.scopes.FragmentScope
import com.em.mediaplayer.app.models.PlayerState
import com.em.mediaplayer.player.PlayerController
import com.em.repository.Song
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@FragmentScope
@ExperimentalCoroutinesApi
class NowPlayingViewModel @Inject constructor(private val playerController: PlayerController, private val songsRepository: SongsRepository) : ViewModel() {

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState = _playerState

    private val _currentSong = MutableLiveData<Song>()
    val currentSong = _currentSong

    private val _currentUIState = MutableLiveData<UIState>()
    val currentUIState = _currentUIState


    init {
        viewModelScope.launch {
            playerController.playerState.collect {
                _playerState.value = it
            }
        }

        viewModelScope.launch {
            playerController.currentSongChannel.collect {
                _currentSong.value = it
            }
        }

        viewModelScope.launch {
            songsRepository.queue.collect {
                _currentUIState.value = UIState(songs = it, showList = _currentUIState.value?.showList
                        ?: false)
            }
        }

    }

    fun seekTo(progress: Int) {
        playerController.seek(progress.toLong())
    }

    fun togglePlay() {
        when (_playerState.value) {
            is PlayerState.Playing -> playerController.pause()
            is PlayerState.Paused -> playerController.resume()
            else -> {
            }
        }
    }

    fun next() {
        playerController.next()
    }

    fun previous() {
        playerController.previous()
    }

    fun listToggle() {
        val currentState = requireNotNull(_currentUIState.value)
        _currentUIState.value = currentState.copy(showList = !currentState.showList)
    }

    fun play(song: Song) {
        playerController.playNow(song)
    }

    data class UIState(val showList: Boolean = false, val songs: List<Song>)
}
