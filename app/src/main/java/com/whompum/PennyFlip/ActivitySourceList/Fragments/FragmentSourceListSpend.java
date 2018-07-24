package com.whompum.PennyFlip.ActivitySourceList.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;

import com.whompum.PennyFlip.ActivitySourceData.ActivitySourceDataSpent;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListAdapterBase;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceSpendListAdapter;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Money.Sources.SourceMetaData;
import com.whompum.PennyFlip.Money.Transactions.Models.TransactionType;

/**
 * Created by bryan on 12/27/2017.
 */

public class FragmentSourceListSpend extends FragmentSourceList {

    public static final int LOADER_ID = 2000;
    public static final int SOURCE_TYPE = TransactionType.SPEND;

    {
        loaderId = LOADER_ID;
        sourceType = SOURCE_TYPE;
        newSourceDialogImage = R.drawable.ic_source_minus;
    }


    public static FragmentSourceList newInstance(@Nullable final Bundle args) {
        FragmentSourceList fragment = new FragmentSourceListSpend();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected SourceListAdapterBase manifestAdapter() {
        return new SourceSpendListAdapter(getContext());
    }

    @Override
    protected void createIntent(@NonNull SourceMetaData data) {
        super.intent = new Intent(getActivity(), ActivitySourceDataSpent.class);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Do nothing yet
    }
}
