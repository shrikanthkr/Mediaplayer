package com.em.mediaplayer.ui.songs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.em.mediaplayer.app.databinding.ViewholderHomeItemBinding
import com.em.repository.Song

class SongsRecyclerAdapter(private val songs: List<Song>, private val itemClick: (Song) -> Unit, private val addToQueue: (Song) -> Unit) : RecyclerView.Adapter<SongsItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsItemViewHolder {
        val binding = ViewholderHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongsItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongsItemViewHolder, position: Int) {
        songs[position].run {
            holder.setTitle(title)
            holder.setSubTitle(artist)
            holder.setAlbumArt(this.uri)
            holder.itemView.setOnClickListener {
                this@SongsRecyclerAdapter.itemClick(this)
            }
            holder.setQueueClick {
                this@SongsRecyclerAdapter.addToQueue(this)
            }

        }
    }

}