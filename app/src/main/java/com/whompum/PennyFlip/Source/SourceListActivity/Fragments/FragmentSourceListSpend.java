package com.whompum.PennyFlip.Source.SourceListActivity.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Source.SourceListActivity.Adapter.SourceListAdapterBase;
import com.whompum.PennyFlip.Source.SourceListActivity.Adapter.SourceSpendListAdapter;
import com.whompum.PennyFlip.Source.SourceListActivity.Models.SourceMetaData;
import com.whompum.PennyFlip.Source.SourceListActivity.Models.SpendSourceMetaData;
import com.whompum.PennyFlip.Time.Timestamp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryan on 12/27/2017.
 */

public class FragmentSourceListSpend extends FragmentSourceList {


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
    protected void populate(CharSequence query) {

        final List<SourceMetaData> data = new ArrayList<>();

        data.add(new SpendSourceMetaData( 0,"CAR WASH", 27822, Timestamp.now()));
        data.add(new SpendSourceMetaData( SpendSourceMetaData.NO_PURSE,"CAR WASH", 27822, Timestamp.now()));
        data.add(new SpendSourceMetaData( 90,"CAR WASH", 27822, Timestamp.now()));
        data.add(new SpendSourceMetaData( 50,"CAR WASH", 27822, Timestamp.now()));
        data.add(new SpendSourceMetaData( SpendSourceMetaData.NO_PURSE,"CAR WASH", 27822, Timestamp.now()));
        data.add(new SpendSourceMetaData( 66,"CAR WASH", 27822, Timestamp.now()));
        data.add(new SpendSourceMetaData( SpendSourceMetaData.NO_PURSE,"CAR WASH", 27822, Timestamp.now()));
        data.add(new SpendSourceMetaData( 99,"CAR WASH", 27822, Timestamp.now()));
        data.add(new SpendSourceMetaData( 10,"CAR WASH", 27822, Timestamp.now()));
        data.add(new SpendSourceMetaData( 100,"CAR WASH", 27822, Timestamp.now()));
        data.add(new SpendSourceMetaData( SpendSourceMetaData.NO_PURSE,"CAR WASH", 27822, Timestamp.now()));

    swapDataSet(data);
    }

    @Override
    protected void filter(Object filter) {

    }



}
