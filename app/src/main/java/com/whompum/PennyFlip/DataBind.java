package com.whompum.PennyFlip;

import android.support.annotation.NonNull;

public interface DataBind<T> {
    void bind(@NonNull final T item);
}
