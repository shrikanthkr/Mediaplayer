package com.em.mediaplayer.ui.albums

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.db.SongsRepository
import com.em.mediaplayer.app.di.scopes.FragmentScope
import com.em.mediaplayer.player.PlayerController
import com.em.repository.Album
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@FragmentScope
class AlbumsViewModel @Inject constructor(private val repository: SongsRepository, private val playerController: PlayerController) : ViewModel() {


    private val _albums = MutableLiveData<List<Album>>()
    val albums = _albums

    init {
        viewModelScope.launch {
            _albums.value = repository.albums()
        }
    }

    @ExperimentalStdlibApi
    fun playAlbum(album: Album) {
        playerController.clear()
        viewModelScope.launch {
            val songs = repository.songs(album)
            playerController.playNow(songs.removeFirst())
            playerController.queueAll(songs)
        }
    }
}