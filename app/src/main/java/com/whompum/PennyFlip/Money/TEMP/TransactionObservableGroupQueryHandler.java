package com.whompum.PennyFlip.Money.TEMP;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Contracts.MoneyQueryBase;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

import java.util.List;

public class TransactionObservableGroupQueryHandler extends GroupQueryHandler<LiveData<List<Transaction>>> {
    public TransactionObservableGroupQueryHandler(@NonNull MoneyDatabase database) {
        super(database);
    }

    @Override
    protected MoneyQueryBase<LiveData<List<Transaction>>> getDao() {
        return moneyDatabase.getObservableTransactionAccessor();
    }
}
