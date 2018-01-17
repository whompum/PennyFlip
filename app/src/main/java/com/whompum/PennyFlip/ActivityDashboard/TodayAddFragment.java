package com.whompum.PennyFlip.ActivityDashboard;

import android.os.Bundle;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transaction.Models.Transactions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryan on 1/12/2018.
 */

public class TodayAddFragment extends TodayFragment {

    {
        VALUE_TEXT_COLOR = R.color.light_green;
    }

    public static TodayFragment newInstance(final Bundle args){
        final TodayAddFragment fragment = new TodayAddFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected void populate() {

        final List<Transactions> tempData = new ArrayList<>();

        tempData.add(new Transactions(Transactions.ADD, 2782, 0000, "Car Was" ));
        tempData.add(new Transactions(Transactions.ADD, 2782, 0000, "Car Was" ));
        tempData.add(new Transactions(Transactions.ADD, 2782, 0000, "Car Was" ));


        setTransactions(tempData);
    }

    @Override
    protected void populateValue() {
        setValue(500);
    }
}
