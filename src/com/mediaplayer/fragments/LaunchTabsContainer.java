package com.mediaplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shrikanth on 1/21/17.
 */

public class LaunchTabsContainer extends TabsContainer {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setTitle() {

    }



    @Override
    public String getTabTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Songs";
                break;
            case 1:
                title = "Albums";
                break;
            case 2:
                title = "Artists";
                break;
            case 3:
                title = "Playlist";
                break;
            case 4:
                break;
            default:
                break;
        }
        return title;
    }

    @Override
    public MediaFragment getTabFragment(int position) {
        MediaFragment fragment = null;
        switch (position){
            case 0:
                fragment = new SongListFragment();
                break;
            case 1:
                fragment = new AlbumsFragment();
                break;
            case 2:
                fragment = new ArtistsFragment();
                break;
            case 3:
                fragment = new PlaylistsFragment();
                break;
            case 4:
                break;
            default:
                fragment = new SongListFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
