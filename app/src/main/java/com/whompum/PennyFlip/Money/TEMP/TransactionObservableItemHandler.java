package com.whompum.PennyFlip.Money.TEMP;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyDaoId;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

public class TransactionObservableItemHandler extends ItemQueryHandler<LiveData<Transaction>> {

    public TransactionObservableItemHandler(@NonNull MoneyDatabase database) {
        super(database);
    }

    @Override
    protected MoneyDaoId<LiveData<Transaction>> getDao() {
        return moneyDatabase.getObservableTransactionAccessor();
    }
}
