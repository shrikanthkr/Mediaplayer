package com.mediaplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mediaplayer.app.R;
import com.mediaplayer.interfaces.RecyclerClickHelper;
import com.mediaplayer.models.Radio;

import java.util.List;

/**
 * Created by shrikanth on 2/18/17.
 */

public class RadioRecyclerAdapter extends BaseRecyclerAdapter<RadioRecyclerAdapter.RadioViewHolder> {

    List<Radio> stations;
    public RadioRecyclerAdapter(RecyclerClickHelper clickHelper) {
        super(clickHelper);
    }

    @Override
    public RadioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.radio_station_item, parent, false);
        RadioViewHolder vh = new RadioViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RadioViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Radio r = stations.get(position);
        holder.radioName.setText(r.streamTitle);
    }

    @Override
    public int getItemCount() {
        return stations != null ? stations.size() : 0;
    }

    public class RadioViewHolder extends RecyclerView.ViewHolder {

        TextView radioName;
        public RadioViewHolder(View itemView) {
            super(itemView);
            radioName = (TextView) itemView.findViewById(R.id.name);
        }
    }

    public void setStations(List<Radio> stations){
        this.stations = stations;
    }
}
