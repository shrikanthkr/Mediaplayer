package com.mediaplayer.com;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mediaplayer.adapter.PlaylistCreationAdapter;
import com.mediaplayer.db.SongInfoDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

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
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.playlist_creation_menu, menu);
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
}
