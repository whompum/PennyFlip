package com.whompum.PennyFlip.ActivityDashboard;

import android.os.Bundle;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transactions.Models.TransactionType;

/**
 * Created by bryan on 1/12/2018.
 */

public class TodayAddFragment extends TodayFragment {


    {
        super.VALUE_TEXT_COLOR = R.color.light_green;
        super.transactionType = TransactionType.ADD;
    }

    public static final int ADD_LOADER_ID = 15;


    public static TodayFragment newInstance(final Bundle args){
        final TodayAddFragment fragment = new TodayAddFragment();
        fragment.setArguments(args);

        return fragment;
    }

}
