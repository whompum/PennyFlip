package com.whompum.PennyFlip.ActivityStatistics.Fragment;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.ActivityStatistics.Data.ReportData;
import com.whompum.PennyFlip.Money.Source.Source;

import java.util.List;

import static com.whompum.PennyFlip.Money.Transaction.TransactionType.INCOME;
import static com.whompum.PennyFlip.Money.Transaction.TransactionType.EXPENSE;

public class StatisticsViewLayerContract {

    public interface Activity{
        void onTransactionTypeChange(@IntRange(from = INCOME, to = EXPENSE) final int newType);
    }
    public interface Fragment{
        void displayReportData(@NonNull final ReportData data);
        void displayTopSources(@NonNull final List<Source> topSources, @IntRange(from = INCOME, to = EXPENSE) final int newType);
    }

}
