package com.mediaplayer.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.mediaplayer.adapter.GridAdapter;
import com.mediaplayer.com.MetaInfo;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.manager.BroadcastManager;
import com.mediaplayer.utility.AlbumArtLoader;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by shrikanth on 10/2/15.
 */
public abstract class MultiviewFragment extends MediaFragment implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener{
    GridAdapter adapter;
    GridView gridview;
    SongInfoDatabase database;
    ArrayList<MetaInfo> list;
    SearchView searchView;
    AlbumArtLoader.Mode mode;
    Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.multiview_layout_fragment,container,false);
        gridview = (GridView)v.findViewById(R.id.gridview);
        adapter = new GridAdapter(getActivity(), list, gridview,mode,this);
        gridview.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity  =getActivity();
        setData();
    }

    public abstract void setData();
    public abstract ArrayList<SongInfo> getToBePlayedData(MetaInfo info);

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchSongs(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        searchSongs(s);
        return false;
    }
    public abstract void searchSongs(String search);

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MetaInfo info  =list.get(i);
        LinkedList<SongInfo> serailaLisedArray = new LinkedList<SongInfo>(getToBePlayedData(info));
        Intent playSong = new Intent(BroadcastManager.APPEND_LIST);
        Bundle b= new Bundle();
        b.putSerializable(BroadcastManager.LIST_KEY, serailaLisedArray);
        playSong.putExtras(b);
        LocalBroadcastManager.getInstance(activity).sendBroadcast(playSong);
        Toast.makeText(activity, "Added to Queue", Toast.LENGTH_LONG).show();
    }

    public void showMore(View view, final int position){
        View popupView = getActivity().getLayoutInflater().inflate(R.layout.moremenu, null);
        final PopupWindow popup = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setWindowLayoutMode(0, 0);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        TextView play = (TextView)popupView.findViewById(R.id.popup_play);
        TextView append = (TextView)popupView.findViewById(R.id.popup_append);
        MetaInfo info  =list.get(position);
        final LinkedList<SongInfo> serailaLisedArray = new LinkedList<SongInfo>(getToBePlayedData(info));
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playSong = new Intent(BroadcastManager.PLAY_SELECTED);
                Bundle b= new Bundle();
                b.putSerializable(BroadcastManager.SONG_KEY,serailaLisedArray.get(0));
                b.putSerializable(BroadcastManager.LIST_KEY, serailaLisedArray);
                playSong.putExtras(b);
                LocalBroadcastManager.getInstance(activity).sendBroadcast(playSong);
                Toast.makeText(activity, "Playing Selected", Toast.LENGTH_LONG).show();
                popup.dismiss();
            }
        });
        append.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playSong = new Intent(BroadcastManager.APPEND_LIST);
                Bundle b= new Bundle();
                b.putSerializable(BroadcastManager.LIST_KEY, serailaLisedArray);
                playSong.putExtras(b);
                LocalBroadcastManager.getInstance(activity).sendBroadcast(playSong);
                Toast.makeText(activity, "Added to Queue", Toast.LENGTH_LONG).show();
                popup.dismiss();
            }
        });
        popup.setOutsideTouchable(true);
        popup.showAsDropDown(view);
        popup.update();
    }
}
