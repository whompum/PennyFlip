package com.whompum.PennyFlip.Money.Contracts;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.support.annotation.NonNull;

/**
 * Server sided interface implemented by a service providing storage
 * of data
 * @param <T> The type of data we're saving
 */
@Dao
public interface MoneyDaoWriter<T> {
    /**
     * Insert a new record into a database.
     * @param t the data to insert
     */
    @Insert
    void insert(@NonNull final T t);
}
