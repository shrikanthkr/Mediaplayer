package com.em.ui.albums

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.em.app.R
import com.em.repository.Album
import com.em.ui.songs.AlbumItemViewHolder

class AlbumsRecyclerAdapter(private val songs: List<Album>, private val callback: (Album) -> Unit) : RecyclerView.Adapter<AlbumItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_album_item, parent, false) as ViewGroup
        return AlbumItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: AlbumItemViewHolder, position: Int) {
        songs[position].run {
            holder.setTitle(this.title)
            holder.setAlbumArt(this.albumArtPath)
            holder.itemView.setOnClickListener {
                this@AlbumsRecyclerAdapter.callback(this)
            }
        }


    }

}