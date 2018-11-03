package com.whompum.PennyFlip.Money.Queries;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Deliverable;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;

import com.whompum.PennyFlip.Money.Queries.QueryHelper;
import com.whompum.PennyFlip.Money.Queries.Resolvers.Transaction.TransactionIdQueryResolver;
import com.whompum.PennyFlip.Money.Queries.Resolvers.Transaction.TransactionObservableQueryResolver;
import com.whompum.PennyFlip.Money.Queries.Resolvers.Transaction.TransactionQueryResolver;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

import java.util.List;

public class TransactionQueries {

    public Deliverable<List<Transaction>> queryGroup(@NonNull final MoneyRequest request,
                                                     @NonNull final MoneyDatabase database){
        return new QueryHelper<>(
                new TransactionQueryResolver( database )
        ).query( request );
    }

    public Deliverable<LiveData<List<Transaction>>> queryObservableObservableGroup(@NonNull final MoneyRequest request,
                                                                         @NonNull final MoneyDatabase database){
        return new QueryHelper<>(
                new TransactionObservableQueryResolver( database )
        ).query( request );
    }

    public Deliverable<Transaction> queryById(@NonNull final MoneyRequest request,
                                              @NonNull final MoneyDatabase database){
        return new QueryHelper<>(
                new TransactionIdQueryResolver( database )
        ).query( request );
    }

}













