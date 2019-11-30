package com.mediaplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mediaplayer.app.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the [AlbumsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albums, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = AlbumsFragment()
    }
}
