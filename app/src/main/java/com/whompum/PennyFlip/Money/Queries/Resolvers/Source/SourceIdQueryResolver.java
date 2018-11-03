package com.whompum.PennyFlip.Money.Queries.Resolvers.Source;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.Source.SourceDao;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Resolvers.QueryResolver;
import com.whompum.PennyFlip.Money.Source.Source;

public class SourceIdQueryResolver extends QueryResolver<Source> {

    private SourceDao dao;

    public SourceIdQueryResolver(@NonNull final MoneyDatabase database) {
        this.dao = database.getSourceAccessor();
    }

    @Override
    public Source resolve(@NonNull MoneyRequest moneyQuery) {

        final SourceResolverHelper helper =
                new SourceResolverHelper(moneyQuery);

        if( helper.useTitle() )
            return dao.fetchById( helper.resolveTitle() );

        return null;
    }
}
