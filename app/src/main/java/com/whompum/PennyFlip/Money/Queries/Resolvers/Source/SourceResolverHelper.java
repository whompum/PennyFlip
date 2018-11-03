package com.whompum.PennyFlip.Money.Queries.Resolvers.Source;

import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Source.SourceQueryKeys;
import com.whompum.PennyFlip.Money.TimeRange;

public class SourceResolverHelper {

    private MoneyRequest request;

    public SourceResolverHelper(MoneyRequest request) {
        this.request = request;
    }

    public boolean isEmpty(){

        if( request.containsNonNullQueryAtKey(MoneyRequest.RESERVED_KEY_IS_EMPTY) )
            return (Boolean) request.getQueryParameter(MoneyRequest.RESERVED_KEY_IS_EMPTY).get();

    return true;
    }

    public boolean useTitle(){
        return request.containsNonNullQueryAtKey(SourceQueryKeys.TITLE);
    }

    public boolean useLikeTitle(){
        return request.containsNonNullQueryAtKey(SourceQueryKeys.LIKE_TITLE);
    }

    public boolean useTimerange(){
        return request.containsNonNullQueryAtKey(SourceQueryKeys.TIMERANGE);
    }

    public boolean useTransactionType(){
        return request.containsNonNullQueryAtKey(SourceQueryKeys.TRANSACTION_TYPE);
    }

    public boolean useAmount(){
        return request.containsNonNullQueryAtKey(SourceQueryKeys.AMOUNT);
    }

    public String resolveTitle(){
        if( useTitle() )
            return (String)request.getQueryParameter(SourceQueryKeys.TITLE).get();

    return null;
    }

    public String resolveLikeTitle() {
        if (useLikeTitle())
            return (String) request.getQueryParameter(SourceQueryKeys.LIKE_TITLE).get();

    return null;
    }

    public TimeRange resolveTimerange(){
        if( useTimerange() )
            return (TimeRange) request.getQueryParameter(SourceQueryKeys.TIMERANGE).get();

    return null;
    }

    public Integer resolveType(){
        if( useTransactionType() )
            return (Integer) request.getQueryParameter(SourceQueryKeys.TRANSACTION_TYPE).get();

    return null;
    }

}
