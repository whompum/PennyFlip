package com.whompum.PennyFlip.ActivitySourceList.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.ActivitySourceData.ActivitySourceDataAdded;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceAddListAdapter;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListAdapterBase;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Sources.SourceMetaData;
import com.whompum.PennyFlip.Transactions.Models.TransactionType;

/**
 * Created by bryan on 12/27/2017.
 */

public class FragmentSourceListAdd extends FragmentSourceList {


    public static final int LOADER_ID = 1000;

    public static final int SOURCE_TYPE = TransactionType.ADD;


    {
        loaderId = LOADER_ID;
        sourceType = SOURCE_TYPE;
        newSourceDialogImage = R.drawable.ic_source_plus;
    }

    public static FragmentSourceList newInstance(@Nullable final Bundle args) {

        FragmentSourceList fragment = new FragmentSourceListAdd();
        fragment.setArguments(args);
        return fragment;
    }



    @Override //RESPONSIBLE ONLY TO RETURN ITS TYPE OF ADAPTER, THAT IS IT;
    protected SourceListAdapterBase manifestAdapter() {
        return new SourceAddListAdapter(getContext());
    }

    @Override
    protected void createIntent(@NonNull SourceMetaData data) {
        this.intent = new Intent(getActivity(), ActivitySourceDataAdded.class);
    }


}
