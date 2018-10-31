package com.whompum.PennyFlip.Money.Contracts;

import android.support.annotation.NonNull;

public interface MoneyDaoId<T> {

    <I> T fetchById(@NonNull final I id);

    int ID_KEY = 0;
}
