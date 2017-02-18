package com.mediaplayer.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mediaplayer.adapter.SongsShowAdapter;
import com.mediaplayer.app.R;
import com.mediaplayer.com.MetaInfo;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.com.SongsManager;
import com.mediaplayer.db.SongInfoDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SongsShowActivity extends Activity {

    public static final String MODE_KEY = "mode_key";
    public static final String ID_KEY = "id";
    public static final String NAME_KEY = "name_key";
    String currentId = "", currentName;
    public enum SHOW_MODE{
        PLAY_LIST,
        PLAYLIST_CREATION,
        ALBUMS,
        ARTISTS,
        NOW_PLAYING
    }
    SHOW_MODE CURRENT_MODE = SHOW_MODE.PLAYLIST_CREATION;
    ListView lv;
    SongsShowAdapter adapter;
    ArrayList<SongInfo> song_array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_creation);
        this.getActionBar().setTitle("Create Playlist");
        lv = (ListView)findViewById(R.id.listview);
        Bundle b = getIntent().getExtras();
        if(b!=null){
            CURRENT_MODE = (SHOW_MODE)b.getSerializable(MODE_KEY);
            currentId = b.getString(ID_KEY);
            currentName = b.getString(NAME_KEY);
        }
        setupAdapter();
    }

    private void setupAdapter() {
        Map<String, SongInfo> selected_song_array = new HashMap<>();
        MetaInfo info;
        song_array = new ArrayList<>();
        adapter = new SongsShowAdapter(this,song_array,lv);
        lv.setFastScrollEnabled(true);
        lv.setAdapter(adapter);
        lv.setClickable(false);
        song_array.clear();
        switch (CURRENT_MODE){
            case PLAYLIST_CREATION:
                song_array.addAll(SongInfoDatabase.getInstance().getSongs(null) );
                adapter.setListener();
                break;
            case ALBUMS:
                info = new MetaInfo(currentId,currentName);
                song_array.addAll(SongInfoDatabase.getInstance().getSongsForAlbum(info));
                break;
            case ARTISTS:
                info = new MetaInfo(currentId,currentName);
                song_array.addAll(SongInfoDatabase.getInstance().getSongsForArtist(info));
                break;
            case NOW_PLAYING:
                song_array.addAll(SongsManager.getInstance().getSongsList());
                for (int i=0;i<song_array.size();i++){
                    selected_song_array.put(song_array.get(i).getId(),song_array.get(i));
                }
                adapter.setListener();
                break;
            case PLAY_LIST:
                info = new MetaInfo(currentId,currentName);
                song_array.addAll(SongInfoDatabase.getInstance().getSongsForPlaylist(info));
                adapter.setListener();
                break;
        }
        if(selected_song_array.size() > 0){
            adapter.setSelected_song_array(selected_song_array);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.playlist_creation_menu, menu);
        MenuItem item = menu.findItem(R.id.create_playlist);
        switch (CURRENT_MODE){
            case ALBUMS:
            case ARTISTS:
                item.setVisible(false);
                getActionBar().setTitle(currentName);
                break;
            case NOW_PLAYING:
            case PLAY_LIST:
                item.setVisible(true);
                getActionBar().setTitle(currentName);
                break;
            case PLAYLIST_CREATION:
                item.setVisible(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.create_playlist:
                showDialog();
                break;
        }
        return true;
    }



    private void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View v = this.getLayoutInflater().inflate(R.layout.playlist_save_dialog,null);
        final EditText editText = (EditText)v.findViewById(R.id.playlist_name);
        dialogBuilder.setView(v);
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editText.getText().toString();
                long[] ids = getLongArray();
                if (name != null && name.length() > 0) {
                    SongInfoDatabase.getInstance().createNewPLaylist(ids,name);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a valid name", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog  = dialogBuilder.create();
        alertDialog.show();
    }

    private long[] getLongArray(){
        Map<String,SongInfo> selected_song_array = adapter.getSelected_song_array();
        Iterator it = selected_song_array.entrySet().iterator();
        long[] ids  = new long[selected_song_array.entrySet().size()];
        int counter = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ids[counter++] = Long.parseLong((String)pair.getKey());
            it.remove();
        }
        return  ids;
    }

    public SHOW_MODE getCURRENT_MODE() {
        return CURRENT_MODE;
    }

    public void setCURRENT_MODE(SHOW_MODE CURRENT_MODE) {
        this.CURRENT_MODE = CURRENT_MODE;
    }
}
