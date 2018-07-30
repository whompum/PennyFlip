package com.whompum.PennyFlip.Dashboard;

import android.os.Bundle;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;

/**
 * Created by bryan on 1/12/2018.
 */

public class TodayAddFragment extends TodayFragment {


    {
        super.VALUE_TEXT_COLOR = R.color.light_green;
        super.transactionType = TransactionType.ADD;
    }

    public static TodayFragment newInstance(final Bundle args){
        final TodayAddFragment fragment = new TodayAddFragment();
        fragment.setArguments(args);

        return fragment;
    }

}
