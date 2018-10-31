package com.whompum.PennyFlip.Money.TEMP;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyDaoId;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

public class TransactionItemQueryHandler extends ItemQueryHandler<Transaction> {

    public TransactionItemQueryHandler(@NonNull MoneyDatabase database) {
        super(database);
    }

    @Override
    protected MoneyDaoId<Transaction> getDao() {
        return moneyDatabase.getTransactionAccessor();
    }
}
