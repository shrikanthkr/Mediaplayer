package com.mediaplayer.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mediaplayer.adapter.CommonListAdapter;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;

import java.util.ArrayList;

/**
 * Created by shrikanth on 10/2/15.
 */
public class AlbumsFragment extends Fragment {
    CommonListAdapter adapter;
    ListView listview;
    SongInfoDatabase database;
    ArrayList<ArrayList<SongInfo>> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.albums_fragment,container,false);
        listview = (ListView)v.findViewById(R.id.listView);
        listview.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new SongInfoDatabase(getActivity());
        database.open();
        list = new ArrayList<ArrayList<SongInfo>>();
        list = database.getSongs_albums();
        // database.close();
        adapter = new CommonListAdapter(getActivity(), list, listview, 1, null);
        database.close();
    }
}
