package com.mediaplayer.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mediaplayer.com.MetaInfo;
import com.mediaplayer.com.PlaylistCreationActivity;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.manager.BroadcastManager;
import com.mediaplayer.utility.AlbumArtLoader;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by shrikanth on 10/2/15.
 */
public class PlaylistsFragment extends MultiviewFragment  implements AdapterView.OnItemClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  super.onCreateView(inflater, container, savedInstanceState);
        gridview.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void setTitle() {
        getActivity().setTitle(getString(R.string.playlist));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = AlbumArtLoader.Mode.PLAYLIST;
    }

    @Override
    public void setData() {
        database =  SongInfoDatabase.getInstance();
        list = database.getPLaylists(null);
    }

    @Override
    public void searchSongs(String search) {
        list = database.getPLaylists(search);
        adapter.addAll(list);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MetaInfo info  =list.get(i);
        SongInfoDatabase db =  SongInfoDatabase.getInstance();
        LinkedList<SongInfo> serailaLisedArray = new LinkedList<SongInfo>(db.getSongsForPlaylist(info));
        Intent playSong = new Intent(BroadcastManager.APPEND_LIST);
        Bundle b= new Bundle();
        b.putSerializable(BroadcastManager.LIST_KEY, serailaLisedArray);
        playSong.putExtras(b);
        LocalBroadcastManager.getInstance(activity).sendBroadcast(playSong);
        Toast.makeText(activity, "Added to Queue", Toast.LENGTH_LONG).show();
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
        Intent i = new Intent(getActivity(),PlaylistCreationActivity.class);
        startActivity(i);
    }
}

