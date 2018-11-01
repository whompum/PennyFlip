package com.whompum.PennyFlip.Money.Queries.Resolvers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.Contracts.MoneyDaoId;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Query.QueryParameter;

import static com.whompum.PennyFlip.Money.Contracts.MoneyDaoId.ID_KEY;

public class ItemQueryResolver<T> implements QueryResolver<T> {

    protected MoneyDaoId<T> server;

    public ItemQueryResolver(@NonNull final MoneyDaoId<T> server){
        this.server = server;
    }

    @Override
    @Nullable
    public T resolve(@NonNull MoneyRequest moneyQuery) {

        final QueryParameter param;

        if( (param = moneyQuery.getQueryParameter( ID_KEY )) != null )
            return server.fetchById( param.get() );

        return null;
    }
}
