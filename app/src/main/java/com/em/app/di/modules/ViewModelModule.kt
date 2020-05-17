package com.em.app.di.modules

import androidx.lifecycle.ViewModel
import com.em.app.ViewModelFactory
import com.em.app.activities.home.HomeActivityViewModel
import com.em.db.SongsRepository
import com.em.player.PlayerController
import com.em.ui.albums.AlbumsViewModel
import com.em.ui.artist.ArtistViewModel
import com.em.ui.now.playing.NowPlayingViewModel
import com.em.ui.songs.SongsViewModel
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider
import kotlin.reflect.KClass

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
    fun homeActivityViewModel(playerController: PlayerController): ViewModel {
        return HomeActivityViewModel(playerController)
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
    @ViewModelKey(NowPlayingViewModel::class)
    fun nowPlayingViewModel(playerController: PlayerController): ViewModel {
        return NowPlayingViewModel(playerController)
    }
}