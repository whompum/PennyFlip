package com.whompum.PennyFlip.ActivityStatistics.Fragment;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.ActivityStatistics.Data.ReportData;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;

import java.util.List;

import static com.whompum.PennyFlip.Money.Transaction.TransactionType.ADD;
import static com.whompum.PennyFlip.Money.Transaction.TransactionType.SPEND;

public class StatisticsViewLayerContract {

    public interface Activity{
        void onTransactionTypeChange(@IntRange(from = ADD, to = SPEND) final int newType);
    }
    public interface Fragment{
        void displayReportData(@NonNull final ReportData data);
        void displayTopSources(@NonNull final List<Source> topSources, @IntRange(from = ADD, to = SPEND) final int newType);
    }

}
