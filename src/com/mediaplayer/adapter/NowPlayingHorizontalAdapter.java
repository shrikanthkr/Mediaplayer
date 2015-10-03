package com.mediaplayer.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.mediaplayer.com.Nowplaying;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.manager.BroadcastManager;
import com.mediaplayer.utility.SongsHolder;
import com.mediaplayer.utility.Util;

public class NowPlayingHorizontalAdapter extends HorizontalAdapter {

	public NowPlayingHorizontalAdapter(ArrayList<SongInfo> song_array, HorizontalListView lv, Activity activity) {
		super(song_array,lv,activity);
	}

	@Override
	public void setLayoutName() {
		id = R.layout.nowplaying_horizonal_songitem;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		SongInfo songInfo = song_array.get(arg2);
		Intent playSong = new Intent(BroadcastManager.PLAYSONG);
		Bundle b= new Bundle();
		b.putSerializable(BroadcastManager.SONG_KEY, songInfo);
		playSong.putExtras(b);
		LocalBroadcastManager.getInstance(activity).sendBroadcast(playSong);

	}

}
