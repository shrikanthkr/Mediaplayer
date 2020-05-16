package com.mediaplayer.ui.albums

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediaplayer.app.di.scopes.FragmentScope
import com.mediaplayer.db.SongsRepository
import com.mediaplayer.player.PlayerController
import com.mediaplayer.repository.Album
import kotlinx.coroutines.launch
import javax.inject.Inject

@FragmentScope
class AlbumsViewModel @Inject constructor(repository: SongsRepository, private val playerController: PlayerController) : ViewModel() {
    private val _albums = MutableLiveData<List<Album>>()
    val albums = _albums

    init {
        viewModelScope.launch {
            _albums.value = repository.albums()
        }

    }
}