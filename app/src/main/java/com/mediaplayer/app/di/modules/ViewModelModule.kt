package com.mediaplayer.app.di.modules

import androidx.lifecycle.ViewModel
import com.mediaplayer.app.ViewModelFactory
import com.mediaplayer.app.activities.home.HomeActivityViewModel
import com.mediaplayer.db.SongsRepository
import com.mediaplayer.player.PlayerController
import com.mediaplayer.ui.home.HomeViewModel
import com.mediaplayer.ui.now.playing.NowPlayingViewModel
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
    @ViewModelKey(HomeViewModel::class)
    fun homeViewModel(songsRepository: SongsRepository, playerController: PlayerController): ViewModel {
        return HomeViewModel(songsRepository, playerController)
    }

    @Provides
    @IntoMap
    @ViewModelKey(HomeActivityViewModel::class)
    fun homeActivityViewModel(): ViewModel {
        return HomeActivityViewModel()
    }

    @Provides
    @IntoMap
    @ViewModelKey(NowPlayingViewModel::class)
    fun nowPlayingViewModel(playerController: PlayerController): ViewModel {
        return NowPlayingViewModel(playerController)
    }
}