package com.whompum.PennyFlip.Money.Writes;

import android.support.annotation.NonNull;

/**
 * Client sided interface that will invoke a save request,
 * most likely a call to {@link MoneyDaoWriter}. This is the
 * interface to the server to which a client to speak to.
 * @param <T> The type of data to save
 */
public interface MoneyWriter<T> {
    /**
     * Save a new record to storage.
     * @param t  The data to save
     */
    void save(@NonNull final T t);
}
