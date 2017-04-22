package com.mediaplayer.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mediaplayer.adapter.RadioRecyclerAdapter;
import com.mediaplayer.app.R;
import com.mediaplayer.customviews.RadioRecyclerView;
import com.mediaplayer.interfaces.RecyclerClickHelper;
import com.mediaplayer.manager.StreamManager;
import com.mediaplayer.models.Radio;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shrikanth on 10/2/15.
 */
public class RadioFragment2 extends BaseFragment{
    private static final String TAG = "RADIO FRAGMENT";

    List<Radio> stations;
    RadioRecyclerView radioStationsView;
    RadioRecyclerAdapter adapter;
    LinearLayoutManager layoutManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stations = new ArrayList<>();
        adapter = new RadioRecyclerAdapter(new RecyclerClickHelper() {
            @Override
            public void onItemClickListener(View view, int position) {
                final Radio r = stations.get(position);
                showAlertDialog(r.streamTitle, "Do you want Join?", "Yes", "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                receive(r.streamUrl);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dismissAlertDialog();
                                break;
                        }
                    }
                });
            }
        });
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.radio_fragment,container,false);

        radioStationsView = (RadioRecyclerView)v.findViewById(R.id.radio_stations);
        radioStationsView.setLayoutManager(layoutManager);
        radioStationsView.setAdapter(adapter);
        return v;

    }



    @Override
    public void setTitle() {
        getActivity().setTitle(getString(R.string.radio_stations));
    }

    private void receive(String url) {
        StreamManager.getInstance().startRemotePlay(url);
    }

}
