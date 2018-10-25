package com.whompum.PennyFlip.ActivityHistory;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Transaction.Transaction;

import java.util.List;

/**
 * Client interface to receive data after a Query was done.
 * Implemented by {@link ActivityHistory}
 */
public interface ActivityHistoryClient {
    void onDataQueried(@NonNull final List<Transaction> data);
}
