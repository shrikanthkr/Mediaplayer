package com.em.ui.albums

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.app.di.scopes.FragmentScope
import com.em.db.SongsRepository
import com.em.player.PlayerController
import com.em.repository.Album
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