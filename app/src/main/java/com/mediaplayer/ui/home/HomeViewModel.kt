package com.mediaplayer.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mediaplayer.app.di.scopes.FragmentScope
import com.mediaplayer.db.SongsRepository
import com.mediaplayer.player.PlayerController
import com.mediaplayer.player.PlayerListener
import com.mediaplayer.repository.Song
import javax.inject.Inject

@FragmentScope
class HomeViewModel @Inject constructor(repository: SongsRepository, private val playerController: PlayerController) : ViewModel() {
    private val _songsLiveData = MutableLiveData<List<Song>>()
    val songsLiveData = _songsLiveData
    private val playerListener = object : PlayerListener {
        override fun onStart() {
            Log.d(TAG, " Start")
        }

        override fun onPause() {
            Log.d(TAG, " Pause")
        }

        override fun onProgress(progress: Long) {
            Log.d(TAG, " Progress $progress")
        }

        override fun onEnd() {
            Log.d(TAG, " End")
        }

        override fun onError() {
            Log.d(TAG, " Error")
        }

    }

    init {
        try {
            val songs = repository.getSongs()
            _songsLiveData.value = songs
            playerController.addListener(playerListener)
            Log.d(TAG, "NEw HOme View model")
        } catch (e: Exception) {
            print(e)
        }

    }

    override fun onCleared() {
        super.onCleared()
        playerController.removeListener(playerListener)
    }

    fun play(song: Song) {
        playerController.play(song.data)
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}
