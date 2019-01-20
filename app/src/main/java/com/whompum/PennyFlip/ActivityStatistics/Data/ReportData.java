package com.whompum.PennyFlip.ActivityStatistics.Data;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Statistics.TransactionStatistics;
import com.whompum.PennyFlip.Money.Statistics.TransactionStatistics.TransactionMetData;

import java.util.List;

import static com.whompum.PennyFlip.Money.Transaction.TransactionType.INCOME;
import static com.whompum.PennyFlip.Money.Transaction.TransactionType.EXPENSE;

public class ReportData {

    @IntRange(from = INCOME, to = EXPENSE)
    private int transactionType;

    private long netAmount;

    private long numTransactions;

    private long transactionAverage;

    private double transactionRatio = 0D;

    private String lastTransactiontitle;

    private String lastTransactionSourceTitle;

    private long lastTransactionAmount;

    private long dailyAverage;

    private List<Source> topSources;

    private SourceObserver sourceObserver;

    private boolean hasRecentTransaction = false;

    public ReportData(@IntRange(from = INCOME, to = EXPENSE) final int type){
        this.transactionType = type;
    }

    public ReportData(){

    }

    public void bindData(@NonNull final TransactionStatistics statistics, final int activeDays){
        transactionType = statistics.getTransactionType();
        netAmount = statistics.getNetAmount();
        numTransactions = statistics.getNumTransactions();
        transactionAverage = statistics.getTransactionAverage();
        transactionRatio = statistics.getTransactionRatio();

        final TransactionMetData transData = statistics.getTransaction();

        if( transData != null ){
            hasRecentTransaction = true;
            lastTransactionSourceTitle = transData.getSourceTitle();
            lastTransactiontitle = transData.getTitle();
            lastTransactionAmount = transData.getAmount();
        }


        setActiveUserDays( activeDays );
    }

    public void setActiveUserDays(int activeDays){
        if( activeDays == 0 )
            activeDays = 1;

        this.dailyAverage = netAmount / activeDays;
    }

    public void setTopSources(@Nullable final List<Source> topSources){
        this.topSources = topSources;
        if( sourceObserver != null )
            sourceObserver.onSourceSet( topSources );
    }


    @IntRange(from = INCOME, to = EXPENSE)
    public int getTransactionType(){
        return transactionType;
    }

    public List<Source> getTopSources() {
        return topSources;
    }

    public long getNetAmount() {
        return netAmount;
    }

    public long getNumTransactions() {
        return numTransactions;
    }

    public long getTransactionAverage() {
        return transactionAverage;
    }

    public double getTransactionRatio() {
        return transactionRatio;
    }

    @Nullable
    public String getLastTransactiontitle() {
        return lastTransactiontitle;
    }

    @Nullable
    public String getLastTransactionSourceTitle() {
        return lastTransactionSourceTitle;
    }

    public long getLastTransactionAmount() {
        return lastTransactionAmount;
    }

    public long getDailyAverage() {
        return dailyAverage;
    }

    public boolean hasRecentTransaction() {
        return hasRecentTransaction;
    }

    public SourceObserver getSourceObserver() {
        return sourceObserver;
    }

    public void setSourceObserver(@NonNull final SourceObserver sourceObserver){
        this.sourceObserver = sourceObserver;
    }

    public void removeSourceObserver(){
        this.sourceObserver = null;
    }

    public interface SourceObserver{
        void onSourceSet(@Nullable final List<Source> sources);
    }

}
