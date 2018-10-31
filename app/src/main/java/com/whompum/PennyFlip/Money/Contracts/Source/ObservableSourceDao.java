package com.whompum.PennyFlip.Money.Contracts.Source;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Source.Source;

import java.util.List;

@Dao
public abstract class ObservableSourceDao implements com.whompum.PennyFlip.Money.Contracts.MoneyQueryBase<LiveData<Source>> {

    @Query("SELECT * FROM SOURCE WHERE title = :title")
    public abstract LiveData<Source> fetchExact(@NonNull final String title);

    @Query("SELECT * FROM Source WHERE title like :title AND transactionType = :type") //Similar to Title
    abstract LiveData<List<Source>> fetchNonExact(@NonNull final String title, final int type);

}
