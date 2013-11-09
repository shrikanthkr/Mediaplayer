package com.mediaplayer.adapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
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
import com.mediaplayer.db.ImageLoader;
import com.mediaplayer.utility.StaticMusic;
import com.mediaplayer.utility.Util;

public class HorizontalAdapter extends BaseAdapter implements
		OnItemClickListener {

	private Activity activity;
	ArrayList<SongInfo> song_array;
	private LayoutInflater inflater = null;
	BaseAdapter adapter;
	Thread t;
	HorizontalListView lv;
	HashMap<String, Bitmap> art_work;
	Util util;
	final int id = R.layout.horizontal_songitem_xml;
	ImageLoader imageLoader;

	public HorizontalAdapter(ArrayList<SongInfo> song_array,
			HorizontalListView lv, Activity activity) {
		this.song_array = song_array;
		this.activity = activity;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.lv = lv;
		util = new Util();
		lv.setOverScrollMode(ListView.OVER_SCROLL_ALWAYS);
		lv.setOnItemClickListener(this);
		imageLoader = new com.mediaplayer.db.ImageLoader(
				activity.getApplicationContext());
		//Log.i("Code PATH", activity.getApplicationContext().getPackageName());

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
	public View getView(int arg0, View vi, ViewGroup arg2) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		if (vi == null) {
			holder = new ViewHolder();
			vi = inflater.inflate(id, null);
			holder.song_name = (TextView) vi
					.findViewById(R.id.song_name_textView);
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
			imageLoader.DisplayImage(uri.toString(), holder.album_art);

		} catch (NumberFormatException e) {
		}
		return vi;
	}

	public class ViewHolder {
		ImageView album_art;
		TextView song_name;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		// Toast.makeText(activity, song_array.get(arg2).getTitle(),
		// Toast.LENGTH_SHORT).show();
		StaticMusic.setSongInfo(song_array.get(arg2));
		try {
			StaticMusic.songQueue.clear();
			StaticMusic.songQueue = new LinkedList<SongInfo>(song_array);
		} catch (NullPointerException nu) {
			StaticMusic.songQueue = new LinkedList<SongInfo>(song_array);
		}
		Intent toNowPlaying = new Intent(activity, Nowplaying.class);
		toNowPlaying.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		toNowPlaying.putExtra("activity", "songlist");
		activity.startActivity(toNowPlaying);

	}

	@Override
	public void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		imageLoader.clearCache();
	}

}
