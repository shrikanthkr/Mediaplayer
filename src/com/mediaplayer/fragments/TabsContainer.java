package com.mediaplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mediaplayer.adapter.BaseViewPagerAdapter;
import com.mediaplayer.com.R;
import com.mediaplayer.customviews.BaseViewPager;
import com.mediaplayer.interfaces.TabsContainerHelpers;

/**
 * Created by shrikanth on 1/21/17.
 */

public abstract class TabsContainer extends MediaFragment  implements TabsContainerHelpers{
    protected BaseViewPager viewPager;
    protected BaseViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.base_tabs_container_fragment, container, false);
        viewPager = (BaseViewPager)v.findViewById(R.id.base_view_pager);
        adapter = new BaseViewPagerAdapter(getChildFragmentManager(), this);
        viewPager.setAdapter(adapter);
        return v;
    }
}
