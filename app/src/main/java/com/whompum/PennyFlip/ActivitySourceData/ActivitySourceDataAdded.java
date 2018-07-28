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

public class ActivitySourceDataAdded extends ActivitySourceData{


    private ListPopulator<HeaderItem> transactionFragment;
    private Populator<SourceStatistic> statisticsFragement;

    @Override
    protected void populateFragmentAdapter(SourceFragmentAdapter adapter) {

        final TransactionFragment transFragment = (TransactionFragment) TransactionFragment.newInstance();
        transactionFragment = transFragment;
        adapter.addFragment(transFragment);

        final StatisticsFragment statsFrag = (StatisticsFragment) StatisticsFragment.newInstance(data.getPennies());
        adapter.addFragment(statsFrag);
        statisticsFragement = statsFrag;

        bindStrip(getString(R.string.string_transaction));
        bindStrip(getString(R.string.string_statistics));
    }

    @Override
    protected void populateFragments() {
        //The fragments implement Populator, so call populate() on them
    }

    @Override
    protected Source initMetaData(final Intent intent) {
        return (Source) intent.getSerializableExtra(DATA);
    }

}
