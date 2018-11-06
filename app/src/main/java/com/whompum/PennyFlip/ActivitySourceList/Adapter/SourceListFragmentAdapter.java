package com.whompum.PennyFlip.ActivitySourceList.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.whompum.PennyFlip.ActivitySourceList.Fragments.FragmentSourceList;
import com.whompum.PennyFlip.ActivitySourceList.Fragments.FragmentSourceListAdd;
import com.whompum.PennyFlip.ActivitySourceList.Fragments.FragmentSourceListSpend;

/**
 * Created by bryan on 12/27/2017.
 */

public class SourceListFragmentAdapter extends FragmentPagerAdapter {

    private FragmentSourceList[] fragments = null;

    public SourceListFragmentAdapter(final FragmentManager fragmentManager,
                                     @NonNull final FragmentSourceList... fragments){
        super(fragmentManager);

        this.fragments = fragments;

    }

    @Override
    public Fragment getItem(int position) {

        if( fragments == null || position > 0 || position < fragments.length-1 ) return null;

        return fragments[ position ];
    }

    @Override
    public int getCount() {
        if( fragments == null )
            return 0;

        return fragments.length;
    }
}
