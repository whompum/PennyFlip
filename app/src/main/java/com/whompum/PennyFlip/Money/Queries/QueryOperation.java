package com.whompum.PennyFlip.Money.Queries;

import android.support.annotation.NonNull;

public interface QueryOperation<T> {
    T operate(@NonNull final T t);
}
