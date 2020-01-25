package com.mediaplayer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mediaplayer.app.activities.BaseActivity
import com.mediaplayer.app.di.components.FragmentComponent

abstract class BaseFragment : Fragment() {
    lateinit var fragmentComponent: FragmentComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentComponent = (requireActivity() as BaseActivity).appComponent.fragmentComponent().create()
        inject(fragmentComponent)
        super.onCreate(savedInstanceState)

    }


    abstract fun inject(fragmentComponent: FragmentComponent)

}