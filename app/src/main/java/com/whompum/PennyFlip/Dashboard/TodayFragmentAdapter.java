package com.whompum.PennyFlip.Dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;

public class TodayFragmentAdapter extends FragmentPagerAdapter {

    private final TodayFragment addFragment = TodayFragment.newInstance( TransactionType.ADD );
    private final TodayFragment spendFragment = TodayFragment.newInstance( TransactionType.SPEND );


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

    public  TodayFragment getAddFragment(){
        return addFragment;
    }

    public TodayFragment getSpendFragment() {
        return spendFragment;
    }
}
