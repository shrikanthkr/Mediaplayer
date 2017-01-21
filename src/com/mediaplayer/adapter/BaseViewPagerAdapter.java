package com.mediaplayer.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mediaplayer.fragments.MediaFragment;
import com.mediaplayer.interfaces.TabsContainerHelpers;

/**
 * Created by shrikanth on 1/21/17.
 */

public class BaseViewPagerAdapter extends FragmentStatePagerAdapter {

    TabsContainerHelpers helper;
    public BaseViewPagerAdapter(FragmentManager fm, TabsContainerHelpers helper) {
        super(fm);
        this.helper = helper;
    }

    @Override
    public MediaFragment getItem(int position) {
        return helper.getTabFragment(position);
    }

    @Override
    public int getCount() {
        return helper.getCount();
    }

}
