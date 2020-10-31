package com.em.mediaplayer.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.db.SongsRepository
import com.em.mediaplayer.player.PlayerController
import com.em.repository.Song
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import javax.inject.Inject



class SearchViewModel @Inject constructor(private val repository: SongsRepository, private val playerController: PlayerController) : ViewModel() {

    private val _songsLiveData = MutableLiveData<List<Song>>()
    val songsLiveData = _songsLiveData

    fun search(query: String) {
        viewModelScope.launch {
            if (query.isNotEmpty()) {
                _songsLiveData.value = repository.search(query)
            } else {
                _songsLiveData.value = emptyList()
            }
        }
    }


    fun play(song: Song) {
        playerController.playNow(song)
    }

    fun addToQueue(son: Song) {
        playerController.queue(son)
    }
}