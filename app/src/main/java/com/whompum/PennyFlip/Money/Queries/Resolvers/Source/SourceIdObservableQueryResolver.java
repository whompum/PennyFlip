package com.whompum.PennyFlip.Money.Queries.Resolvers.Source;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.Source.SourceObservableDao;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Resolvers.QueryResolver;
import com.whompum.PennyFlip.Money.Source.Source;

public class SourceIdObservableQueryResolver extends QueryResolver<LiveData<Source>> {

    private SourceObservableDao dao;

    public  SourceIdObservableQueryResolver(@NonNull final MoneyDatabase database){
        this.dao = database.getObservableSourceAccessor();
    }

    @Override
    public LiveData<Source> resolve(@NonNull MoneyRequest moneyQuery) {
        final SourceResolverHelper helper =
                new SourceResolverHelper(moneyQuery);

        if( helper.useTitle() )
            return dao.fetchById( helper.resolveTitle() );

        return null;
    }
}
