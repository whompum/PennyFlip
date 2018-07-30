package com.whompum.PennyFlip.Money.Source;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Transaction.TransactionType;

import java.util.List;

@Dao
public abstract class ObservableSourceAccessor extends SourceAccessor {

    @Query("SELECT * FROM Source") //ALL
    public abstract LiveData<List<Source>> fetch(); //LiveData so it can be observed

    @Query("SELECT * FROM Source WHERE title like :title AND transactionType = :type") //Similar to Title
    public abstract LiveData<List<Source>> fetchNonExact(@NonNull final String title, final int type);

    @Query("SELECT * FROM Source WHERE title like :title")
    public abstract LiveData<List<Source>> fetchNonExact(@NonNull final String title);

    @Query("SELECT * FROM Source where transactionType = :type") // Similar to Transaction
    public abstract LiveData<List<Source>> fetch(@IntRange(from = TransactionType.ADD, to = TransactionType.CALLIBRATE) final int type);

    @Query("SELECT  * FROM Source WHERE title = :title AND transactionType = :type")
    public abstract LiveData<List<Source>> fetch(@NonNull final String title, final int type);

}
