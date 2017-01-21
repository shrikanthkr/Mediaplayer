package com.mediaplayer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * Created by shrikanth on 10/4/15.
 */
public abstract class MediaFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        setTitle();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public abstract void setTitle();
}
