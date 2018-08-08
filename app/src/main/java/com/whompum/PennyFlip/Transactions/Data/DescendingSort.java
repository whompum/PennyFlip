package com.whompum.PennyFlip.Transactions.Data;

import com.whompum.PennyFlip.Transactions.Adapter.TransactionsContent;

import java.util.Comparator;

public class DescendingSort implements Comparator<TransactionsContent> {

    @Override
    public int compare(TransactionsContent o1, TransactionsContent o2) {
        return (int)(o2.getTransaction().getTimestamp() - o1.getTransaction().getTimestamp()); //If i get an overflow, convert Millis to seconds.
    }
}
