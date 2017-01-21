package com.mediaplayer.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mediaplayer.fragments.MediaFragment;

/**
 * Created by shrikanth on 1/21/17.
 */

public class BaseViewPagerAdapter extends FragmentStatePagerAdapter {

    public BaseViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public MediaFragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    public interface ViewPageAdapterHelper{

    }
}
