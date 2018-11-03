package com.whompum.PennyFlip.Money.Queries.Resolvers.Transaction;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.TimeRange;
import com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys;

public class TransactionResolverHelper {

    private MoneyRequest request;

    public TransactionResolverHelper(@NonNull final MoneyRequest request){
        this.request = request;
    }

    public boolean isQueryEmpty(){
        if( request.containsNonNullQueryAtKey(MoneyRequest.RESERVED_KEY_IS_EMPTY) )
            return (Boolean) request.getQueryParameter(MoneyRequest.RESERVED_KEY_IS_EMPTY).get();

        return true;
    }

    public boolean useId(){
        return request.containsNonNullQueryAtKey(TransactionQueryKeys.TRANSACTION_ID);
    }

    public boolean useTimerange() {
        return request.containsNonNullQueryAtKey(TransactionQueryKeys.TIMERANGE);
    }

    public boolean useTransactionType() {
        return request.containsNonNullQueryAtKey(TransactionQueryKeys.TRANSACTION_TYPE);
    }

    public boolean useSourceId() {
        return request.containsNonNullQueryAtKey(TransactionQueryKeys.SOURCE_ID);
    }

    public boolean usePennyValue() {
        //return request.containsNonNullQueryAtKey(TransactionQueryKeys.PENNY_VALUE);
        return false;
    }

    public TimeRange resolveTimerange(){
        if( useTimerange() )
            return (TimeRange) request.getQueryParameter(TransactionQueryKeys.TIMERANGE).get();

        return null;
    }

    public Integer resolveType(){
        if( useTransactionType() )
            return (Integer) request.getQueryParameter(TransactionQueryKeys.TRANSACTION_TYPE).get();

        return null;
    }

    public String resolveSourceId(){
        if( useSourceId() )
            return (String) request.getQueryParameter(TransactionQueryKeys.SOURCE_ID).get();

        return null;
    }

    public Integer resolveId(){
        if( useId() )
            return (Integer) request.getQueryParameter(TransactionQueryKeys.TRANSACTION_ID).get();

        return null;
    }

    //Add Penny Value

}
