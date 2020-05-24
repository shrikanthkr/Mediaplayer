package com.em.mediaplayer.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.em.mediaplayer.app.ViewModelFactory
import com.em.mediaplayer.app.activities.home.HomeActivityViewModel
import com.em.mediaplayer.app.databinding.SearchFragmentBinding
import com.em.mediaplayer.app.di.components.FragmentComponent
import com.em.mediaplayer.ui.BaseFragment
import com.em.mediaplayer.ui.songs.SongsRecyclerAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject


@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
class SearchFragment : BaseFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: SearchViewModel
    private lateinit var homeActivityViewModel: HomeActivityViewModel
    private lateinit var viewBinding: SearchFragmentBinding
    private var myPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPosition = requireArguments().getInt(MY_POSITION)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewBinding = SearchFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
        homeActivityViewModel = ViewModelProvider(this.requireActivity(), viewModelFactory).get(HomeActivityViewModel::class.java)
        viewBinding.songs.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.songs.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        viewModel.songsLiveData.observe(viewLifecycleOwner, Observer {
            viewBinding.songs.adapter = SongsRecyclerAdapter(it, { song ->
                viewModel.play(song)
            }, { song ->
                viewModel.addToQueue(song)
            })
        })
        viewBinding.editQuery.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.search(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

        })

        homeActivityViewModel.scrollTo.observe(viewLifecycleOwner, Observer {
            if (myPosition == it) {
                viewBinding.songs.scrollToPosition(0)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewBinding.editQuery.postDelayed({
            viewBinding.editQuery.requestFocus()
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(viewBinding.editQuery, InputMethodManager.SHOW_IMPLICIT)
        }, 300)
    }

    companion object {
        private const val MY_POSITION = "POSITION"

        fun newInstance(position: Int): BaseFragment {
            val args = Bundle()
            args.putInt(MY_POSITION, position)
            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }


}