package com.em.mediaplayer.ui.now.playing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.SeekBar.VISIBLE
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.em.mediaplayer.app.R
import com.em.mediaplayer.app.ViewModelFactory
import com.em.mediaplayer.app.activities.home.HomeActivityViewModel
import com.em.mediaplayer.app.databinding.FragmentNowplayingBinding
import com.em.mediaplayer.app.di.components.FragmentComponent
import com.em.mediaplayer.app.models.PlayerState.*
import com.em.mediaplayer.app.server.FileServer
import com.em.mediaplayer.app.utils.loadAlbum
import com.em.mediaplayer.ui.BaseFragment
import com.em.repository.formattedDuration
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
@FlowPreview
class NowPlayingFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var fileServer: FileServer

    private lateinit var viewBinding: FragmentNowplayingBinding
    private lateinit var homeActivityViewModel: HomeActivityViewModel
    private lateinit var viewModel: NowPlayingViewModel
    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivityViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(HomeActivityViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NowPlayingViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentNowplayingBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.currentList.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.currentList.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        viewBinding.downArrow.setOnClickListener {
            homeActivityViewModel.updateState(BottomSheetBehavior.STATE_HIDDEN)
        }
        viewModel.currentSong.observe(viewLifecycleOwner, {
            if (it != null) {
                viewBinding.album.text = it.album
                viewBinding.songTitle.text = it.title
                viewBinding.artistTitle.text = it.artist
                viewBinding.albumImage.loadAlbum(it.uri)
                viewBinding.playerSeekbar.max = it.duration.toInt()
            }
        })
        viewModel.playerState.observe(viewLifecycleOwner, {
            when (it) {
                is Playing -> {
                    viewBinding.playerSeekbar.progress = it.progress.toInt()
                    viewBinding.playPause.setImageResource(R.drawable.ic_pause)
                    viewBinding.totalTime.text = it.song.duration.formattedDuration()
                    viewBinding.currentTime.text = it.progress.formattedDuration()
                }
                is Paused -> {
                    viewBinding.playPause.setImageResource(R.drawable.ic_play_arrow)
                }
                is Erred -> {
                    Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_LONG).show()
                }
                Idle -> Unit
                Loading -> {
                    viewBinding.playPause.setImageResource(R.drawable.ic_loading)
                }
                is Started -> Unit
                is Completed -> Unit
            }
        })

        viewModel.currentUIState.observe(viewLifecycleOwner, {
            if (it.showList) {
                viewBinding.albumImage.visibility = GONE
                viewBinding.currentList.visibility = VISIBLE
            } else {
                viewBinding.albumImage.visibility = VISIBLE
                viewBinding.currentList.visibility = GONE
            }
            val adapter = NowSongsRecyclerAdapter(it.songs) {
                viewModel.play(it)
            }
            viewBinding.currentList.adapter = adapter
            adapter.notifyDataSetChanged()
        })
        viewBinding.playerSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) = Unit
            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.seekTo(seekBar.progress)
            }

        })

        viewBinding.playPause.setOnClickListener {
            viewModel.togglePlay()
        }
        viewBinding.next.setOnClickListener {
            viewModel.next()
        }
        viewBinding.previous.setOnClickListener {
            viewModel.previous()
        }

        viewBinding.queue.setOnClickListener {
            viewModel.listToggle()
        }

        CastButtonFactory.setUpMediaRouteButton(this.requireContext(), viewBinding.mediaRouteButton)
    }
}