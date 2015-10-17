package com.mediaplayer.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SearchView;
import com.mediaplayer.adapter.GridAdapter;
import com.mediaplayer.com.MetaInfo;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.utility.AlbumArtLoader;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by shrikanth on 10/2/15.
 */
public abstract class MultiviewFragment extends MediaFragment implements SearchView.OnQueryTextListener{
    GridAdapter adapter;
    GridView gridview;
    SongInfoDatabase database;
    ArrayList<MetaInfo> list;
    SearchView searchView;
    AlbumArtLoader.Mode mode;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.multiview_layout_fragment,container,false);
        gridview = (GridView)v.findViewById(R.id.gridview);
        adapter = new GridAdapter(getActivity(), list, gridview,mode);
        gridview.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
    }

    public abstract void setData();

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchSongs(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        searchSongs(s);
        return false;
    }
    public abstract void searchSongs(String search);

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);
        searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(this);
    }
}
