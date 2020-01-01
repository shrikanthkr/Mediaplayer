package com.mediaplayer.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mediaplayer.app.di.scopes.FragmentScope
import com.mediaplayer.db.SongsRepository
import com.mediaplayer.repository.Song
import javax.inject.Inject

@FragmentScope
class HomeViewModel @Inject constructor(repository: SongsRepository) : ViewModel() {
    private val _songsLiveData = MutableLiveData<List<Song>>()
    val songsLiveData = _songsLiveData

    init {
        try {
            _songsLiveData.value = repository.getSongs()
        } catch (e: Exception) {
            print(e)
        }

    }
}
