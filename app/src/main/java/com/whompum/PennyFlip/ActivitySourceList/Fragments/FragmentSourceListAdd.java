package com.whompum.PennyFlip.ActivitySourceList.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.ActivitySourceData.ActivitySourceDataAdded;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transactions.Models.TransactionType;

/**
 * Created by bryan on 12/27/2017.
 */

public class FragmentSourceListAdd extends FragmentSourceList {


    {
        transactionType = TransactionType.ADD;
        highlight = R.color.light_green;
    }

    public static FragmentSourceList newInstance(@Nullable final Bundle args) {

        FragmentSourceList fragment = new FragmentSourceListAdd();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void createIntent(@NonNull Source data) {
        this.intent = new Intent(getActivity(), ActivitySourceDataAdded.class);
    }

}
