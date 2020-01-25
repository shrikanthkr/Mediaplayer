package com.mediaplayer.player

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mediaplayer.app.models.PlayerState
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
                Log.d(TAG, "Start")
            }

            override fun onPause() {
                Log.d(TAG, "Pause")
                val previousState = requireNotNull(_playerState.value) as PlayerState.Playing
                playerState.value = PlayerState.Paused(previousState.song, previousState.progress)
            }

            override fun onProgress(progress: Long) {
                val previousState = requireNotNull(_playerState.value) as PlayerState.Playing
                playerState.value = PlayerState.Playing(previousState.song, progress)
            }

            override fun onEnd() {
                Log.d(TAG, "End")
            }

            override fun onError() {
                Log.d(TAG, "Pause")
                playerState.value = PlayerState.Errored
            }

        })
    }

    fun play(song: Song) {
        playerAdapter.play(song.data)
        _currentSong.value = song
        _playerState.value = PlayerState.Playing(song, 0)
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

    companion object {
        const val TAG = "PlayerController"
    }
}