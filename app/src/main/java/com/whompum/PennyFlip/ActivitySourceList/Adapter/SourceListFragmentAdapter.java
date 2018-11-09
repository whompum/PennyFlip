package com.whompum.PennyFlip.ActivitySourceList.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.whompum.PennyFlip.ActivitySourceList.Fragments.FragmentSourceList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bryan on 12/27/2017.
 */

public class SourceListFragmentAdapter extends FragmentPagerAdapter {

    private List<FragmentSourceList> fragments = null;

    public SourceListFragmentAdapter(final FragmentManager fragmentManager,
                                     @NonNull final FragmentSourceList... fragments){
        super(fragmentManager);

        this.fragments = Arrays.asList( fragments );

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        FragmentSourceList fragment = (FragmentSourceList)
                super.instantiateItem(container, position);

        fragments.set( position, fragment );

        return fragment;
    }

    @Override
    public Fragment getItem(int position) {

        if( fragments == null || position < 0 || position > fragments.size()-1 ) return null;

        return fragments.get( position );
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        fragments.remove( position );
    }

    @Override
    public int getCount() {
        if( fragments == null )
            return 0;

        return fragments.size();
    }
}
