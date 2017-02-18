package com.mediaplayer.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mediaplayer.app.R;

/**
 * Created by shrikanth on 1/21/17.
 */

public class SongListViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView duration;
    public TextView album;
    public TextView section_headerview;

    public SongListViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.song_textView);
        album = (TextView) itemView.findViewById(R.id.song_album_textView);
        duration = (TextView) itemView.findViewById(R.id.song_duration_textView);
        section_headerview = (TextView) itemView.findViewById(R.id.section_headerview);
    }

}
