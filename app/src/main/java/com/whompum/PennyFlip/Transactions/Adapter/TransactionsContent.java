package com.whompum.PennyFlip.Transactions.Adapter;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.ListUtils.AdapterItem;
import com.whompum.PennyFlip.ListUtils.ExpandableContent;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

public class TransactionsContent implements ExpandableContent, AdapterItem {

    private Transaction transaction;

    public TransactionsContent(@NonNull final Transaction t){
        this.transaction = t;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
