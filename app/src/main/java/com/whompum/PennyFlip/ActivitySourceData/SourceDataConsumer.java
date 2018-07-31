package com.whompum.PennyFlip.ActivitySourceData;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Transaction.Transaction;

public interface SourceDataConsumer {
    void observeSource(@NonNull final String id, @NonNull final LifecycleOwner owner);
    void saveTransaction(@NonNull final Transaction transaction);
    void deleteSource(@NonNull final String sourceId);
    void unObserve(@NonNull final LifecycleOwner owner);
}
