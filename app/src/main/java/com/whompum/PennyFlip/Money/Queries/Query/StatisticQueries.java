package com.whompum.PennyFlip.Money.Queries.Query;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Deliverable;
import com.whompum.PennyFlip.Money.Queries.QueryHelper;
import com.whompum.PennyFlip.Money.Queries.Resolvers.QueryHandler;
import com.whompum.PennyFlip.Money.Queries.Resolvers.Statistics.TransactionStatistcsTypeResolver;
import com.whompum.PennyFlip.Money.Statistics.StatisticQueryKeys;
import com.whompum.PennyFlip.Money.Statistics.TransactionStatistics;

public class StatisticQueries {

    public Deliverable<TransactionStatistics> fetchById(@NonNull final MoneyRequest request,
                                                        @NonNull final MoneyDatabase database){
        return new QueryHelper<>(
                new TransactionStatistcsTypeResolver( database )
        ).query( request );

    }

}
