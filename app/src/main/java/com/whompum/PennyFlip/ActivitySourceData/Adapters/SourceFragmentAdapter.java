package com.whompum.PennyFlip.ActivitySourceData.Adapters;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by bryan on 12/30/2017.
 */

public class SourceFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>(2);

    public SourceFragmentAdapter(FragmentManager fm) {
        this(fm, null);
    }

    public SourceFragmentAdapter(FragmentManager fm, @Nullable List<Fragment> data) {
        super(fm);
        if(data != null) this.fragmentList = data;
    }

    public void addFragment(final Fragment fragment){
        fragmentList.add(fragment);
    }

    public void addFragments(final List<Fragment> fragments){
        this.fragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
