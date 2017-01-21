package com.mediaplayer.interfaces;

import com.mediaplayer.fragments.BaseFragment;

/**
 * Created by shrikanth on 1/21/17.
 */

public interface TabsContainerHelpers {
    public String getTabTitle(int position);
    public BaseFragment getTabFragment(int position);
    public int getCount();
}
