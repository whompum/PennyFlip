package com.whompum.PennyFlip.Money.Queries;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Deliverable;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.QueryHelper;
import com.whompum.PennyFlip.Money.Queries.Resolvers.Source.SourceIdObservableQueryResolver;
import com.whompum.PennyFlip.Money.Queries.Resolvers.Source.SourceIdQueryResolver;
import com.whompum.PennyFlip.Money.Queries.Resolvers.Source.SourceObservableQueryResolver;
import com.whompum.PennyFlip.Money.Queries.Resolvers.Source.SourceQueryResolver;
import com.whompum.PennyFlip.Money.Source.Source;

import java.util.List;

public class SourceQueries {

    public Deliverable<List<Source>> queryGroup(@NonNull final MoneyRequest request,
                                                @NonNull final MoneyDatabase database){
        return new QueryHelper<>(
                new SourceQueryResolver( database )
        ).query( request );
    }

    public Deliverable<List<Source>> queryGroupWithOperation(@NonNull final MoneyRequest request,
                                                             @NonNull final MoneyDatabase database,
                                                             @NonNull final QueryOperation<List<Source>> operation){
        final QueryHelper<List<Source>> helper = new QueryHelper<>( new SourceQueryResolver( database ) );

        helper.setOperation( operation );

        return helper.query( request );
    }

    public Deliverable<LiveData<List<Source>>> queryObservableObservableGroup(@NonNull final MoneyRequest request,
                                                                                   @NonNull final MoneyDatabase database){
        return new QueryHelper<>(
                new SourceObservableQueryResolver( database )
        ).query( request );
    }

    public Deliverable<Source> queryById(@NonNull final MoneyRequest request,
                                              @NonNull final MoneyDatabase database){
        return new QueryHelper<>(
                new SourceIdQueryResolver( database )
        ).query( request );
    }

    public Deliverable<LiveData<Source>> queryObservableById(@NonNull final MoneyRequest request,
                                                             @NonNull final MoneyDatabase database){
        return new QueryHelper<>(
                new SourceIdObservableQueryResolver( database )
        ).query( request );
    }


}
