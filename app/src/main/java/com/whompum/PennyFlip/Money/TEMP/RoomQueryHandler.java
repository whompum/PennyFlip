package com.whompum.PennyFlip.Money.TEMP;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.MoneyDatabase;

public abstract class RoomQueryHandler<T> extends QueryHandler<T> {

    protected MoneyDatabase moneyDatabase;

    public RoomQueryHandler(@NonNull final MoneyDatabase database){
        this.moneyDatabase = database;
    }

}
