package com.whompum.PennyFlip.ActivitySourceData;


import android.content.Intent;

import com.whompum.PennyFlip.ListPopulator;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.Adapters.SourceFragmentAdapter;
import com.whompum.PennyFlip.Statistics.Populator;
import com.whompum.PennyFlip.Statistics.SourceStatistic;
import com.whompum.PennyFlip.Statistics.StatisticsFragment;
import com.whompum.PennyFlip.Transactions.Models.HeaderItem;
import com.whompum.PennyFlip.Transactions.TransactionFragment;


public class ActivitySourceDataSpent extends ActivitySourceData {


    private ListPopulator<HeaderItem> transactionFragment;
    private Populator<SourceStatistic> sourceStatisticPopulator;

    @Override
    protected void populateFragmentAdapter(SourceFragmentAdapter adapter) {

        final TransactionFragment transFragment = (TransactionFragment) TransactionFragment.newInstance();
        this.transactionFragment = transFragment;

        final StatisticsFragment statsFragment = (StatisticsFragment) StatisticsFragment.newInstance(data.getPennies());
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
    protected Source initMetaData(final Intent intent) {
        return (Source) intent.getSerializableExtra(DATA);
    }

}
