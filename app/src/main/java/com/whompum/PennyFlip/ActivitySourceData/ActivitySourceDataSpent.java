package com.whompum.PennyFlip.ActivitySourceData;


import android.content.Intent;
import android.os.Bundle;

import com.whompum.PennyFlip.ListPopulator;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.Adapters.SourceFragmentAdapter;
import com.whompum.PennyFlip.Source.SourceMetaData;
import com.whompum.PennyFlip.Source.SpendSourceMetaData;
import com.whompum.PennyFlip.Statistics.Populator;
import com.whompum.PennyFlip.Source.SourceStatistic;
import com.whompum.PennyFlip.Statistics.StatisticsFragment;
import com.whompum.PennyFlip.Time.TimeRange;
import com.whompum.PennyFlip.Transaction.Models.HeaderItem;
import com.whompum.PennyFlip.Transaction.TransactionFragment;


public class ActivitySourceDataSpent extends ActivitySourceData implements StatisticsFragment.StatisticsServer<TimeRange> {


    private ListPopulator<HeaderItem> transactionFragment;
    private Populator<SourceStatistic> sourceStatisticPopulator;

    @Override
    protected void populateFragmentAdapter(SourceFragmentAdapter adapter) {

        final TransactionFragment transFragment = (TransactionFragment) TransactionFragment.newInstance();
        this.transactionFragment = transFragment;

        final StatisticsFragment statsFragment = (StatisticsFragment) StatisticsFragment.newInstance(data.getPennies(), this);
        this.sourceStatisticPopulator = statsFragment;


        bindStrip(getString(R.string.string_transaction));
        bindStrip(getString(R.string.string_statistics));

        adapter.addFragment(transFragment);
        adapter.addFragment(statsFragment);

    }

    @Override
    protected void populateFragments() {
    }

    @Override
    protected SourceMetaData initMetaData(final Intent intent) {
        //Implement Serizalizable on Spend
        return (SpendSourceMetaData) intent.getParcelableExtra(SOURCE_KEY);
    }

    @Override
    public void onDataRequested(TimeRange o) {

    }


}
