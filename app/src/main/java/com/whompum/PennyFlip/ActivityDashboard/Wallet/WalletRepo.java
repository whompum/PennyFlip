package com.whompum.PennyFlip.ActivityDashboard.Wallet;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.ActivityDashboard.Wallet.Persistence.Wallet;
import com.whompum.PennyFlip.ActivityDashboard.Wallet.Persistence.WalletDao;
import com.whompum.PennyFlip.ActivityDashboard.Wallet.Persistence.WalletRoom;
import com.whompum.PennyFlip.Transaction.Models.TransactionType;

/**
 * Simple business layer between the
 * Wallet persistence Layer, and any clients.
 * Registers The Wallet Repo
 */
public class WalletRepo {

    public static final String WALLET_DB_NAME = "Walletabcdefg.db";

    private static WalletRepo instance;

    private LiveData<Wallet> data;

    private WalletDao accessor;

    private Wallet utilWallet = new Wallet();

    private WalletRepo(@NonNull final Context c){

       this.accessor = Room.databaseBuilder(c, WalletRoom.class, WALLET_DB_NAME)
               .build().getDao();

       this.data = accessor.get();
    }

    public static WalletRepo obtain(@Nullable final Context c){
        if(instance == null)
            instance = new WalletRepo(c.getApplicationContext());

        return instance;
    }


    public LiveData<Wallet> getData() {
        return data;
    }


    private long currentValue(){
        if(data.getValue() != null)
            return data.getValue().getValue();

        return 0L;
    }

    public synchronized void updateWallet(final int type, final long amt){

        long newValue = -1;

        if(type == TransactionType.ADD)
            newValue = currentValue()+amt;

        if(type == TransactionType.SPEND){

            newValue = currentValue() - amt;

            if(newValue < 0)
                newValue = 0L;

        }

        if(newValue == -1)
            return;

        utilWallet.setValue(newValue);

        update();
    }

    private void update(){
        new Thread(){
            @Override
            public void run() {
                accessor.update(utilWallet);
            }
        }.start();
    }

}













