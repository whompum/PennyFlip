package com.whompum.PennyFlip.ListUtils;

import android.support.annotation.NonNull;

import java.util.Collection;

public interface CollectionQueryReceiver<T> {
    void onNoData();
    void display(@NonNull final Collection<T> data);
}
