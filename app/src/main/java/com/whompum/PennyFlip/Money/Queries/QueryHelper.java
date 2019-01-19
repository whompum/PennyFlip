package com.whompum.PennyFlip.Money.Queries;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.whompum.PennyFlip.Money.Queries.Deliverable;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Query.QueryReceiver;
import com.whompum.PennyFlip.Money.Queries.Query.QueryResult;
import com.whompum.PennyFlip.Money.Queries.Resolvers.QueryHandler;
import com.whompum.PennyFlip.Money.Queries.Resolvers.QueryResolver;

public class QueryHelper<T> {

    private Deliverable<T> deliverable;
    private QueryHandler<T> handler;

    private QueryOperation<T> operation;

    public QueryHelper(QueryResolver<T> resolver) {
        deliverable = new Deliverable<>();

        handler = new QueryHandler<>( resolver );

        handler.setQueryReceiver(new QueryReceiver<T>() {
            @Override
            public void onQueryReceived(@Nullable QueryResult<T> t) {

                T data;

                if( t != null && (data = t.getT()) != null ){

                    if( operation != null )
                        data = operation.operate( data );

                    deliverable.setData( data );
                }
            }

            @Override
            public void onQueryFailed(int reason, @Nullable String msg) {
                deliverable.setCancelledResponse( reason, msg );
            }
        });

    }

    public void setOperation(QueryOperation<T> operation) {
        this.operation = operation;
    }

    public Deliverable<T> query(@NonNull final MoneyRequest request){

        handler.call( request );

        return deliverable;
    }

}
