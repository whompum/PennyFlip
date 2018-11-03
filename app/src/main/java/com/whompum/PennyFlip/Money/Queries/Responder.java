package com.whompum.PennyFlip.Money.Queries;

import android.support.annotation.NonNull;

public interface Responder<T> {
    void onActionResponse(@NonNull final T data);
}
