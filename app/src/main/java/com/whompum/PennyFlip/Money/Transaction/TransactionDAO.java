package com.whompum.PennyFlip.Money.Transaction;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Writes.MoneyDaoWriter;
import com.whompum.PennyFlip.Money.Queries.MoneyDao;

import java.util.List;

@Dao
public interface TransactionDAO extends MoneyDao<Transaction, Integer>,
        MoneyDao.MoneyDaoId, MoneyDaoWriter<Transaction>{

    //ALL
    @Override
    @Query("SELECT * FROM `Transaction`")
    List<Transaction> fetchAll();

    @Override
    @Insert
    void insert(@NonNull final Transaction o);

    //RESERVED_KEY_ID
    @Override
    @Query("SELECT * FROM `Transaction` WHERE id = :id")
    Transaction fetchById(@NonNull final Integer id);

    //TransactionType
    @Query("SELECT * From `Transaction` WHERE transactionType = :transactionType")
    List<Transaction> fetchByType(final int transactionType);

    //Timestamp
    @Query("SELECT * From `Transaction` WHERE timestamp >= :floor AND timestamp <= :ciel")
    List<Transaction> fetchByTimestamp(final long floor, final long ciel);

    //SourceId
    @Query("SELECT * FROM `Transaction` WHERE sourceId = :sourceId")
    List<Transaction> fetchBySource(@NonNull final String sourceId);

    //Amount
    @SuppressWarnings("unused")
    @Query("SELECT * FROM `Transaction` WHERE amount >= :floorPennies AND amount <= :cielPennies")
    List<Transaction> fetchByAmountRange(final long floorPennies, final long cielPennies);

    //TransactionType - Timestamp
    @Query("SELECT * FROM `Transaction` WHERE transactionType = :transactionType AND timestamp >= :floor AND timestamp <= :ciel")
    List<Transaction> fetchByTypeAndTime(final int transactionType, final long floor, final long ciel);

    //TransactionType - SourceId
    @Query("SELECT * FROM `transaction` WHERE transactionType = :transactionType AND sourceId = :sourceId")
    List<Transaction> fetchByTypeAndSource(final int transactionType, final String sourceId);

    //Timestamp - SourceId
    @Query("SELECT * From `Transaction` WHERE timestamp >= :floor AND timestamp <= :ciel AND sourceId = :sourceId")
    List<Transaction> fetchFromSourceAndTime(@NonNull final String sourceId, final long floor, final long ciel);

    //TransactionType - Timestamp - SourceId
    @Query("SELECT * From `Transaction` WHERE timestamp >= :floor AND timestamp <= :ciel " +
            "AND sourceId = :sourceId " + "AND transactionType = :transactionType")
    List<Transaction> fetchFromTimeAndSourceAndType(final long floor, final long ciel,
                                                    final  String sourceId, final int transactionType);

    /*
        TODO Add from `amount` on
     */

    /*
        all: All
        id: Singular

        transactionType: All
        timestamp: All
        sourceId: All
        amount: All

        transactionType - Timestamp: All
        transactionType - SourceId: All
        Timestamp - SourceId: All

        transactionType - Timestamp - SourceId: All

        amount - transactionType: All
        amount - sourceId: All
        amount - Timestamp: All

        amount - transactionType - sourceId: All
        amount - transactionType - timestamp: All
        amount - timestamp - sourceId: All

        amount - transactionType - sourceId - Timestamp: All

     */

}
