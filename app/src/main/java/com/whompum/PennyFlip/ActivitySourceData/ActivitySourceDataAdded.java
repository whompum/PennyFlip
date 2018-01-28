package com.whompum.PennyFlip.ActivitySourceData;



import android.content.ContentValues;
import android.content.Intent;

import com.whompum.PennyFlip.Data.Schemas.TransactionsSchema;
import com.whompum.PennyFlip.ListPopulator;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.Adapters.SourceFragmentAdapter;
import com.whompum.PennyFlip.Source.SourceMetaData;
import com.whompum.PennyFlip.Statistics.Populator;
import com.whompum.PennyFlip.Source.SourceStatistic;
import com.whompum.PennyFlip.Statistics.StatisticsFragment;
import com.whompum.PennyFlip.Time.TimeRange;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Transaction.Models.HeaderItem;
import com.whompum.PennyFlip.Transaction.Models.TransactionType;
import com.whompum.PennyFlip.Transaction.TransactionFragment;

public class ActivitySourceDataAdded extends ActivitySourceData implements StatisticsFragment.StatisticsServer<TimeRange>{


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
    protected SourceMetaData initMetaData(final Intent intent) {
        return (SourceMetaData) intent.getParcelableExtra(SOURCE_KEY);
    }

    @Override
    public void onDataRequested(TimeRange o) {
        //Requests data from server, or database for the specified time range
    }

  

}
