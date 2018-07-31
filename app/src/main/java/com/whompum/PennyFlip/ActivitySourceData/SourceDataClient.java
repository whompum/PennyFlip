package com.whompum.PennyFlip.ActivitySourceData;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Source.Source;

public interface SourceDataClient {
    void onSourceChanged(@NonNull final Source source);
}
