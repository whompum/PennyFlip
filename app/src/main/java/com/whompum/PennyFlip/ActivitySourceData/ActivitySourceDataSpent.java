package com.whompum.PennyFlip.ActivitySourceData;


import com.whompum.PennyFlip.ActivitySourceData.ActivitySourceData;
import com.whompum.PennyFlip.ListPopulator;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.Adapters.SourceFragmentAdapter;
import com.whompum.PennyFlip.Statistics.Populator;
import com.whompum.PennyFlip.Statistics.SourceStatistic;
import com.whompum.PennyFlip.Statistics.StatisticsFragment;
import com.whompum.PennyFlip.Statistics.TimeRange;
import com.whompum.PennyFlip.Transaction.TransactionFragment;
import com.whompum.PennyFlip.Transaction.Transactions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ActivitySourceDataSpent extends ActivitySourceData implements StatisticsFragment.StatisticsServer<TimeRange> {


    private ListPopulator<Transactions> transactionFragment;
    private Populator<SourceStatistic> sourceStatisticPopulator;

    @Override
    protected void populateFragmentAdapter(SourceFragmentAdapter adapter) {

        final TransactionFragment transFragment = (TransactionFragment) TransactionFragment.newInstance();
        this.transactionFragment = transFragment;

        final StatisticsFragment statsFragment = (StatisticsFragment) StatisticsFragment.newInstance(sourcePennies, this);
        this.sourceStatisticPopulator = statsFragment;


        bindStrip(getString(R.string.string_transaction));
        bindStrip(getString(R.string.string_statistics));

        adapter.addFragment(transFragment);
        adapter.addFragment(statsFragment);

    }

    @Override
    protected void populateFragments() {

        final List<Transactions> data = new ArrayList<>();

        final long currTime = System.currentTimeMillis();


        int day = 0;

        for(int i =0; i < 10; i ++){
            data.add(new Transactions(currTime - TimeUnit.DAYS.toMillis(day),2782, 5997, "Food"));

            day += 64;
        }


        transactionFragment.populate(data);
        sourceStatisticPopulator.populate(new SourceStatistic( 30000));

    }

    @Override
    public void onDataRequested(TimeRange o) {

    }


}
