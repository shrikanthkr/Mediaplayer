package com.mediaplayer.interfaces;

import com.mediaplayer.fragments.MediaFragment;

/**
 * Created by shrikanth on 1/21/17.
 */

public interface TabsContainerHelpers {
    public String getTabTitle(int position);
    public MediaFragment getTabFragment(int position);
    public int getCount();
}
