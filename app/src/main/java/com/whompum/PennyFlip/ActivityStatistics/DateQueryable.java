package com.whompum.PennyFlip.ActivityStatistics;

import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.TimeRange;

public interface DateQueryable {
    void setDate(@Nullable final TimeRange range);
}
