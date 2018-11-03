package com.whompum.PennyFlip.Money.Contracts.Source;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;

import java.util.List;

@Dao
public interface SourceObservableDao {

    @Query("SELECT * FROM Source")
    LiveData<List<Source>> fetchAll();

    @Query("SELECT * FROM Source WHERE title = :title")
    LiveData<Source> fetchById(@NonNull final String title);

    //Fetches a list of Sources whose title is 'like' `title`
    @Query("SELECT * FROM Source WHERE title like :title")
    LiveData<List<Source>> fetchLikeTitle(@NonNull final String title);

    //Fetches a list of source whose title is 'like' `title` and whose type is of `type`
    @Query("SELECT * FROM Source WHERE title like :title AND transactionType = :type")
    LiveData<List<Source>> fetchLikeTitleOfType(@NonNull final String title,
                                                      @IntRange(from = TransactionType.ADD, to = TransactionType.CALLIBRATE) final int type);

    @Query("SELECT * FROM Source WHERE creationDate >= :mFloor AND creationDate <= :mCiel")
    LiveData<List<Source>> fetchTimeRange(final long mFloor, final long mCiel);

    //Fetches a list of Sources whose type is of `type`
    @Query("SELECT * FROM Source where transactionType = :type")
    LiveData<List<Source>> fetchOfType(@IntRange(from = TransactionType.ADD, to = TransactionType.CALLIBRATE) final int type);


}
