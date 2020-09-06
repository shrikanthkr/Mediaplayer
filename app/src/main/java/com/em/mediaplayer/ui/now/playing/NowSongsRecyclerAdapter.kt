package com.em.mediaplayer.ui.now.playing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.em.mediaplayer.app.databinding.ViewholderSongSmallItemBinding
import com.em.repository.Song

class NowSongsRecyclerAdapter(private val songs: List<Song>, private val itemClick: (Song) -> Unit) : RecyclerView.Adapter<SongsItemSmallViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsItemSmallViewHolder {
        val binding = ViewholderSongSmallItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongsItemSmallViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongsItemSmallViewHolder, position: Int) {
        songs[position].run {
            holder.setTitle(title)
            holder.itemView.setOnClickListener {
                this@NowSongsRecyclerAdapter.itemClick(this)
            }

        }
    }

}