package com.whompum.PennyFlip.Money.TEMP;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyQueryBase;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

import java.util.List;

public class TransactionGroupQueryHandler extends GroupQueryHandler<List<Transaction>> {

    public TransactionGroupQueryHandler(@NonNull MoneyDatabase database) {
        super(database);
    }

    @Override
    protected void internalQuery(@NonNull MoneyRequest query) {



        super.internalQuery(query);
    }

    @Override
    protected MoneyQueryBase<List<Transaction>> getDao() {
        return moneyDatabase.getTransactionAccessor();
    }
}
