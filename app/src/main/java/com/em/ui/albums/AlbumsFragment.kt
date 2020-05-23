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
import com.em.app.activities.home.HomeActivityViewModel
import com.em.app.databinding.FragmentAlbumsBinding
import com.em.app.di.components.FragmentComponent
import com.em.ui.BaseFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the [AlbumsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@ExperimentalStdlibApi
@FlowPreview
@ExperimentalCoroutinesApi
class AlbumsFragment : BaseFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewBinding: FragmentAlbumsBinding
    private lateinit var viewmodel: AlbumsViewModel
    private lateinit var homeActivityViewModel: HomeActivityViewModel
    private var myPosition: Int = -1

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(this, viewModelFactory).get(AlbumsViewModel::class.java)
        homeActivityViewModel = ViewModelProvider(this.requireActivity(), viewModelFactory).get(HomeActivityViewModel::class.java)
        myPosition = requireArguments().getInt(MY_POSITION)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewBinding = FragmentAlbumsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.albums.layoutManager = GridLayoutManager(requireContext(), 2)
        viewmodel.albums.observe(viewLifecycleOwner, Observer {
            viewBinding.albums.adapter = AlbumsRecyclerAdapter(it) { album ->
                viewmodel.playAlbum(album)
            }
        })

        homeActivityViewModel.scrollTo.observe(viewLifecycleOwner, Observer {
            if (myPosition == it) {
                viewBinding.albums.scrollToPosition(0)
            }
        })
    }

    companion object {
        private const val MY_POSITION = "POSITION"

        @JvmStatic
        fun newInstance(position: Int): BaseFragment {
            val bundle = Bundle()
            bundle.putInt(MY_POSITION, position)
            val albums = AlbumsFragment()
            albums.arguments = bundle
            return albums
        }
    }


}
