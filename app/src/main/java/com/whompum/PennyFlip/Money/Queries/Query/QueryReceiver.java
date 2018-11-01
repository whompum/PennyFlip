package com.whompum.PennyFlip.Money.Queries.Query;

import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.Queries.Query.QueryResult;

public interface QueryReceiver<T> {
    void onQueryReceived(@Nullable final QueryResult<T> t);
    void onQueryFailed(final int reason, @Nullable final String msg);
}
