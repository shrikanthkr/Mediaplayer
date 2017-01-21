package com.mediaplayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mediaplayer.com.MetaInfo;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.com.SongsShowActivity;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.utility.AlbumArtLoader;

import java.util.ArrayList;

/**
 * Created by shrikanth on 10/2/15.
 */
public class PlaylistsFragment extends MultiviewFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  super.onCreateView(inflater, container, savedInstanceState);
        //gridview.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void setTitle() {
        getActivity().setTitle(getString(R.string.playlist));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setData() {
        database =  SongInfoDatabase.getInstance();
        list = database.getPLaylists(null);
    }

    @Override
    public void setMode() {
        mode = AlbumArtLoader.Mode.PLAYLIST;
    }

    @Override
    public ArrayList<SongInfo> getToBePlayedData(MetaInfo info) {
        SongInfoDatabase db =  SongInfoDatabase.getInstance();
        return db.getSongsForPlaylist(info);
    }

    @Override
    public void searchSongs(String search) {
        list = database.getPLaylists(search);
        adapter.addAll(list);

    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
        adapter.addAll(list);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.playlist_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.add_playlist:
                launchPlaylistCreation();
                break;
            default:
                break;
        }
        return true;
    }

    private void launchPlaylistCreation() {
        Intent i = new Intent(getActivity(),SongsShowActivity.class);
        startActivity(i);
    }
}

