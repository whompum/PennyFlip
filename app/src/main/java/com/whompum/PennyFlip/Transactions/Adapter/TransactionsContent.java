package com.whompum.PennyFlip.Transactions.Adapter;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.ListUtils.AdapterItem;
import com.whompum.PennyFlip.ListUtils.ExpandableContent;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

public class TransactionsContent implements AdapterItem {

    private Transaction transaction;

    public TransactionsContent(@NonNull final Transaction t){
        this.transaction = t;
    }

    @Override
    public boolean isExpandable() {
        return false;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
