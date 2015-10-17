package com.mediaplayer.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mediaplayer.adapter.CommonListAdapter;
import com.mediaplayer.com.MetaInfo;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.manager.BroadcastManager;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by shrikanth on 10/2/15.
 */
public class ArtistsFragment extends MultiviewFragment implements AdapterView.OnItemClickListener{
    Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        gridview.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public void setData() {
        database =  SongInfoDatabase.getInstance();
        list = new ArrayList<MetaInfo>();
        list =  database.getArtists(null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MetaInfo info  =list.get(i);
        SongInfoDatabase db =  SongInfoDatabase.getInstance();
        LinkedList<SongInfo> serailaLisedArray = new LinkedList<SongInfo>(db.getSongsForArtist(info));
        Intent playSong = new Intent(BroadcastManager.APPEND_LIST);
        Bundle b= new Bundle();
        b.putSerializable(BroadcastManager.LIST_KEY, serailaLisedArray);
        playSong.putExtras(b);
        LocalBroadcastManager.getInstance(activity).sendBroadcast(playSong);
        Toast.makeText(activity, "Added to Queue", Toast.LENGTH_LONG).show();
    }

    @Override
    public void searchSongs(String search) {
        list = database.getArtists(search);
        adapter.addAll(list);
    }
}
