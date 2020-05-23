package com.em.ui.now.playing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.app.di.scopes.FragmentScope
import com.em.app.models.PlayerState
import com.em.db.SongsRepository
import com.em.player.PlayerController
import kotlinx.coroutines.launch
import javax.inject.Inject

@FragmentScope
class NowPlayingViewModel @Inject constructor(private val playerController: PlayerController, private val songsRepository: SongsRepository) : ViewModel() {

    val playerStateLiveData = playerController.playerState
    val currentSongLiveData = songsRepository.currentSong


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

    fun next() {
        viewModelScope.launch {
            songsRepository.next()
        }
    }

    fun previous() {
        viewModelScope.launch {
            songsRepository.previous()
        }
    }


}
