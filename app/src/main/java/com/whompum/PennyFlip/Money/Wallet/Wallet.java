package com.whompum.PennyFlip.Money.Wallet;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Wallet {

    public static final int WALLET_ID = 1;

    @PrimaryKey
    private int id = WALLET_ID; //Only one record of this value.

    private long value; //DaoQueryAdapter single unit of currency

    public Wallet(int id, long value) {
        this.id = id;
        this.value = value;
    }

    @Ignore
    public Wallet(final long value){
        setValue(value);
    }

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
