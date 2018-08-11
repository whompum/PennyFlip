package com.whompum.PennyFlip.ActivityHistory;

import android.content.Context;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.TimeRange;

public interface ActivityHistoryConsumer {
    void fetch(@NonNull final TimeRange timeRange);
    long fetchStartDate(@NonNull final Context c);
}
