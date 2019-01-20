package com.whompum.PennyFlip.ActivityStatistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import com.whompum.PennyFlip.ActivityStatistics.Data.ReportData;
import com.whompum.PennyFlip.ActivityStatistics.Fragment.ReportDataFragment;
import com.whompum.PennyFlip.ActivityStatistics.Fragment.StatisticsViewLayerContract;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Widgets.TransactionGraphContainer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.whompum.PennyFlip.Money.Transaction.TransactionType.INCOME;

public class ActivityStatistics extends AppCompatActivity implements StatisticsMvpContract.View,
        StatisticsViewLayerContract.Activity{

    @BindView(R.id.id_local_date_selector) public Spinner dateSelector;
    @BindView(R.id.id_stat_graph_container) public TransactionGraphContainer transactionGraph;

    private DateListAdapter dateListAdapter;

    private StatisticsMvpContract.Presenter presenter;

    private StatisticsViewLayerContract.Fragment reportDataFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.statistics );
        ButterKnife.bind( this );

        reportDataFragment = (StatisticsViewLayerContract.Fragment) getFragment();

        if( savedInstanceState == null )
            injectReportDataFragment();

        setSupportActionBar( findViewById( R.id.id_global_toolbar ) );

        if( getSupportActionBar() != null )
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        initializeDateSelector();

        presenter = new StatisticsPresenter( this, this );

        presenter.onStatisticsTypeSelected(INCOME);

    }

    @Override
    public void onTransactionTypeChange(int newType) {
        presenter.onStatisticsTypeSelected( newType ); //Default Query
    }

    @Override
    public void displayUserMonths(List<Long> monthDates) {
        dateListAdapter.swapDataSet( monthDates );
    }

    @Override
    public void displayTransactionsOverview(@NonNull List<Transaction> data) {
    }

    @Override
    public void displayReportData(@NonNull final ReportData data) {
        reportDataFragment.displayReportData( data );
    }

    @Override
    public void displayTopSources(@Nullable List<Source> sources, int transactionType) {
        reportDataFragment.displayTopSources( sources, transactionType );
    }

    private void initializeDateSelector(){
        dateListAdapter = new DateListAdapter( this, android.R.layout.simple_list_item_1 );

        dateSelector.setAdapter( dateListAdapter );

        dateSelector.setOnItemSelectedListener(new SimpleSpinnerItemSelectedListener() {
            @Override
            void onItemSelected(int pos) {

                long selectedMonth;
                if( (selectedMonth = dateListAdapter.getItem( pos )) != -1L )
                    presenter.onMonthSelected( selectedMonth );

            }
        });
    }

    private void injectReportDataFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .add( R.id.id_global_container, (Fragment)reportDataFragment, ReportDataFragment.TAG )
                .commit();
    }

    private Fragment getFragment(){

        if( getSupportFragmentManager().findFragmentByTag( ReportDataFragment.TAG ) != null )
            return getSupportFragmentManager().findFragmentByTag( ReportDataFragment.TAG );

        return new ReportDataFragment();
    }

}
