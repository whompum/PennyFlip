package com.whompum.PennyFlip.ActivitySourceList.Adapter;

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


    private final FragmentSourceList addingFragment = FragmentSourceListAdd.newInstance(null);
    private final FragmentSourceList spendingFragment = FragmentSourceListSpend.newInstance(null);

    public SourceListFragmentAdapter(final FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return (position==0) ? addingFragment : spendingFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}