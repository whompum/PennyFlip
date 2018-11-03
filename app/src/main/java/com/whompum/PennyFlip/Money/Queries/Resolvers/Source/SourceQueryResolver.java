package com.whompum.PennyFlip.Money.Queries.Resolvers.Source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.Contracts.Source.SourceDao;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Resolvers.QueryResolver;
import com.whompum.PennyFlip.Money.Source.Source;

import java.util.List;

public class SourceQueryResolver extends QueryResolver<List<Source>> {

    private SourceDao dao;

    public SourceQueryResolver(@NonNull MoneyDatabase database) {
       this.dao = database.getSourceAccessor();
    }

    @Nullable
    @Override
    public List<Source> resolve(@NonNull MoneyRequest moneyQuery) {

        final SourceResolverHelper helper =
                new SourceResolverHelper(moneyQuery);

        List<Source> data = null;

        if( helper.isEmpty() )
            data = dao.fetchAll();

        else if( helper.useLikeTitle() ){

            if( helper.useTransactionType() )
                data = dao.fetchLikeTitleOfType( helper.resolveLikeTitle(), helper.resolveType() );

            else data = dao.fetchLikeTitle( helper.resolveLikeTitle() );

        }

        else if( helper.useTransactionType() )
            data = dao.fetchOfType( helper.resolveType() );

        else if( helper.useTimerange() )
            data = dao.fetchTimeRange(
                    helper.resolveTimerange().getMillisFloor(),
                    helper.resolveTimerange().getMillisCiel()
            );

        return data;
    }


}
