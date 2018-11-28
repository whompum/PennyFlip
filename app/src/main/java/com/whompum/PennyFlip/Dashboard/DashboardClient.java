package com.whompum.PennyFlip.Dashboard;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.Transaction.Transaction;

import java.util.List;

public interface DashboardClient {
    void onWalletChanged(final long pennies );
    LifecycleOwner getLifecycleOwner();
    void handleAddTransactions(@Nullable final List<Transaction> data);
    void handleSpendTransactions(@Nullable final List<Transaction> data);
}
