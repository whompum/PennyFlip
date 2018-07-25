package com.whompum.PennyFlip.DialogSourceChooser;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Transaction.Transaction;

public interface OnSourceItemSelected {
    void onSourceItemSelected(@NonNull final SourceWrapper wrapper, @NonNull final Transaction transaction);
}
