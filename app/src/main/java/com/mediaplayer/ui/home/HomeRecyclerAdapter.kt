package com.mediaplayer.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mediaplayer.app.R
import com.mediaplayer.repository.Song
import com.mediaplayer.repository.albumArtPath

class HomeRecyclerAdapter(private val songs: List<Song>) : RecyclerView.Adapter<HomeItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_home_item, parent, false) as ViewGroup
        return HomeItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
        holder.setTitle(songs[position].title)
        holder.setSubTitle(songs[position].album)
        holder.setAlbumArt(songs[position].albumArtPath())
    }

}