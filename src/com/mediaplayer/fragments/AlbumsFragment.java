package com.mediaplayer.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mediaplayer.app.R;
import com.mediaplayer.com.MetaInfo;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.utility.AlbumArtLoader;

import java.util.ArrayList;


/**
 * Created by shrikanth on 10/2/15.
 */
public class AlbumsFragment extends MultiviewFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        //gridview.setOnItemClickListener(this);
        return v;

    }

    @Override
    public void setTitle() {
        getActivity().setTitle(getString(R.string.albums));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setData() {
        database =  SongInfoDatabase.getInstance();
        list = new ArrayList<MetaInfo>();
        list = database.getAlbums(null);
    }

    @Override
    public void setMode() {
        mode = AlbumArtLoader.Mode.ALBUM;
    }

    @Override
    public ArrayList<SongInfo> getToBePlayedData(MetaInfo info) {
        SongInfoDatabase db =  SongInfoDatabase.getInstance();
        return db.getSongsForAlbum(info);
    }

    @Override
    public void searchSongs(String search) {
        list = database.getAlbums(search);
        adapter.addAll(list);
    }

}
