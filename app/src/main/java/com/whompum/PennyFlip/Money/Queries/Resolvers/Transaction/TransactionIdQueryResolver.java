package com.whompum.PennyFlip.Money.Queries.Resolvers.Transaction;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.Transaction.TransactionDao;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Resolvers.QueryResolver;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

public class TransactionIdQueryResolver extends QueryResolver<Transaction> {

    private TransactionDao dao;

    public TransactionIdQueryResolver(@NonNull final MoneyDatabase database) {
        this.dao = database.getTransactionAccessor();
    }

    @Override
    public Transaction resolve(@NonNull MoneyRequest moneyQuery) {

        final TransactionResolverHelper helper = new TransactionResolverHelper(moneyQuery);

        if( helper.useId() )
            return dao.fetchById( helper.resolveId() );


        return null;
    }
}
