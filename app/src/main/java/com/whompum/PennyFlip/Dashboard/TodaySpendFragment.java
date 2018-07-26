package com.whompum.PennyFlip.Dashboard;

import android.os.Bundle;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transactions.Models.TransactionType;

/**
 * Created by bryan on 1/12/2018.
 */

public class TodaySpendFragment extends TodayFragment {


    {
        super.VALUE_TEXT_COLOR = R.color.light_red;
        super.transactionType = TransactionType.SPEND;
    }


    public static TodayFragment newInstance(final Bundle args){
        final TodaySpendFragment fragment = new TodaySpendFragment();
        fragment.setArguments(args);

        return fragment;
    }

}
