package com.em.mediaplayer.ui.now.playing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.em.mediaplayer.app.R
import com.em.mediaplayer.app.ViewModelFactory
import com.em.mediaplayer.app.activities.home.HomeActivityViewModel
import com.em.mediaplayer.app.cast.CastSessionListener
import com.em.mediaplayer.app.cast.CastSessionListener.CastSessionStatus.Available
import com.em.mediaplayer.app.databinding.FragmentNowplayingBinding
import com.em.mediaplayer.app.di.components.FragmentComponent
import com.em.mediaplayer.app.models.PlayerState.Paused
import com.em.mediaplayer.app.models.PlayerState.Playing
import com.em.mediaplayer.app.utils.load
import com.em.mediaplayer.ui.BaseFragment
import com.em.repository.formattedDuration
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
@FlowPreview
class NowPlayingFragment : BaseFragment() {
    private lateinit var mSessionManager: SessionManager

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewBinding: FragmentNowplayingBinding
    private lateinit var homeActivityViewModel: HomeActivityViewModel
    private lateinit var viewModel: NowPlayingViewModel
    private lateinit var castContext: CastContext
    private val castSessionListener = CastSessionListener()
    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivityViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(HomeActivityViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NowPlayingViewModel::class.java)
        castContext = CastContext.getSharedInstance(this.requireContext())
        mSessionManager = castContext.sessionManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentNowplayingBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.downArrow.setOnClickListener {
            homeActivityViewModel.updateState(BottomSheetBehavior.STATE_HIDDEN)
        }
        viewModel.currentSong.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                viewBinding.album.text = it.album
                viewBinding.songTitle.text = it.title
                viewBinding.artistTitle.text = it.artist
                viewBinding.albumImage.load(it.albumArtPath)
                viewBinding.playerSeekbar.max = it.duration.toInt()
            }
        })
        viewModel.playerState.observe(viewLifecycleOwner, Observer {
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
            }
        })
        viewBinding.playerSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    viewModel.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit

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
        CastButtonFactory.setUpMediaRouteButton(this.requireContext(), viewBinding.mediaRouteButton)
        mSessionManager.addSessionManagerListener(castSessionListener, CastSession::class.java)

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            castSessionListener.castSessionState.collect {
                when (it) {
                    is Available -> {
                        viewModel.switchToCastAdapter(it.session)
                    }
                    else -> {
                        viewModel.toDefaultAdapter()
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mSessionManager.removeSessionManagerListener(castSessionListener, CastSession::class.java)
    }
}