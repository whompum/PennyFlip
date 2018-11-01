package com.whompum.PennyFlip.Money.Queries.Resolvers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.Contracts.Source.SourceDao;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Resolvers.GroupQueryResolver;

public class SourceQueryResolver<T> extends GroupQueryResolver<T> {

    public SourceQueryResolver(@NonNull SourceDao<T> server) {
        super(server);
    }

    @Nullable
    @Override
    public T resolve(@NonNull MoneyRequest moneyQuery) {

        final SourceDao<T> internalServer = (SourceDao<T>) server;

        return super.resolve(moneyQuery);
    }
}
