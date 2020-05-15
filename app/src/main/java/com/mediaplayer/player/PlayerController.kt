package com.mediaplayer.player

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mediaplayer.app.models.PlayerState
import com.mediaplayer.app.models.PlayerState.*
import com.mediaplayer.repository.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerController @Inject constructor(private val playerAdapter: PlayerAdapter) {
    private val _playerState = MutableLiveData<PlayerState>()
    val playerState = _playerState

    private val _currentSong = MutableLiveData<Song>()
    val currentSong = _currentSong

    init {
        playerAdapter.addListener(object : PlayerListener {
            override fun onStart() {
                playerState.value = Started(requireNotNull(_currentSong.value))
                Log.d(TAG, "Start")
            }

            override fun onPause() {
                Log.d(TAG, "Pause")
                val previousState = requireNotNull(_playerState.value) as Playing
                playerState.value = Paused(previousState.song, previousState.progress)
            }

            override fun onProgress(progress: Long) {
                when (_playerState.value) {
                    is Playing -> {
                        val previousState = requireNotNull(_playerState.value) as Playing
                        playerState.value = Playing(previousState.song, progress)
                    }
                    is Paused -> {
                        val previousState = requireNotNull(_playerState.value) as Paused
                        playerState.value = Playing(previousState.song, progress)
                    }
                    else -> {
                        val previousState = requireNotNull(_playerState.value) as Started
                        playerState.value = Playing(previousState.song, progress)
                    }
                }
            }

            override fun onEnd() {
                if (_playerState.value is Playing) {
                    val previousState = requireNotNull(_playerState.value) as Playing
                    playerState.value = Completed(previousState.song)
                } else {
                    val previousState = requireNotNull(_playerState.value) as Paused
                    playerState.value = Completed(previousState.song)
                }
            }

            override fun onError() {
                Log.d(TAG, "Erred")
                playerState.value = Erred
            }

        })
        _playerState.value = Idle
    }

    fun play(song: Song) {
        playerAdapter.play(song.uri)
        _currentSong.value = song
        _playerState.value = Playing(song, 0)
    }

    fun pause() {
        playerAdapter.pause()
    }

    fun resume() {
        playerAdapter.resume()
    }

    fun seek(position: Long) {
        playerAdapter.seek(position)
    }

    fun clear() {
        playerAdapter.clear()
    }

    companion object {
        const val TAG = "PlayerController"
    }
}