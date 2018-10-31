package com.whompum.PennyFlip.Money.Contracts.Source;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyDaoId;
import com.whompum.PennyFlip.Money.Contracts.MoneyQueryBase;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;

import java.util.List;

@Dao
public interface ObservableSourceAccessor extends MoneyQueryBase<LiveData<List<Source>>>,
        MoneyDaoId<LiveData<Source>> {

    @Query("SELECT * FROM Source") //ALL
    LiveData<List<Source>> fetch(); //LiveData so it can be observed

    @Query("SELECT * FROM SOURCE WHERE title = :title")
    LiveData<Source> fetchExact(@NonNull final String title);

    @Query("SELECT * FROM Source WHERE title like :title AND transactionType = :type") //Similar to Title
    LiveData<List<Source>> fetchNonExact(@NonNull final String title, final int type);

    @Query("SELECT * FROM Source WHERE title like :title")
    LiveData<List<Source>> fetchNonExact(@NonNull final String title);

    @Query("SELECT * FROM Source where transactionType = :type") // Similar to Transaction
    LiveData<List<Source>> fetch(@IntRange(from = TransactionType.ADD, to = TransactionType.CALLIBRATE) final int type);

    @Query("SELECT  * FROM Source WHERE title = :title AND transactionType = :type")
    LiveData<List<Source>> fetch(@NonNull final String title, final int type);

}
