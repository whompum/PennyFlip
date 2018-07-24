package com.whompum.PennyFlip.Data.Services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.whompum.PennyFlip.Data.Schemas.WalletSchema;
import com.whompum.PennyFlip.Money.Transactions.Models.TransactionType;

/**
 * Updates the Wallet total
 */

public class UpdateWalletService extends IntentService {

    public static final String name = "UpdateWallet";

    public static final String TRANSACTION_TYPE_KEY = "transactionType.ky";
    public static final String TRANSACTION_AMOUNT_KEY = "transactionAmount.ky";

    public static final String COL_WALLET_TOTAL = WalletSchema.Wallet.COL_CURR_TOTAL;
    public static final String COL_TOTAL_ADDED = WalletSchema.Wallet.COL_TOTAL_ADDED;
    public static final String COL_TOTAL_SPENT = WalletSchema.Wallet.COL_TOTAL_SPENT;

    public static final Uri uri = WalletSchema.Wallet.URI;

    private static final String WHERE = WalletSchema.Wallet._ID + "=?";
    private static final String[] WHERE_ARGS = {String.valueOf(1)};

    public UpdateWalletService(){
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent == null)
            return;

        final int transactionType = intent.getIntExtra(TRANSACTION_TYPE_KEY, 0);
        final long amount = intent.getLongExtra(TRANSACTION_AMOUNT_KEY, 0L);

        if(transactionType == TransactionType.ADD)
            updateAddColumn(amount);

        else if(transactionType == TransactionType.SPEND)
            updateSpendColumn(amount);

        updateWalletTotal(transactionType, amount);
    }

    private void updateAddColumn(final long amount){
        update(COL_TOTAL_ADDED, fetchCurrTotal(COL_TOTAL_ADDED) + amount);
    }

    private void updateSpendColumn(final long amount){
        update(COL_TOTAL_SPENT, fetchCurrTotal(COL_TOTAL_ADDED) + amount);
    }

    private void updateWalletTotal(final int transactionType, final long amount){

        final long currWalletTotal = fetchCurrTotal(COL_WALLET_TOTAL);

       switch(transactionType){

           case TransactionType.ADD: update(COL_WALLET_TOTAL, currWalletTotal + amount);
           break;

           case TransactionType.SPEND:
               if( (currWalletTotal - amount) <= 0L)
                   update(COL_WALLET_TOTAL, 0L);
               else
                   update(COL_WALLET_TOTAL, currWalletTotal - amount);
           break;
       }

    }

    private long fetchCurrTotal(final String column){

        final Cursor data = getContentResolver().query(uri, new String[]{column},
                WHERE, WHERE_ARGS,  null);

        if(data == null)
            throw new NullPointerException("Null Cursor for Wallet Total");

        Log.i("UPDATE_WALLET", "COUNT: " + data.getCount());

        data.moveToFirst();

        final long currAmount = data.getLong(data.getColumnIndex(column));

        data.close();

        return currAmount;

    }

    private void update(final String column, final long amount){
        final ContentValues values = new ContentValues();
        values.put(column, amount);

        Log.i("WALLET", "UPDATED COLUMN: " + column + " WITH AMOUNT: " + amount);

        getContentResolver().update(uri, values, WHERE, WHERE_ARGS);
    }


}
