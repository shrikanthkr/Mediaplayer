package com.mediaplayer.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mediaplayer.com.R;
import com.mediaplayer.customviews.BaseImageView;

/**
 * Created by shrikanth on 1/21/17.
 */
public class GridViewHolder extends RecyclerView.ViewHolder{
    public TextView name;
    public BaseImageView album;
    public LinearLayout more_layout;

    public GridViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.song_name);
        album = (BaseImageView) itemView.findViewById(R.id.album_imageView);
        more_layout = (LinearLayout)itemView.findViewById(R.id.more_layout);
    }
}
