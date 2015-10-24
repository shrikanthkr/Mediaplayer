package com.mediaplayer.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.com.SongsManager;
import com.mediaplayer.manager.BroadcastManager;

import org.w3c.dom.Text;

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

	@Override
	public int getCount() {
		return song_array.size();
	}

	@Override
	public View getView(int arg0, View vi, ViewGroup arg2) {
		View v  = super.getView(arg0, vi, arg2);
		if(SongsManager.getInstance().getCurrentSongInfo()!=null && song_array.get(arg0).getId()  == SongsManager.getInstance().getCurrentSongInfo().getId()){
			((TextView)v.findViewById(R.id.song_name_textView)).setTextColor(activity.getResources().getColor(R.color.base_dark));
		}else{
			((TextView)v.findViewById(R.id.song_name_textView)).setTextColor(activity.getResources().getColor(R.color.light_text_color));

		}
		return v;
	}
}
