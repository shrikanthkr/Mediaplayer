package com.em.mediaplayer.app.di.modules

import androidx.lifecycle.ViewModel
import com.em.db.SongsRepository
import com.em.mediaplayer.app.ViewModelFactory
import com.em.mediaplayer.app.activities.home.HomeActivityViewModel
import com.em.mediaplayer.player.PlayerController
import com.em.mediaplayer.ui.albums.AlbumsViewModel
import com.em.mediaplayer.ui.artist.ArtistViewModel
import com.em.mediaplayer.ui.now.playing.NowPlayingViewModel
import com.em.mediaplayer.ui.search.SearchViewModel
import com.em.mediaplayer.ui.songs.SongsViewModel
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Provider
import kotlin.reflect.KClass

@ExperimentalCoroutinesApi
@FlowPreview
@Module
class ViewModelModule {
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    @Retention(AnnotationRetention.RUNTIME)
    @MapKey
    internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

    @Provides
    fun viewModelFactory(map: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelFactory {
        return ViewModelFactory(map)
    }

    @Provides
    @IntoMap
    @ViewModelKey(SongsViewModel::class)
    fun homeViewModel(songsRepository: SongsRepository, playerController: PlayerController): ViewModel {
        return SongsViewModel(songsRepository, playerController)
    }

    @Provides
    @IntoMap
    @ViewModelKey(HomeActivityViewModel::class)
    fun homeActivityViewModel(playerController: PlayerController, songsRepository: SongsRepository): ViewModel {
        return HomeActivityViewModel(playerController, songsRepository)
    }

    @Provides
    @IntoMap
    @ViewModelKey(AlbumsViewModel::class)
    fun albumsVideModel(songsRepository: SongsRepository, playerController: PlayerController): ViewModel {
        return AlbumsViewModel(songsRepository, playerController)
    }

    @Provides
    @IntoMap
    @ViewModelKey(ArtistViewModel::class)
    fun artistsViewModel(songsRepository: SongsRepository, playerController: PlayerController): ViewModel {
        return ArtistViewModel(songsRepository, playerController)
    }

    @Provides
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun searchViewModel(songsRepository: SongsRepository, playerController: PlayerController): ViewModel {
        return SearchViewModel(songsRepository, playerController)
    }

    @Provides
    @IntoMap
    @ViewModelKey(NowPlayingViewModel::class)
    fun nowPlayingViewModel(playerController: PlayerController, songsRepository: SongsRepository): ViewModel {
        return NowPlayingViewModel(playerController, songsRepository)
    }
}