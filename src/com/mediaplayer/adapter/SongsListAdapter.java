package com.mediaplayer.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.utility.ThumbnailLoader;

public class SongsListAdapter extends BaseAdapter {
	private Activity activity;
	ArrayList<SongInfo> song_array;
	private LayoutInflater inflater = null;
	BaseAdapter adapter;
	ListView lv;
	Thread t;
	// SongInfoDatabase database;
	String searchString = "";
	DecimalFormat format;
	int min, sec, total;

	public SongsListAdapter(Activity activity2, ArrayList<SongInfo> song_array,
							ListView lv) {
		activity = activity2;
		this.song_array = song_array;
		this.lv = lv;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.lv = lv;
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

			holder.title.setText(song_array.get(arg0).getTitle());

			total = Integer.parseInt(song_array.get(arg0).getDuration()) / 1000;
			min = total / 60;
			sec = total % 60;
			holder.duration.setText(min + ":" + sec);
			holder.album.setText( song_array.get(arg0).getAlbum());
			holder.artist.setText(song_array.get(arg0).getArtist());
			new ThumbnailLoader(activity,song_array.get(arg0).getAlbum_id(),holder.image).execute();
			/*if (song_array.get(arg0).getTitle().trim().toUpperCase(Locale.US)
					.charAt(0) != song_array.get(arg0 - 1).getTitle().trim()
					.toUpperCase(Locale.US).charAt(0)) {
				holder.header.setVisibility(View.VISIBLE);
				setSection(holder.header, song_array.get(arg0).getTitle());
			} else {
				holder.header.setVisibility(View.GONE);

			}


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
			}*/
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


}