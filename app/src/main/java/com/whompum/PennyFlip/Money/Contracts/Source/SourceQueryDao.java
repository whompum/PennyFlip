package com.whompum.PennyFlip.Money.Contracts.Source;

import android.arch.persistence.room.Query;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyQueryBase;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;

public interface SourceQueryDao<T> extends MoneyQueryBase<T> {

    @Override
    @Query("SELECT * FROM Source")
    T fetchAll();

    //Fetches a list of Sources whose title is 'like' `title`
    @Query("SELECT * FROM Source WHERE title like :title")
    T fetchLikeTitle(@NonNull final String title);

    //Fetches a list of source whose title is 'like' `title` and whose type is of `type`
    @Query("SELECT * FROM Source WHERE title like :title AND transactionType = :type")
    T fetchLikeTitleOfType(@NonNull final String title,
                                           @IntRange(from = TransactionType.ADD, to = TransactionType.CALLIBRATE) final int type);

    @Query("SELECT * FROM Source WHERE creationDate >= :mFloor AND creationDate <= :mCiel")
    T fetchTimeRange(final long mFloor, final long mCiel);

    //Fetches a list of Sources whose type is of `type`
    @Query("SELECT * FROM Source where transactionType = :type")
    T fetchOfType(@IntRange(from = TransactionType.ADD, to = TransactionType.CALLIBRATE) final int type);



}
