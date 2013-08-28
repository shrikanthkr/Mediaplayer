package com.mediaplayer.adapter;

import com.mediaplayer.com.AudioFxDemo;
import com.mediaplayer.com.R;
import com.mediaplayer.utility.StaticMusic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MySpinnerAdapter extends ArrayAdapter<String> {

	LayoutInflater inflater;
	Activity activity;
	String[] presets;

	public MySpinnerAdapter(Activity activity, int textViewResourceId,
			String[] objects) {
		super(activity, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		inflater = activity.getLayoutInflater();
		presets = objects;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// return super.getView(position, convertView, parent);

		View row = inflater.inflate(R.layout.spinner_listitem, parent, false);
		TextView label = (TextView) row.findViewById(R.id.preset_textView);
		label.setText(presets[position]);
		if(StaticMusic.band_equi==position){
			label.setTextColor(Color.parseColor("#ff5a00"));
		}
		return row;
	}
}