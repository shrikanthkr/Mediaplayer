package com.em.mediaplayer.ui.artist

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.em.mediaplayer.app.R

class ArtistItemViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {

    private var title: TextView = itemView.findViewById(R.id.title)

    fun setTitle(title: String) {
        this.title.text = title
    }

}