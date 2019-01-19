package com.whompum.PennyFlip.ActivityStatistics;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.whompum.PennyFlip.ActivityStatistics.Data.ReportData;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

import java.util.List;

import static com.whompum.PennyFlip.Money.Transaction.TransactionType.ADD;
import static com.whompum.PennyFlip.Money.Transaction.TransactionType.SPEND;

@RestrictTo(RestrictTo.Scope.SUBCLASSES)
interface StatisticsMvpContract {

    interface Presenter{
        void onMonthSelected(final long monthMillis);
        void onStatisticsTypeSelected(@IntRange(from = ADD, to = SPEND) final int transactionType);
    }

    interface View{
        void displayUserMonths(final List<Long> monthDates);
        void displayTransactionsOverview(@NonNull final List<Transaction> data);
        void displayReportData(@NonNull final ReportData data);
        void displayTopSources(@Nullable final List<Source> sources, final int transactionType);
    }

}
