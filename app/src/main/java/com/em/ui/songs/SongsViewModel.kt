package com.em.ui.songs

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.app.di.scopes.FragmentScope
import com.em.db.SongsRepository
import com.em.repository.Song
import kotlinx.coroutines.launch
import javax.inject.Inject

@FragmentScope
class SongsViewModel @Inject constructor(private val repository: SongsRepository) : ViewModel() {
    private val _songsLiveData = MutableLiveData<List<Song>>()
    val songsLiveData = _songsLiveData

    init {
        try {
            viewModelScope.launch {
                val songs = repository.all()
                _songsLiveData.value = songs
                Log.d(TAG, "NEw HOme View model")
            }
        } catch (e: Exception) {
            print(e)
        }

    }


    fun play(song: Song) = viewModelScope.launch {
        repository.playNow(song)
    }

    fun addToQueue(son: Song) = viewModelScope.launch {
        repository.queue(son)
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}
