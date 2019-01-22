package com.whompum.PennyFlip.ActivitySourceList.Adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.whompum.PennyFlip.ActivitySourceList.Fragments.FragmentSourceList;
import com.whompum.PennyFlip.R;

public class SourceListFragmentAdapter extends FragmentPagerAdapter {

    private FragmentSourceList incomeSources;
    private FragmentSourceList expenseSources;

    public SourceListFragmentAdapter(@NonNull final FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        FragmentSourceList fragment = (FragmentSourceList)
                super.instantiateItem(container, position);

        if( position == 0 )
            incomeSources = fragment;
        else if( position == 1 )
            expenseSources = fragment;

        return fragment;
    }

    @Override
    public Fragment getItem(int position) {

        if( position == 0 )
            return FragmentSourceList.newInstance( R.layout.source_list_adding_null_data );
        else if( position == 1 )
            return FragmentSourceList.newInstance( R.layout.source_list_spending_null_data );

        return null;
    }

    @Nullable
    public FragmentSourceList getFragmentAtPosition(@Size(min = 0) final int position){

        if( position == 0 )
            return incomeSources;
        else if( position == 1 )
            return expenseSources;

        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        if( position == 0 )
            incomeSources = null;
        else if( position == 1 )
            expenseSources = null;
    }

    @Size(min = 2)
    @Override
    public int getCount() {
        return 2;
    }
}
