package com.mediaplayer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;

import com.devsmart.android.ui.HorizontalListView;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.manager.BroadcastManager;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by shrikanth on 10/3/15.
 */
public class AlbumViewAdapter  extends HorizontalAdapter{

    public AlbumViewAdapter(ArrayList<SongInfo> song_array, HorizontalListView lv, Activity activity) {
        super(song_array, lv, activity);
    }

    @Override
    public void setLayoutName() {
        id= R.layout.horizontal_songitem_xml;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        SongInfo songInfo = song_array.get(arg2);
        LinkedList<SongInfo> serailaLisedArray = new LinkedList<SongInfo>(song_array);
        Intent playSong = new Intent(BroadcastManager.APPEND_LIST);

        Bundle b= new Bundle();
        b.putSerializable(BroadcastManager.SONG_KEY, songInfo);
        b.putSerializable(BroadcastManager.LIST_KEY,serailaLisedArray);
        playSong.putExtras(b);
        LocalBroadcastManager.getInstance(activity).sendBroadcast(playSong);

    }
}
