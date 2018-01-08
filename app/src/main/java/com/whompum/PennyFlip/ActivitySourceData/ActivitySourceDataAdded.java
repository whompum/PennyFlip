package com.whompum.PennyFlip.ActivitySourceData;



import com.whompum.PennyFlip.ListPopulator;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.Adapters.SourceFragmentAdapter;
import com.whompum.PennyFlip.Statistics.Populator;
import com.whompum.PennyFlip.Statistics.SourceStatistic;
import com.whompum.PennyFlip.Statistics.StatisticsFragment;
import com.whompum.PennyFlip.Statistics.TimeRange;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Transaction.Models.HeaderItem;
import com.whompum.PennyFlip.Transaction.Models.TransactionHeaderItem;
import com.whompum.PennyFlip.Transaction.Models.TransactionsItem;
import com.whompum.PennyFlip.Transaction.TransactionFragment;
import com.whompum.PennyFlip.Transaction.Models.Transactions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActivitySourceDataAdded extends ActivitySourceData implements StatisticsFragment.StatisticsServer<TimeRange>{


    private ListPopulator<HeaderItem> transactionFragment;

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
        transactionFragment.populate(temp());
        statisticsFragement.populate(new SourceStatistic( 19990));
    }


    List<HeaderItem> temp(){

        final List<HeaderItem> transactions = new ArrayList<>();

        final long day = TimeUnit.DAYS.toMillis(1);

        final long today = System.currentTimeMillis();

        long transactionDate = today;

        for(int i = 0; i < 7; i++){

            final TransactionHeaderItem headerItem = new TransactionHeaderItem(Timestamp.from(transactionDate), 4);

            transactions.add(headerItem);

            for(int a = 0; a < 4; a++){

                Transactions trans = null;

                if(a % 2 == 0)
                    trans = new Transactions(Transactions.ADD, 2782L, 2113L, "Car Wash");
                else
                    trans = new Transactions(Transactions.SPEND, 9999L, 5556L, "Food");


                final TransactionsItem transItem = new TransactionsItem(trans);
                transactions.add(transItem);
            }
            transactionDate -= day;
        }

        return transactions;
    }



    @Override
    public void onDataRequested(TimeRange o) {
        //Requests data from server, or database for the specified time range
    }


}
