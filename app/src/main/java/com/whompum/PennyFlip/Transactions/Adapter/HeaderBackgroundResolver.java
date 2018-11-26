package com.whompum.PennyFlip.Transactions.Adapter;

import android.support.annotation.DrawableRes;

import java.io.Serializable;

public abstract class HeaderBackgroundResolver implements Serializable {

    @DrawableRes
    public abstract int getHeaderBackground();
}
