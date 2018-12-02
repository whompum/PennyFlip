package com.whompum.PennyFlip.ActivitySourceData;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.annotation.TransitionRes;

import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Wallet.Wallet;

import java.util.List;

public interface SourceDataClient {
    void onSourceChanged(@NonNull final Source source);
    void onTransactionChanged(@NonNull final List<Transaction> data);
    void onWalletChanged(@NonNull final Wallet wallet);
    LifecycleOwner getLifecycleOwner();
}
