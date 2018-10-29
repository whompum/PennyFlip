package com.whompum.PennyFlip.Money.Queries;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Queries.Query.QueryResult;

public interface QueryReceiver<T> {
    void onQueryReceived(@NonNull final QueryResult<T> t);
}
