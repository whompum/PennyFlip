package com.whompum.PennyFlip.Transactions.Adapter;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.ListUtils.AdapterItem;
import com.whompum.PennyFlip.ListUtils.ExpandableContent;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

public class TransactionsContent implements ExpandableContent, AdapterItem {

    private boolean isXpanded = false;
    private int groupPos;

    private Transaction transaction;

    public TransactionsContent(@NonNull final Transaction t){
        this.transaction = t;
    }

    public TransactionsContent(@NonNull final Transaction t, final int groupPos){
        this.transaction = t;
        this.groupPos = groupPos;
    }


    @Override
    public void toggle() {
        isXpanded = !isXpanded;
    }

    @Override
    public boolean isExpanded() {
        return isXpanded;
    }

    @Override
    public void setPosition(int position) {
        this.groupPos = position;
    }

    @Override
    public int getPosition() {
        return groupPos;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
