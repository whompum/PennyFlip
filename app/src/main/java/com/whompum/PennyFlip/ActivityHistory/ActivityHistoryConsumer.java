package com.whompum.PennyFlip.ActivityHistory;

import android.content.Context;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.TimeRange;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

import java.util.List;

/**
 * Consumer of data operations done for the {@link ActivityHistory}
 */
public interface ActivityHistoryConsumer {
    void fetch(@NonNull final TimeRange timeRange);
    long fetchStartDate(@NonNull final Context c);
}
