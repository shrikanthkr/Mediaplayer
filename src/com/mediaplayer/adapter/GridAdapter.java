package com.mediaplayer.adapter;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mediaplayer.app.R;
import com.mediaplayer.com.MetaInfo;
import com.mediaplayer.fragments.MultiviewFragment;
import com.mediaplayer.utility.AlbumArtLoader;
import com.mediaplayer.viewholders.GridViewHolder;

import java.util.ArrayList;

public class GridAdapter extends BaseRecyclerAdapter<GridViewHolder>{

	private ArrayList<MetaInfo> infos;
	private LayoutInflater inflater = null;
	private RecyclerView gv;
	AlbumArtLoader.Mode current;
	MultiviewFragment fragment;

	public GridAdapter(Activity activity, ArrayList<MetaInfo> infos, RecyclerView gv, AlbumArtLoader.Mode mode, MultiviewFragment fragment) {
		super(null);
		this.infos = infos;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.gv = gv;
		current = mode;
		this.fragment = fragment;
	}



	@Override
	public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		super.onCreateViewHolder( parent, viewType);
		View v = inflater.inflate(R.layout.grid_listitem_xml, parent, false);
		return new GridViewHolder(v);
	}

	@Override
	public void onBindViewHolder(GridViewHolder holder, int position) {
		super. onBindViewHolder(holder,position);
		holder.more_layout.setOnClickListener(moreListener);
		holder.name.setText(infos.get(position).getName());
		String id = infos.get(position).getId();
		Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), Long.parseLong(id) );
		holder.album.setDefaultImage(R.drawable.albums);
		holder.album.loadImage(uri);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return infos.size();
	}


	public void addAll(ArrayList<MetaInfo> infos){
		this.infos.clear();
		this.infos = infos;
		notifyDataSetChanged();
	}


	View.OnClickListener moreListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		}
	};



}
