package com.em.ui.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.em.app.ViewModelFactory
import com.em.app.activities.home.HomeActivityViewModel
import com.em.app.databinding.FragmentSongsBinding
import com.em.app.di.components.FragmentComponent
import com.em.ui.BaseFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject


@FlowPreview
@ExperimentalCoroutinesApi
class SongsFragment : BaseFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: SongsViewModel
    private lateinit var homeActivityViewModel: HomeActivityViewModel
    private lateinit var viewBinding: FragmentSongsBinding
    private var myPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SongsViewModel::class.java)
        homeActivityViewModel = ViewModelProvider(this.requireActivity(), viewModelFactory).get(HomeActivityViewModel::class.java)
        myPosition = requireArguments().getInt(MY_POSITION)
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentSongsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.homeRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        viewModel.songsLiveData.observe(viewLifecycleOwner, Observer {
            viewBinding.homeRecyclerView.adapter = SongsRecyclerAdapter(it, { song ->
                viewModel.play(song)
            }, { song ->
                viewModel.addToQueue(song)
            })
        })

        homeActivityViewModel.scrollTo.observe(viewLifecycleOwner, Observer {
            if (myPosition == it) {
                viewBinding.homeRecyclerView.scrollToPosition(0)
            }
        })

    }

    companion object {
        private const val MY_POSITION = "POSITION"
        fun newInstance(position: Int): BaseFragment {
            val bundle = Bundle()
            bundle.putInt(MY_POSITION, position)
            val songs = SongsFragment()
            songs.arguments = bundle
            return songs
        }
    }
}