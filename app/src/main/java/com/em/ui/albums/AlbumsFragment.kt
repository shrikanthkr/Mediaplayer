package com.em.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.em.app.ViewModelFactory
import com.em.app.databinding.FragmentAlbumsBinding
import com.em.app.di.components.FragmentComponent
import com.em.ui.BaseFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the [AlbumsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumsFragment : BaseFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewBinding: FragmentAlbumsBinding
    private lateinit var viewmodel: AlbumsViewModel

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(this, viewModelFactory).get(AlbumsViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewBinding = FragmentAlbumsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.albums.layoutManager = GridLayoutManager(requireContext(), 2)
        viewmodel.albums.observe(viewLifecycleOwner, Observer {
            viewBinding.albums.adapter = AlbumsRecyclerAdapter(it) {

            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = AlbumsFragment()
    }


}
