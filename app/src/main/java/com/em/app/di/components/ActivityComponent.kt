package com.em.app.di.components

import com.em.app.activities.home.HomeActivity
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
@Subcomponent
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    fun inject(activity: HomeActivity)
}