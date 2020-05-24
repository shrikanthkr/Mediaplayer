package com.em.mediaplayer.app.di.modules

import com.em.mediaplayer.app.di.components.FragmentComponent
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@Module(subcomponents = [FragmentComponent::class])
class SubComponentsModule