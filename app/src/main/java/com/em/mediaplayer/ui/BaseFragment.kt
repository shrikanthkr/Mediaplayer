package com.em.mediaplayer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.em.mediaplayer.app.activities.BaseActivity
import com.em.mediaplayer.app.di.components.FragmentComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

abstract class BaseFragment : Fragment() {
    lateinit var fragmentComponent: FragmentComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentComponent = (requireActivity() as BaseActivity).appComponent.fragmentComponent().create()
        inject(fragmentComponent)
        super.onCreate(savedInstanceState)

    }


    abstract fun inject(fragmentComponent: FragmentComponent)

}