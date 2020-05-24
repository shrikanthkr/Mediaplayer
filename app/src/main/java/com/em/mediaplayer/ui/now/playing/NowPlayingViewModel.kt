package com.em.mediaplayer.ui.now.playing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.db.SongsRepository
import com.em.mediaplayer.app.di.scopes.FragmentScope
import com.em.mediaplayer.app.models.PlayerState
import com.em.mediaplayer.app.server.FileServer
import com.em.mediaplayer.player.CastAdapter
import com.em.mediaplayer.player.PlayerController
import com.em.repository.Song
import com.google.android.gms.cast.framework.CastSession
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
    }

    fun seekTo(progress: Int) {
        playerController.seek(progress.toLong())
    }

    fun togglePlay() {
        if (_playerState.value is PlayerState.Playing) {
            playerController.pause()
        } else {
            playerController.resume()
        }
    }

    fun next() {
        playerController.next()
    }

    fun previous() {
        playerController.previous()
    }

    fun switchToCastAdapter(server: FileServer, session: CastSession) {
        val adapter = CastAdapter(server, session)
        playerController.switchAdapter(adapter)
    }

    fun toDefaultAdapter() {
        playerController.switchToDefaultAdapter()
    }


}
