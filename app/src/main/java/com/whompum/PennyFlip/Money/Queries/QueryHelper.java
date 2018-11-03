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

    public QueryHelper(QueryResolver<T> resolver) {
        deliverable = new Deliverable<>();

        handler = new QueryHandler<>( resolver );

        handler.setQueryReceiver(new QueryReceiver<T>() {
            @Override
            public void onQueryReceived(@Nullable QueryResult<T> t) {
                if( t != null && t.getT() != null )
                    deliverable.setData( t.getT() );
            }

            @Override
            public void onQueryFailed(int reason, @Nullable String msg) {
                deliverable.setCancelledResponse( reason, msg );
            }
        });

    }

    public Deliverable<T> query(@NonNull final MoneyRequest request){

        handler.call( request );

        return deliverable;
    }

}
