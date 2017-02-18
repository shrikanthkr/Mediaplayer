package com.mediaplayer.adapter;

import android.app.Activity;
import android.content.ContentUris;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mediaplayer.app.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.com.SongsManager;
import com.mediaplayer.customviews.BaseImageView;
import com.mediaplayer.interfaces.RecyclerClickHelper;

import java.util.ArrayList;


public class NowPlayingHorizontalAdapter extends  BaseRecyclerAdapter<NowPlayingHorizontalAdapter.ViewHolder>  {

	ArrayList<SongInfo> songArray;
	Activity context;
	RecyclerView rv;
	public NowPlayingHorizontalAdapter(ArrayList<SongInfo> songArray, RecyclerView rv, Activity activity, RecyclerClickHelper recyclerClickHelper) {
		super(recyclerClickHelper);
		this.songArray = songArray;
		context = activity;
		this.rv = rv;
	}




	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		super.onCreateViewHolder(parent,viewType);
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.nowplaying_horizonal_songitem, parent, false);
		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		super.onBindViewHolder(holder,position);
		holder.songName.setText(songArray.get(position).getDisplayName());
		Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), Long.parseLong(songArray.get(position).getId()) );
		holder.album.setDefaultImage(R.drawable.albums);
		holder.album.loadImage(uri);
		if(SongsManager.getInstance().getCurrentSongInfo()!=null
				&& songArray.get(position).getId()  == SongsManager.getInstance().getCurrentSongInfo().getId()){
			holder.songName.setTextColor(context.getResources().getColor(R.color.base_dark));
		}else{
			holder.songName.setTextColor(context.getResources().getColor(R.color.light_text_color));
		}
	}

	@Override
	public int getItemCount() {
		return songArray.size();
	}

	public void addAll(ArrayList<SongInfo> horizontal_songInfo_array) {
		if(songArray == null){
			songArray = new ArrayList<>();
		}
		songArray.clear();
		songArray.addAll(horizontal_songInfo_array);
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		TextView songName;
		BaseImageView album;
		public ViewHolder(View v) {
			super(v);
			songName = (TextView)v.findViewById(R.id.song_name_textView);
			album = (BaseImageView)v.findViewById(R.id.song_imageView);
		}
	}
}
