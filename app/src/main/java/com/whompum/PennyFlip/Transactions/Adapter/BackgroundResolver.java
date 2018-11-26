package com.whompum.PennyFlip.Transactions.Adapter;

import android.support.annotation.DrawableRes;

public interface BackgroundResolver{

    @DrawableRes
    int getBackground(final int transactionType);
}
