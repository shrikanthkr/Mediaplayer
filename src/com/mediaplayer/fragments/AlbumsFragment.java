package com.mediaplayer.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mediaplayer.adapter.CommonListAdapter;
import com.mediaplayer.adapter.SongsListAdapter;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;

import java.util.ArrayList;

/**
 * Created by shrikanth on 10/2/15.
 */
public class AlbumsFragment extends MultiviewFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setData() {
        database = new SongInfoDatabase(getActivity());
        database.open();
        list = new ArrayList<ArrayList<SongInfo>>();
        list = database.getSongs_albums();
        database.close();
    }

    @Override
    public void searchSongs(String search) {
        database.open();
        list = database.searchSongs_albums(search);
        adapter.addAll(list);
        database.close();
    }
}
