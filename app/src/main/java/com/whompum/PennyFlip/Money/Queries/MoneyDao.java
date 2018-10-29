package com.whompum.PennyFlip.Money.Queries;

import android.arch.persistence.room.Dao;
import android.support.annotation.NonNull;

import java.util.List;

@Dao
public interface MoneyDao<T, PRIMARY_KEY_TYPE> {

    List<T> fetchAll();
    T fetchById(@NonNull final PRIMARY_KEY_TYPE primary_key_type);

    interface MoneyDaoId{
        int ID_KEY = 0;
    }

}
