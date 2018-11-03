package com.whompum.PennyFlip.Money;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

public class DatabaseUtils {


    public static MoneyDatabase getMoneyDatabase(@NonNull final Context context){
        return Room.databaseBuilder(context, MoneyDatabase.class, MoneyDatabase.NAME)
                .build();
    }

}
