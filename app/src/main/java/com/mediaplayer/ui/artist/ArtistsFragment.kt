package com.mediaplayer.ui.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.mediaplayer.app.ViewModelFactory
import com.mediaplayer.app.databinding.FragmentArtistsBinding
import com.mediaplayer.app.di.components.FragmentComponent
import com.mediaplayer.ui.BaseFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the [ArtistsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArtistsFragment : BaseFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewBinding: FragmentArtistsBinding
    private lateinit var viewmodel: ArtistViewModel

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(this, viewModelFactory).get(ArtistViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewBinding = FragmentArtistsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.artists.layoutManager = GridLayoutManager(requireContext(), 2)
        viewmodel.artists.observe(viewLifecycleOwner, Observer {
            viewBinding.artists.adapter = ArtistsRecyclerAdapter(it) {

            }
        })
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment ArtistsFragment.
         */
        @JvmStatic
        fun newInstance() = ArtistsFragment()
    }
}
