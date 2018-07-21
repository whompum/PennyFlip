package com.whompum.PennyFlip.ActivityDashboard;

import android.os.Bundle;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transaction.Models.TransactionType;

/**
 * Created by bryan on 1/12/2018.
 */

public class TodaySpendFragment extends TodayFragment {


    {
        super.VALUE_TEXT_COLOR = R.color.light_red;
        super.sourceType = TransactionType.SPEND;
    }


    public static final int SPEND_LOADER_ID = 16;

    public static TodayFragment newInstance(final Bundle args){
        final TodaySpendFragment fragment = new TodaySpendFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    protected int getLoaderId() {
        return SPEND_LOADER_ID;
    }


}
