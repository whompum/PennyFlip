package com.whompum.PennyFlip.Money.Queries.Resolvers.Transaction;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.Transaction.TransactionObservableDao;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Resolvers.QueryResolver;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

import java.util.List;

public class TransactionObservableQueryResolver extends QueryResolver<LiveData<List<Transaction>>> {

    private TransactionObservableDao dao;

    public TransactionObservableQueryResolver(@NonNull final MoneyDatabase database) {
        this.dao = database.getObservableTransactionAccessor();
    }

    @Override
    public LiveData<List<Transaction>> resolve(@NonNull MoneyRequest moneyQuery) {

        final TransactionResolverHelper helper = new TransactionResolverHelper(moneyQuery);

        LiveData<List<Transaction>> data = null;

        if( helper.isQueryEmpty() )
            data = dao.fetchAll();

        else if( helper.useSourceId() )
            data = dao.fetchBySource( helper.resolveSourceId() );

        else if( helper.useTransactionType() )
            data = dao.fetchByType( helper.resolveType() );

        else if( helper.useTimerange() )
            data = dao.fetchByTime(
                    helper.resolveTimerange().getMillisFloor(),
                    helper.resolveTimerange().getMillisCiel()
            );

        return data;
    }
}

/*

    @Query("SELECT * FROM `Transaction` WHERE timestamp >= :mFloor AND timestamp <= :mCiel")
    LiveData<List<Transaction>> fetchByTime(final long mFloor, final long mCiel);


 */
