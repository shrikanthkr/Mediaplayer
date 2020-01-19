package com.mediaplayer.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediaplayer.app.di.scopes.FragmentScope
import com.mediaplayer.db.SongsRepository
import com.mediaplayer.player.PlayerController
import com.mediaplayer.player.PlayerListener
import com.mediaplayer.repository.Song
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

            playerController.play(songs.first().data)
            viewModelScope.launch {
                delay(5000)
                playerController.seek(290000)
            }
        } catch (e: Exception) {
            print(e)
        }

    }

    override fun onCleared() {
        super.onCleared()
        playerController.removeListener(playerListener)
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}
