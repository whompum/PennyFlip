package com.whompum.PennyFlip.ActivitySourceList.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.ActivitySourceData.ActivitySourceDataSpent;
import com.whompum.PennyFlip.Money.Source.Source;
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


    @Override
    protected void createIntent(@NonNull Source data) {
        super.intent = new Intent(getActivity(), ActivitySourceDataSpent.class);
    }

}
