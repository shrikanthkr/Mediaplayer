package com.mediaplayer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by shrikanth on 10/24/15.
 */
public class PlaylistCreationAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{
    private Activity activity;
    ArrayList<SongInfo> song_array;
    Map<String,SongInfo> selected_song_array;
    private LayoutInflater inflater = null;
    ListView lv;

    final static int SECTION = 2;
    final static int NORMAL = 1;
    int[] rowStates;

    public PlaylistCreationAdapter(Activity activity, ArrayList<SongInfo> song_array, ListView lv) {
        this.activity = activity;
        this.song_array = song_array;
        this.inflater = activity.getLayoutInflater();
        this.lv = lv;
        selected_song_array = new HashMap<>();
        lv.setOnItemClickListener(this);
        rowStates = new int[getCount()];
    }

    @Override
    public int getCount() {
        return song_array.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder {
        public TextView title;
        public TextView section_headerview;
        public ImageView selected;
    }
    @Override
    public View getView(int arg0, View vi, ViewGroup parent) {
        ViewHolder holder;
        boolean showSeparator = false;
        if (vi == null) {

            holder = new ViewHolder();
            vi = inflater.inflate(R.layout.playlist_creation_songlist_item, null);
            holder.title = (TextView) vi.findViewById(R.id.song_textView);
            holder.section_headerview = (TextView) vi.findViewById(R.id.section_headerview);
            holder.selected = (ImageView)vi.findViewById(R.id.selected);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        holder.title.setText(song_array.get(arg0).getTitle());
        if(selected_song_array.get(song_array.get(arg0).getId())!=null){
            holder.selected.setVisibility(View.VISIBLE);
        }else{
            holder.selected.setVisibility(View.GONE);
        }
        switch (rowStates[arg0]){
            case SECTION:
                showSeparator = true;
                break;
            case NORMAL:
                showSeparator = false;
                break;
            default:
                if (arg0 == 0) {
                    showSeparator = true;
                    rowStates[arg0] = SECTION;
                }else {
                    char previousName = Character.toLowerCase(song_array.get(arg0 - 1).getTitle().toCharArray()[0] );
                    char currentName = Character.toLowerCase(song_array.get(arg0).getTitle().toCharArray()[0] );
                    if (previousName != currentName) {
                        showSeparator = true;
                        rowStates[arg0] = SECTION;
                    } else {
                        showSeparator = false;
                        rowStates[arg0] = NORMAL;
                    }
                }
                break;
        }
        if (showSeparator) {
            holder.section_headerview.setText( Character.toUpperCase(song_array.get(arg0).getTitle().toCharArray()[0]) +"" );
            holder.section_headerview.setVisibility(View.VISIBLE);
        }
        else {
            holder.section_headerview.setVisibility(View.GONE);
        }

        return vi;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ViewHolder holder = (ViewHolder)view.getTag();
        if(selected_song_array.get(song_array.get(position).getId())!=null){
            holder.selected.setVisibility(View.GONE);
            selected_song_array.remove(song_array.get(position).getId());
        }else{
            holder.selected.setVisibility(View.VISIBLE);
            selected_song_array.put(song_array.get(position).getId(), song_array.get(position));
        }
    }

    public Map<String, SongInfo> getSelected_song_array() {
        return selected_song_array;
    }

}
