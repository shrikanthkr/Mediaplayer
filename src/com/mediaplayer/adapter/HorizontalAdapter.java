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
import android.widget.ListView;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.mediaplayer.com.Nowplaying;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.manager.BroadcastManager;
import com.mediaplayer.utility.SongsHolder;
import com.mediaplayer.utility.Util;

public abstract class HorizontalAdapter extends BaseAdapter implements
		OnItemClickListener {

	protected Activity activity;
	ArrayList<SongInfo> song_array;
	protected LayoutInflater inflater = null;
	BaseAdapter adapter;
	Thread t;
	HorizontalListView lv;
	HashMap<String, Bitmap> art_work;
	Util util;
	int id;

	public HorizontalAdapter(ArrayList<SongInfo> song_array, HorizontalListView lv, Activity activity) {
		this.song_array = song_array;
		this.activity = activity;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.lv = lv;
		util = new Util();
		lv.setOverScrollMode(ListView.OVER_SCROLL_ALWAYS);
		lv.setOnItemClickListener(this);
		setLayoutName();

	}
	public abstract void setLayoutName();

	public ArrayList<SongInfo> getUrlList() {
		return song_array;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return song_array.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View vi, ViewGroup arg2) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		if (vi == null) {
			holder = new ViewHolder();
			vi = inflater.inflate(id, null);
			holder.song_name = (TextView) vi.findViewById(R.id.song_name_textView);
			holder.album_art = (ImageView) vi.findViewById(R.id.song_imageView);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
		holder.song_name.setText(song_array.get(arg0).getTitle());
		try {
			Uri albumArtUri = Uri
					.parse("content://media/external/audio/albumart");
			final Uri uri = ContentUris.withAppendedId(albumArtUri,
					Long.parseLong(song_array.get(arg0).getAlbum_id()));
			//imageLoader.DisplayImage(uri.toString(), holder.album_art);

		} catch (NumberFormatException e) {
		}
		return vi;
	}

	public class ViewHolder {
		ImageView album_art;
		TextView song_name;
	}



}
