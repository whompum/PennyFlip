package com.whompum.PennyFlip.Dashboard.Fragments.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.whompum.PennyFlip.Dashboard.Fragments.TodayFragment;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;

public class TodayFragmentAdapter extends FragmentPagerAdapter {

    private TodayFragment addFragment = TodayFragment.newInstance( TransactionType.INCOME);
    private TodayFragment spendFragment = TodayFragment.newInstance( TransactionType.EXPENSE);



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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TodayFragment fragment = (TodayFragment)
                super.instantiateItem(container, position);

        if( position == 0 )
            addFragment = fragment;

        else if( position == 1 )
            spendFragment = fragment;

        return fragment;
    }

    public  TodayFragment getAddFragment(){
        return addFragment;
    }

    public TodayFragment getSpendFragment() {
        return spendFragment;
    }
}
