package com.mediaplayer.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.ImageDownloader;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.utility.StaticMusic;

public class ReadLisstAdapter extends BaseAdapter implements OnScrollListener {
	private Activity activity;
	ArrayList<SongInfo> song_array;
	private LayoutInflater inflater = null;
	BaseAdapter adapter;
	ListView lv;
	Thread t;
	public ImageDownloader imageLoader;
	// SongInfoDatabase database;
	String searchString = "";
	DecimalFormat format;
	int min, sec, total;

	public ReadLisstAdapter(Activity activity2, ArrayList<SongInfo> song_array,
			ListView lv) {
		activity = activity2;
		this.song_array = song_array;
		this.lv = lv;
		imageLoader = new ImageDownloader(this,
				activity.getApplicationContext());
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.lv = lv;
		imageLoader.loadImage(0, 5);
		this.lv.setOnScrollListener(this);
		//Log.i("READLIST ADAPTER", "CONSTRUCTOR CREATED");
		format = new DecimalFormat("#.00");

	}

	public ArrayList<SongInfo> getUrlList() {
		return song_array;
	}

	public void setUrlList(ArrayList<SongInfo> song_array) {
		this.song_array = song_array;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return song_array.size();
	}

	public ListView getListView() {
		return lv;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return super.getViewTypeCount();
	}

	public class ViewHolder {
		public TextView title;
		public TextView duration;
		public TextView album;
		public TextView artist;
		public ImageView image;
		public TextView header;
		int pos;
	}

	public View getView(final int arg0, View vi, ViewGroup arg2) {
		ViewHolder holder;

		if (vi == null) {

			holder = new ViewHolder();
			vi = inflater.inflate(R.layout.songlist_item, null);
			holder.header = (TextView) vi
					.findViewById(R.id.label_header_textview);
			holder.title = (TextView) vi.findViewById(R.id.song_textView);
			holder.image = (ImageView) vi.findViewById(R.id.songlist_imageView);
			holder.album = (TextView) vi.findViewById(R.id.song_album_textView);
			holder.artist = (TextView) vi
					.findViewById(R.id.song_artist_textView);
			holder.duration = (TextView) vi
					.findViewById(R.id.song_duration_textView);
			holder.pos = arg0;
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		try {

			holder.title.setText(song_array.get(arg0).getTitle());

			total = Integer.parseInt(song_array.get(arg0).getDuration()) / 1000;
			min = total / 60;
			sec = total % 60;
			holder.duration.setText(min + ":" + sec);
			holder.album.setText("from " + song_array.get(arg0).getAlbum());
			holder.artist.setText("by " + song_array.get(arg0).getArtist());
			Uri albumArtUri = Uri
					.parse("content://media/external/audio/albumart");
			final Uri uri = ContentUris.withAppendedId(albumArtUri,
					Long.parseLong(song_array.get(arg0).getAlbum_id()));
			holder.image.setImageBitmap(imageLoader.getDrawble(uri.toString()));

			if (song_array.get(arg0).getTitle().trim().toUpperCase(Locale.US)
					.charAt(0) != song_array.get(arg0 - 1).getTitle().trim()
					.toUpperCase(Locale.US).charAt(0)) {
				holder.header.setVisibility(View.VISIBLE);
				setSection(holder.header, song_array.get(arg0).getTitle());
			} else {
				holder.header.setVisibility(View.GONE);

			}

		} catch (Exception e) {
			setSection(holder.header, song_array.get(arg0).getTitle());
		}
		try{
			if (song_array.get(arg0).getData()
					.equals(StaticMusic.songInfo.getData())) {
				holder.title.setTextColor(Color.parseColor("#32b3e6"));
				holder.title.setSelected(true);
				holder.title.setEllipsize(TruncateAt.MARQUEE);
				holder.title.setSingleLine(true);
				holder.title.setMarqueeRepeatLimit(-1);
				
			}
			else{
				holder.title.setTextColor(Color.parseColor("#ffffff"));
				holder.title.setEllipsize(TruncateAt.END);
			}
		}catch(Exception e){}
		return vi;
	}

	private void setSection(TextView text, String label) {
		// text.setBackgroundColor(0xffe47168);
		text.setBackgroundResource(R.drawable.blue_strip);
		text.setText((label.substring(0, 1) + "").toUpperCase());
		text.setTextSize(15);
		text.setPadding(5, 0, 0, 0);
		text.setGravity(Gravity.CENTER_VERTICAL);

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		switch (scrollState) {
		case SCROLL_STATE_IDLE:
			StaticMusic.smoothScrollTo = lv.getFirstVisiblePosition();
			imageLoader.loadImage(lv.getFirstVisiblePosition(),
					lv.getLastVisiblePosition());
			break;

		}

	}

	@Override
	public void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		imageLoader.finalize();
	}

}