package com.mediaplayer.customviews;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

/**
 * Created by shrikanth on 1/21/17.
 */

public class BaseImageView extends ImageView {


    RequestManager glide;
    int defaultImage = -1;
    public BaseImageView(Context context) {
        super(context);
        glide = Glide.with(context);
    }

    public BaseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        glide = Glide.with(context);
    }


    public void loadImage(String url){
        glide.load(url).placeholder(defaultImage).error(defaultImage).into(this);
    }

    public void loadImage(Uri url){
        glide.load(url).placeholder(defaultImage).error(defaultImage).into(this);
    }

    public void setDefaultImage(int id){
        defaultImage = id;
    }


}
