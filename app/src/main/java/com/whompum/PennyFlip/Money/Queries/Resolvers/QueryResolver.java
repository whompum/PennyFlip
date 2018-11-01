package com.whompum.PennyFlip.Money.Queries.Resolvers;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Query.QueryReceiver;
import com.whompum.PennyFlip.Money.Queries.Query.QueryResult;
import com.whompum.PennyFlip.Money.Queries.Handlers.QueryHandler;

/**
 * Base strategy interface to query data using a {@link MoneyRequest}
 * Any logic to actually read from a Dao should be described in one of these
 * instances
 *
 * @param <T> The expected return type of data
 */
@WorkerThread
public abstract class QueryResolver<T> {

    private QueryReceiver<T> client;

    public void resolveQuery(@NonNull final MoneyRequest request){
        final T data = resolve(request);

        if(client == null) return;

        if(data == null) client.onQueryFailed(QueryHandler.NULL_DATA_QUERY, "The queried data returned a null response");

        else client.onQueryReceived(new QueryResult<>(data));
    }

    public void setQueryReceiver(@NonNull final QueryReceiver<T> receiver){
       this.client = client;
    }

    /**
     * Resolve a MoneyQueryRequest, returning proper data if exists.
     * @param moneyQuery The query parameters
     * @return The data retrieved after querying a data persistence system
     */
     abstract T resolve(@NonNull final MoneyRequest moneyQuery);



}
