package com.whompum.PennyFlip.ActivityStatistics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.util.Log;
import android.util.SparseArray;

import com.whompum.PennyFlip.ActivityStatistics.Data.ReportData;
import com.whompum.PennyFlip.Money.DatabaseUtils;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Deliverable;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Query.StatisticQueries;
import com.whompum.PennyFlip.Money.Queries.SourceQueries;
import com.whompum.PennyFlip.Money.Queries.TransactionQueries;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Source.SourceQueryKeys;
import com.whompum.PennyFlip.Money.Statistics.StatisticQueryKeys;
import com.whompum.PennyFlip.Money.Statistics.TransactionStatistics;
import com.whompum.PennyFlip.Money.TimeRange;
import com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys;
import com.whompum.PennyFlip.Time.MonthlyTimestampResolver;
import com.whompum.PennyFlip.Time.UserStartDate;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Instant;
import org.joda.time.MutableDateTime;

import java.util.ArrayList;
import java.util.List;

import static com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys.TIMERANGE;

class StatisticsPresenter implements StatisticsMvpContract.Presenter{

    private SourceQueries sourceQueries = new SourceQueries();

    private MoneyDatabase database;
    private StatisticsMvpContract.View view;

    private long userStartDate;

    private SparseArray<ReportData> data = new SparseArray<>( 2 );

    StatisticsPresenter(@NonNull final Context context, @NonNull final StatisticsMvpContract.View view) {
        database = DatabaseUtils.getMoneyDatabase( context );
        this.view = view;

        this.userStartDate = UserStartDate.getUserStartDate( context );

        view.displayUserMonths(
                MonthlyTimestampResolver.fetchActiveUserMonths( userStartDate )
        );

    }

    @Override
    public void onMonthSelected(long monthMillis) {
        new TransactionQueries()
                .queryGroup( fetchMonthlyTransactionRequest( monthMillis ), database )
                .attachResponder( view::displayTransactionsOverview );
    }

    @Override
    public void onStatisticsTypeSelected(int transactionType){

        //Check if the data is available in the 'cache' e.g. SparseArray
        //If not, then query for it and deliver it to the view

        ReportData reportData;

        if( (reportData = data.get( transactionType )) == null ){

            //Default ReportData objects prevent multiple downloads of the same data
            data.put( transactionType, new ReportData( transactionType ) );
            queryStatistics( transactionType );

        }else {
            view.displayReportData( reportData );
            view.displayTopSources( reportData.getTopSources(), reportData.getTransactionType() );
        }
    }

    private ReportData bindNewReportData(final TransactionStatistics statistics){

        final ReportData reportData = new ReportData( statistics.getTransactionType() );

        reportData.bindData( statistics, getActiveUserDays() );

        return reportData;
    }

    private void onStatisticsDownloaded(@NonNull final TransactionStatistics statistics){
        final ReportData repoData = bindNewReportData( statistics );

        repoData.setSourceObserver( (s) -> {
            view.displayTopSources( s, statistics.getTransactionType() );
            repoData.removeSourceObserver();
        });

        this.data.put( statistics.getTransactionType(), repoData );

        view.displayReportData( repoData );
        loadTopSources( repoData );
    }

    private int getActiveUserDays(){
        return Days.daysBetween(
                new DateTime( userStartDate ).withTimeAtStartOfDay().toLocalDate(),
                new DateTime( System.currentTimeMillis() ).withTimeAtStartOfDay().toLocalDate()
        ).getDays();
    }

    private void loadTopSources(final ReportData data){
        sourceQueries
                .queryGroupWithOperation(
                        fetchSourcesRequest( data.getTransactionType() ),
                        database,
                        StatisticsPresenter.this::fetchTopSources
                )
                .attachResponder( data::setTopSources )
                .attachCancelledResponder( (reason, msg) -> data.setTopSources( new ArrayList<>() ) );
    }

    @NonNull
    @Size(min = 0, max = 5)
    private List<Source> fetchTopSources(@NonNull final List<Source> source){
        return source;
    }

    private Deliverable<TransactionStatistics> queryStatistics(final int type){
        return new StatisticQueries()
                .fetchById( fetchStatisticRequest( type ), database)
                .attachCancelledResponder( (reason, msg) -> view.displayReportData( data.get( type ) ) ) //Deliver default
                .attachResponder( StatisticsPresenter.this::onStatisticsDownloaded );
    }

    private MoneyRequest fetchMonthlyTransactionRequest(final long millis){
        return new MoneyRequest.QueryBuilder( TransactionQueryKeys.KEYS )
                .setQueryParameter( TIMERANGE, fetchMonthlyTimerange( millis ) )
                .getQuery();
    }

    private MoneyRequest fetchSourcesRequest(final int transactionType){
        return new MoneyRequest.QueryBuilder( SourceQueryKeys.KEYS )
                .setQueryParameter( SourceQueryKeys.TRANSACTION_TYPE, transactionType )
                .getQuery();
    }

    private MoneyRequest fetchStatisticRequest(final int type){
        return new MoneyRequest.QueryBuilder( StatisticQueryKeys.KEYS )
                .setQueryParameter( StatisticQueryKeys.TRANSACTION_TYPE, type )
                .getQuery();
    }

    private TimeRange fetchMonthlyTimerange(final long monthFloor){

        final MutableDateTime monthCiel = new MutableDateTime( monthFloor );
        monthCiel.addMonths( 1 );

        return new TimeRange( monthFloor, monthCiel.getMillis() );
    }

}



