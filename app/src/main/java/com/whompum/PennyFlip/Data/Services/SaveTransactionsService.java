package com.whompum.PennyFlip.Data.Services;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.whompum.PennyFlip.Data.Schemas.SourceSchema;
import com.whompum.PennyFlip.Data.Schemas.TransactionsSchema;
import com.whompum.PennyFlip.Source.SourceWrapper;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Transaction.Models.Transactions;

/**
 * Created by bryan on 1/29/2018.
 */

public class SaveTransactionsService extends IntentService {

    private static final String TITLE = "saveSourceService";

    public static final String SOURCE_KEY = "source.ky";
    public static final String TRANS_AMOUNT_KEY = "transactionAmount.ky";


    public SaveTransactionsService(){
        super(TITLE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent == null)
            return;

        final SourceWrapper source = (SourceWrapper) intent.getSerializableExtra(SOURCE_KEY);
        final long amount = intent.getLongExtra(TRANS_AMOUNT_KEY, 0L);

        if(amount == 0L)
            return;

        final boolean isNew = source.getTagType() == SourceWrapper.TAG.NEW;

        /**
         * If the sourceWrapper is new (Meaning we're creating a new source object)
         * First insert a new Source into the Database, then fetch the ID for the newly
         * created source, and set our sourceWrappers id to that value
         * Then hand of to insertTransaction
         */
        if(isNew)
            source.setSourceId(resolveIdFromUri(createNewSource(source)));

        insertTransaction(source, amount);

        updateWalletTotal(source.getSourceType(), amount);
    }

   private Uri createNewSource(final SourceWrapper wrapper){

        final ContentValues values = new ContentValues();

        final long now = Timestamp.now().millis();

        values.put(SourceSchema.SourceTable.COL_TITLE, wrapper.getTitle());
        values.put(SourceSchema.SourceTable.COL_TYPE, wrapper.getSourceType());
        values.put(SourceSchema.SourceTable.COL_CREATION_DATE, now);
        values.put(SourceSchema.SourceTable.COL_LAST_UPDATE, now);

        return getContentResolver().insert(SourceSchema.SourceTable.URI, values);
   }



    private void insertTransaction(final SourceWrapper sourceWrapper, final long amount){

        final long now = Timestamp.now().millis();

        final ContentValues values = new ContentValues();
        values.put(TransactionsSchema.TransactionTable.COL_TIMESTAMP, now);
        values.put(TransactionsSchema.TransactionTable.COL_TOTAL, amount);
        values.put(TransactionsSchema.TransactionTable.COL_SOURCE_ID, sourceWrapper.getSourceId());
        values.put(TransactionsSchema.TransactionTable.COL_SOURCE_NAME, sourceWrapper.getTitle());
        values.put(TransactionsSchema.TransactionTable.COL_SOURCE_TYPE, sourceWrapper.getSourceType());

        getContentResolver().insert(TransactionsSchema.TransactionTable.URI, values);
        updateSource(sourceWrapper, amount, now);

    }

    /**
     * Updates the source objects total after the transaction has been pushed
     *
     * @param sourceWrapper Contains the source data
     * @param amount The new amount to add to the old
     * @param timestamp
     */
    private void updateSource(final SourceWrapper sourceWrapper, final long amount, final long timestamp){

        final ContentValues values = new ContentValues();
        values.put(SourceSchema.SourceTable.COL_TOTAL, sourceWrapper.getOriginalAmount() + amount);
        values.put(SourceSchema.SourceTable.COL_LAST_UPDATE, timestamp);

        getContentResolver().update(SourceSchema.SourceTable.URI, values, SourceSchema.SourceTable._ID + "=?", new String[]{String.valueOf(sourceWrapper.getSourceId())});

    }

    private void updateWalletTotal(final int transactionType, final long amount){

        final Intent intent = new Intent(this, UpdateWalletService.class);
        intent.putExtra(UpdateWalletService.TRANSACTION_TYPE_KEY, transactionType);
        intent.putExtra(UpdateWalletService.TRANSACTION_AMOUNT_KEY, amount);

        startService(intent);
    }


    /**
     * Static utility method to return a row index marker value from the
     * given URI
     *
     * @param uri The uri to fetch the row index from
     * @return The row index of the Uri
     */
    private static long resolveIdFromUri(final Uri uri){
        return ContentUris.parseId(uri);
    }

}









