package com.whompum.PennyFlip.Money.Transaction;

import java.util.Comparator;

/**
 * Sorts a Transaction Object in a Descending order based on its timestamp
 */
public class DescendingSort implements Comparator<Transaction> {
    @Override
    public int compare(Transaction o1, Transaction o2) {
        return (int)(o2.getTimestamp() - o1.getTimestamp()); //If i get an overflow, convert Millis to seconds.
    }

}
