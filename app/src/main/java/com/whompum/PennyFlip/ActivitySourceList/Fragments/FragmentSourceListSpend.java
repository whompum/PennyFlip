package com.whompum.PennyFlip.ActivitySourceList.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transactions.Models.TransactionType;

/**
 * Created by bryan on 12/27/2017.
 */

public class FragmentSourceListSpend extends FragmentSourceList {


    {
        transactionType = TransactionType.SPEND;
        highlight = R.color.light_red;
    }

    public static FragmentSourceList newInstance(@Nullable final Bundle args) {
        FragmentSourceList fragment = new FragmentSourceListSpend();
        fragment.setArguments(args);
        return fragment;
    }

}
