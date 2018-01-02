package com.whompum.PennyFlip.ActivitySourceData.Adapters;


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
        super(fm);
    }

    public void addFragment(final Fragment fragment){
        fragmentList.add(fragment);
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
