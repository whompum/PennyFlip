package com.whompum.PennyFlip.Money.Queries.Resolvers.Source;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.Source.SourceObservableDao;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Resolvers.QueryResolver;
import com.whompum.PennyFlip.Money.Source.Source;

import java.util.List;

public class SourceObservableQueryResolver extends QueryResolver<LiveData<List<Source>>> {

    private SourceObservableDao dao;

    public SourceObservableQueryResolver(@NonNull final MoneyDatabase database) {
        this.dao = database.getObservableSourceAccessor();
    }

    @Override
    public LiveData<List<Source>> resolve(@NonNull MoneyRequest moneyQuery) {
        final SourceResolverHelper helper =
                new SourceResolverHelper(moneyQuery);

        LiveData<List<Source>> data = null;

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
