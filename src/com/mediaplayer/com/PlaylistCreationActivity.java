package com.mediaplayer.com;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

import com.mediaplayer.adapter.PlaylistCreationAdapter;
import com.mediaplayer.db.SongInfoDatabase;

import java.util.ArrayList;

public class PlaylistCreationActivity extends Activity {

    ListView lv;
    PlaylistCreationAdapter adapter;
    ArrayList<SongInfo> song_array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_creation);
        this.getActionBar().setTitle("Create Playlist");
        lv = (ListView)findViewById(R.id.listview);
        setupAdapter();
    }

    private void setupAdapter() {
        song_array = new ArrayList<>();
        song_array = SongInfoDatabase.getInstance().getSongs(null);
        adapter = new PlaylistCreationAdapter(this,song_array,lv);
        lv.setFastScrollEnabled(true);
        lv.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

}
