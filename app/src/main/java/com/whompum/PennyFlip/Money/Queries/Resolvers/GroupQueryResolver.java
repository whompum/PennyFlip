package com.whompum.PennyFlip.Money.Queries.Resolvers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.Contracts.MoneyQueryBase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;

import static com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest.RESERVED_KEY_IS_EMPTY;

public class GroupQueryResolver<T> implements QueryResolver<T> {

    protected MoneyQueryBase<T> server;

    public GroupQueryResolver(@NonNull final MoneyQueryBase server) {
        this.server = server;
    }

    @Override
    @Nullable
    public T resolve(@NonNull MoneyRequest moneyQuery) {

        //Fetch and launch request
        if( moneyQuery.containsNonNullQueryAtKey( RESERVED_KEY_IS_EMPTY ) )
            return server.fetchAll();

        return null;
    }

}
