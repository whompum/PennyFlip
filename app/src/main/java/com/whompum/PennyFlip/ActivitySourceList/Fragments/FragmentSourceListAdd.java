package com.whompum.PennyFlip.ActivitySourceList.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.Toast;

import com.whompum.PennyFlip.ActivitySourceData.ActivitySourceDataAdded;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceAddListAdapter;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListAdapterBase;
import com.whompum.PennyFlip.Data.Schemas.SourceSchema;
import com.whompum.PennyFlip.Source.SourceCursorAdapter;
import com.whompum.PennyFlip.Source.SourceMetaData;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Transaction.Models.TransactionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryan on 12/27/2017.
 */

public class FragmentSourceListAdd extends FragmentSourceList {


    public static final int LOADER_ID = 1000;

    public static final int SOURCE_TYPE = TransactionType.ADD;


    {
        loaderId = LOADER_ID;
        sourceType = SOURCE_TYPE;
    }

    public static FragmentSourceList newInstance(@Nullable final Bundle args) {

        FragmentSourceList fragment = new FragmentSourceListAdd();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected SourceCursorAdapter manifestCursorAdapter() {
        return new SourceCursorAdapter(null);
    }

    @Override //RESPONSIBLE ONLY TO RETURN ITS TYPE OF ADAPTER, THAT IS IT;
    protected SourceListAdapterBase manifestAdapter() {
        return new SourceAddListAdapter(getContext());
    }

    @Override
    protected void createIntent(@NonNull SourceMetaData data) {
        this.intent = new Intent(getActivity(), ActivitySourceDataAdded.class);
    }



    @Override
    protected void filter(Object filter) {

    }
}
