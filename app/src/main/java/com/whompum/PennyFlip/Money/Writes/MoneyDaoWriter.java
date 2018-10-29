package com.whompum.PennyFlip.Money.Writes;

import android.support.annotation.NonNull;

/**
 * Server sided interface implemented by a service providing storage
 * of data
 * @param <T> The type of data we're saving
 */
public interface MoneyDaoWriter<T> {
    /**
     * Insert a new record into a database.
     * @param t the data to insert
     */
    void insert(@NonNull final T t);
}
