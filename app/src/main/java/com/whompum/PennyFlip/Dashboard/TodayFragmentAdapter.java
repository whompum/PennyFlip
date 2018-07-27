package com.whompum.PennyFlip.Dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by bryan on 1/12/2018.
 */

public class TodayFragmentAdapter extends FragmentPagerAdapter {

    private static final TodayFragment addFragment = TodayAddFragment.newInstance(null);
    private static final TodayFragment spendFragment = TodaySpendFragment.newInstance(null);


    public TodayFragmentAdapter(final FragmentManager fragMan){
        super(fragMan);
    }

    @Override
    public Fragment getItem(int position) {
        return (position == 0) ? addFragment : spendFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
