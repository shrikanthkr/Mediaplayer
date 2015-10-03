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
import com.mediaplayer.utility.SongsHolder;
import com.mediaplayer.utility.Util;

public class NowPlayingHorizontalAdapter extends BaseAdapter implements  OnItemClickListener {

	private Activity activity;
	ArrayList<SongInfo> song_array;
	private LayoutInflater inflater = null;
	BaseAdapter adapter;
	Thread t;
	HorizontalListView lv;
	HashMap<String, Bitmap> art_work;
	Util util;
	int id=R.layout.nowplaying_horizonal_songitem;
	public NowPlayingHorizontalAdapter(ArrayList<SongInfo> song_array, HorizontalListView lv,
			Activity activity) {
		this.song_array = song_array;
		this.activity = activity;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.lv = lv;
		util=new Util();
		lv.setOnItemClickListener(this);
		//Log.i("Code PATH",activity.getApplicationContext().getPackageName());

	}
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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final View vi;
		final ViewHolder holder;
		holder = new ViewHolder();
		vi = inflater.inflate(id, null);
		holder.song_name = (TextView) vi.findViewById(R.id.song_name_textView);
		holder.album_art = (ImageView) vi.findViewById(R.id.song_imageView);
		holder.song_name.setText(song_array.get(arg0).getTitle());
		try{
			Uri albumArtUri = Uri
					.parse("content://media/external/audio/albumart");
			Uri uri = ContentUris.withAppendedId(albumArtUri,
					Long.parseLong(song_array.get(arg0).getAlbum_id()));
			//imageLoader.DisplayImage(uri.toString(), holder.album_art);
		}catch(NumberFormatException e){}
		return vi;
	}

	public class ViewHolder {
		ImageView album_art;
		TextView song_name;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		//Toast.makeText(activity, song_array.get(arg2).getTitle(), Toast.LENGTH_SHORT).show();
		/*SongsHolder.setSongInfo(song_array.get(arg2));
		try{
			SongsHolder.songQueue.clear();
			SongsHolder.songQueue=new LinkedList<SongInfo>(song_array);
		}catch(NullPointerException nu){
			SongsHolder.songQueue=new LinkedList<SongInfo>(song_array);
		}
		Intent toNowPlaying = new Intent(activity, Nowplaying.class);
		toNowPlaying.putExtra("activity", "songlist");
		toNowPlaying.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(toNowPlaying);*/


	}

}
