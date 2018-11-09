package com.whompum.PennyFlip.Money;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

public class DatabaseUtils {

    private static MoneyDatabase database;

    public static MoneyDatabase getMoneyDatabase(@NonNull final Context context){

        if( database == null )
            database = Room.databaseBuilder(context.getApplicationContext(), MoneyDatabase.class, MoneyDatabase.NAME)
                .build();

        return database;
    }

}
