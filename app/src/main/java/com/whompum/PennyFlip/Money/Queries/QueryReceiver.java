package com.whompum.PennyFlip.Money.Queries;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.Queries.Query.QueryResult;

public interface QueryReceiver<T> {
    void onQueryReceived(@Nullable final QueryResult<T> t);
}
