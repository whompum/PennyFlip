package com.whompum.PennyFlip.Money.Transaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.MoneyDao;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyQuery;
import com.whompum.PennyFlip.Money.Queries.QueryReceiver;
import com.whompum.PennyFlip.Money.Queries.RoomMoneyReader;
import com.whompum.PennyFlip.Money.TimeRange;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys.SOURCE_ID;
import static com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys.PENNY_VALUE;
import static com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys.TIMERANGE;
import static com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys.TRANSACTION_TYPE;

public class RoomTransactionReader extends RoomMoneyReader<Transaction, Integer> {

    private TransactionDaoQueries queryProxy;

    public RoomTransactionReader(@NonNull Context context,
                                 @Nullable QueryReceiver<Collection<Transaction>> groupReceiver,
                                 @Nullable QueryReceiver<Transaction> itemReceiver) {

        super(context, groupReceiver, itemReceiver);

        queryProxy = (TransactionDaoQueries) dao;
    }


    @Override
    public void call(@NonNull MoneyQuery query) {


        /*
            TODO: Map query keys to DAO methods
            First account for simple cases (default - id)
            Then start from largest DAO methods to the smallest DAO methods
            This will be accomplished using boolean flags to determine if we have data
         */

        boolean useTitle = isUsableParameter(SOURCE_ID, query);
        boolean useValue = isUsableParameter(PENNY_VALUE, query);
        boolean useTimerange = isUsableParameter(TIMERANGE, query);
        boolean useType = isUsableParameter(TRANSACTION_TYPE, query);

        if(useTitle) {
            callInternalForSource(query, useTimerange); //Covers the Source XOR timerange query
            return;
        }

        if(useType){
            callInternalForTransactionType(query, useTimerange); //Covers the Type XOR timerange query
            return;
        }

        if(useTimerange){

            final TimeRange range = fetchTimeRange(query);

            postGroupToMainThread( new ArrayList<>(
                    queryProxy.fetchByTimestamp(range.getMillisFloor(), range.getMillisCiel()))
            );

            return;
        }

        if(useValue){
            //TODO Add logic for fetching by value
        }

        //Give the super-class a chance to handle the query in case of defaults or ID
        super.call(query);
    }

    @Override
    protected MoneyDao<Transaction, Integer> getDao(@NonNull MoneyDatabase database) {
        return database.getTransactionAccessor();
    }

    private void callInternalForSource(@NonNull final MoneyQuery query, final boolean useTimeRange){
        final String sourceId = fetchSource(query);

        if(sourceId == null) return;

        List<Transaction> data = new ArrayList<>();

        if(useTimeRange) //fetchFromSourceAndTime
            data.addAll(queryProxy.fetchFromSourceAndTime(sourceId,
                    fetchTimeRange(query).getMillisFloor(), fetchTimeRange(query).getMillisCiel()
            ));

        else data.addAll( queryProxy.fetchBySource( sourceId ) );

        postGroupToMainThread( data );

    }

    private void callInternalForTransactionType(@NonNull MoneyQuery query, final boolean useTimeranage){

        final Integer transactionType = fetchTransactionType(query);

        if(transactionType == null) return;

        final List<Transaction> data = new ArrayList<>();

        if(useTimeranage)
            data.addAll( queryProxy.fetchByTypeAndTime(transactionType,
                    fetchTimeRange(query).getMillisFloor(), fetchTimeRange(query).getMillisCiel())
            );

        else data.addAll( queryProxy.fetchByType(transactionType) );

        postGroupToMainThread( data );

    }

    @Nullable
    public TimeRange fetchTimeRange(@NonNull final MoneyQuery query){

        if( isUsableParameter(TIMERANGE, query))
            return (TimeRange) query.getQueryParameter(TIMERANGE).get();

        return null;
    }

    @Nullable
    public String fetchSource(@NonNull final MoneyQuery query){

        if( isUsableParameter(SOURCE_ID, query) )
            return (String) query.getQueryParameter(SOURCE_ID).get();

        return null;
    }

    @Nullable
    public Integer fetchTransactionType(@NonNull final MoneyQuery query){

        if(isUsableParameter(TRANSACTION_TYPE, query))
                return (Integer) query.getQueryParameter(TRANSACTION_TYPE).get();

        return null;
    }


}














