package com.whompum.PennyFlip.Dashboard;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.DialogSourceChooser.SourceWrapper;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

public interface ActivityDashboardConsumer{
    void saveTransaction(@NonNull final SourceWrapper source, @NonNull final Transaction transaction);
    void bindWalletObserver(@NonNull final LifecycleOwner o);
    void bindClient(@NonNull final DashboardClient client);
}
