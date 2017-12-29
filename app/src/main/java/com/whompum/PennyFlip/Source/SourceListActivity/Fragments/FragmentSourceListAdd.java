package com.whompum.PennyFlip.Source.SourceListActivity.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Source.SourceListActivity.Adapter.SourceAddListAdapter;
import com.whompum.PennyFlip.Source.SourceListActivity.Adapter.SourceListAdapterBase;
import com.whompum.PennyFlip.Source.SourceListActivity.Models.SourceMetaData;
import com.whompum.PennyFlip.Time.Timestamp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryan on 12/27/2017.
 */

public class FragmentSourceListAdd extends FragmentSourceList {


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
    protected void populate(CharSequence query) {

        final List<SourceMetaData> data = new ArrayList<>();

        long timeAdjust = 15551;

        final long now = System.currentTimeMillis();

        for(int i =0; i < 20; i++){

            data.add(new SourceMetaData("CAR WASH", 27822, Timestamp.from(now-timeAdjust)));

            timeAdjust-=timeAdjust;

        }
        swapDataSet(data);
    }

    @Override
    protected void filter(Object filter) {

    }
}
