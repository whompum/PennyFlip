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

public class ActivitySourceDataAdded extends ActivitySourceData implements StatisticsFragment.StatisticsServer<TimeRange>{


    private ListPopulator<Transactions> transactionFragment;

    private Populator<SourceStatistic> statisticsFragement;

    @Override
    protected void populateFragmentAdapter(SourceFragmentAdapter adapter) {

        final TransactionFragment transFragment = (TransactionFragment) TransactionFragment.newInstance();
        transactionFragment = transFragment;
        adapter.addFragment(transFragment);

        final StatisticsFragment statsFrag = (StatisticsFragment) StatisticsFragment.newInstance(sourcePennies);
        adapter.addFragment(statsFrag);
        statisticsFragement = statsFrag;

        bindStrip(getString(R.string.string_transaction));
        bindStrip(getString(R.string.string_statistics));

    }

    @Override
    protected void populateFragments() {
        final List<Transactions> data = new ArrayList<>();

        final long currTime = System.currentTimeMillis();


        int day = 0;

        for(int i =0; i < 10; i ++){
            data.add(new Transactions(Transactions.ADD ,currTime - TimeUnit.DAYS.toMillis(day),2782, 5997, "Food"));

            day += 64;
        }

        transactionFragment.populate(data);
        statisticsFragement.populate(new SourceStatistic( 19990));
    }

    @Override
    public void onDataRequested(TimeRange o) {
        //Requests data from server, or database for the specified time range
    }


}
