package com.mediaplayer.ui.artist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediaplayer.app.di.scopes.FragmentScope
import com.mediaplayer.db.SongsRepository
import com.mediaplayer.player.PlayerController
import com.mediaplayer.repository.Artist
import kotlinx.coroutines.launch
import javax.inject.Inject

@FragmentScope
class ArtistViewModel @Inject constructor(repository: SongsRepository, private val playerController: PlayerController) : ViewModel() {
    private val _artists = MutableLiveData<List<Artist>>()
    val artists = _artists

    init {
        viewModelScope.launch {
            _artists.value = repository.artists()
        }

    }
}