package com.whompum.PennyFlip;

import android.support.annotation.NonNull;

public interface InflationObserver {
    void onViewInflated();
    boolean subscribe(@NonNull final InflationOperation operation);
}
