package com.whompum.PennyFlip.Money.Queries.Resolvers.Transaction;

import android.support.annotation.NonNull;
import android.util.Log;

import com.whompum.PennyFlip.Money.Contracts.Transaction.TransactionDao;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Resolvers.QueryResolver;
import com.whompum.PennyFlip.Money.Queries.Resolvers.Transaction.TransactionResolverHelper;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionQueryResolver extends QueryResolver<List<Transaction>> {

    private TransactionDao dao;

    public TransactionQueryResolver(@NonNull MoneyDatabase database) {
        this.dao = database.getTransactionAccessor();
    }

    public List<Transaction> resolve(@NonNull MoneyRequest moneyQuery) {

        //Do typical checking of values and then call the right methods

        List<Transaction> data = null;

        final TransactionResolverHelper helper = new TransactionResolverHelper(moneyQuery);

        Log.i("QUERY_TRANSACTIONS_TEST", "In Transaction Query Resolver");

        //Check for simple cases like if the money query is empty
        if( helper.isQueryEmpty() )
            data = dao.fetchAll();

        else if ( helper.useSourceId() ){

            if( helper.useTimerange() )

                data = dao.fetchFromSourceAndTime(
                        helper.resolveSourceId(),
                        helper.resolveTimerange().getMillisFloor(),
                        helper.resolveTimerange().getMillisCiel());

            else data = dao.fetchBySource( helper.resolveSourceId() );

        } else if ( helper.useTransactionType() ){

            if( helper.useTimerange() ){

                data = dao.fetchByTypeAndTime(
                        helper.resolveType(),
                        helper.resolveTimerange().getMillisFloor(),
                        helper.resolveTimerange().getMillisCiel());

            } else data = dao.fetchByType( helper.resolveType() );

        } else if ( helper.useTimerange() ){

            data = dao.fetchByTimestamp(
                    helper.resolveTimerange().getMillisFloor(),
                    helper.resolveTimerange().getMillisCiel() );

        } else if ( helper.usePennyValue() ) data = new ArrayList<>(); //Unused Currently

        return data;
    }

    /**
     *
     *     //Timestamp
     *     @Query("SELECT * From `Transaction` WHERE timestamp >= :floor AND timestamp <= :ciel")
     *     List<Transaction> fetchByTimestamp(final long floor, final long ciel);
     *
     *     //Amount
     *     @SuppressWarnings("unused")
     *     @Query("SELECT * FROM `Transaction` WHERE amount >= :floorPennies AND amount <= :cielPennies")
     *     List<Transaction> fetchByAmountRange(final long floorPennies, final long cielPennies);
     *
     *
     */



}
