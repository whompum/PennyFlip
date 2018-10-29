package com.whompum.PennyFlip.Money.Queries.Query;

import android.support.annotation.NonNull;

public class QueryResult<T> {
    private T t;

    public QueryResult(@NonNull final T t){
        this.t = t;
    }

    public T getT() {
        return t;
    }
}
