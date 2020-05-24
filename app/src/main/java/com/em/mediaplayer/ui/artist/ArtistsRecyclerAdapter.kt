package com.em.mediaplayer.ui.artist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.em.mediaplayer.app.R
import com.em.repository.Artist

class ArtistsRecyclerAdapter(private val artists: List<Artist>, private val callback: (Artist) -> Unit) : RecyclerView.Adapter<ArtistItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_artist_item, parent, false) as ViewGroup
        return ArtistItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    override fun onBindViewHolder(holder: ArtistItemViewHolder, position: Int) {
        artists[position].run {
            holder.setTitle(this.name)
            holder.itemView.setOnClickListener {
                this@ArtistsRecyclerAdapter.callback(this)
            }
        }
    }

}