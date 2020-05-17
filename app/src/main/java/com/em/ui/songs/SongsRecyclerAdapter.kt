package com.em.ui.songs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.em.app.R
import com.em.repository.Song

class SongsRecyclerAdapter(private val songs: List<Song>, private val callback: (Song) -> Unit) : RecyclerView.Adapter<SongsItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_home_item, parent, false) as ViewGroup
        return SongsItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongsItemViewHolder, position: Int) {
        songs[position].run {
            holder.setTitle(title)
            holder.setSubTitle(artist)
            holder.setAlbumArt(albumArtPath)
            holder.itemView.setOnClickListener {
                this@SongsRecyclerAdapter.callback(this)
            }
        }


    }

}