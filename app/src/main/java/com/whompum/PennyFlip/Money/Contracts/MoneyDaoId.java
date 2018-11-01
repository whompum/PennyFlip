package com.whompum.PennyFlip.Money.Contracts;

import android.support.annotation.NonNull;

public interface MoneyDaoId<T> {

    <P> T fetchById(@NonNull final P id);

    int ID_KEY = 0;
}
