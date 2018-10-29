package com.whompum.PennyFlip.Money.Queries.Query;

import android.support.annotation.Nullable;

/**
 * Simple data wrapper
 *
 * @param <T> The {@link Class} type represented by this object.
 */
public class QueryParameter<T> {

    //The data value
    private T t;

    /**
     * Construct a new QueryParameter object containing a single search value
     * NOTE: Null values are permitted as default
     * @param t the search value
     */
    public QueryParameter(@Nullable final T t){
        this.t = t;
    }

    /**
     * Get the value this object is associated with
     *
     * @return the object this value wraps
     */
    public T get() {
        return t;
    }
}
