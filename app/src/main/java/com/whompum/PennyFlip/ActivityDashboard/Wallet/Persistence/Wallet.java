package com.whompum.PennyFlip.ActivityDashboard.Wallet.Persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Wallet {

    @PrimaryKey
    private int id = 1; //Only one record of this value.

    private long value; //A single unit of currency

    public int getId() {
        return id;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public void setId(int id) {
        this.id = id;
    }
}
