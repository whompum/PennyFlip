package com.whompum.PennyFlip.Data.Providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.whompum.PennyFlip.Data.Schemas.TransactionsSchema;
import com.whompum.PennyFlip.Data.Storage.TransactionsHelper;



/**
 * Created by bryan on 1/17/2018.
 */

public class TransactionsProvider extends ContentProvider {

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.whompum.PennyFlip.Data.Providers.TransactionsProvider";

    public static final Uri URI = Uri.parse(SCHEME + AUTHORITY);

    private TransactionsHelper helper;


    @Override
    public boolean onCreate() {

        this.helper = new TransactionsHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        /**
         * Cursor query (String table,
         String[] columns,
         String selection,
         String[] selectionArgs,
         String groupBy,
         String having,
         String orderBy)
         */

        final Cursor queryCursor = helper.getReadableDatabase()
                .query(TransactionsSchema.TransactionTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

            if(getContext().getContentResolver() != null)
            queryCursor.setNotificationUri(getContext().getContentResolver(), uri);


        Log.i("TRANSACTIONS", "CURSOR IS NULL: " + String.valueOf(queryCursor == null) );

        return queryCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return TransactionsSchema.TransactionTable.MIME_TRANSACTIONS_DIRECTORY;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        long id = -1;

        try {
            id = helper.getWritableDatabase().insertOrThrow(TransactionsSchema.TransactionTable.TABLE_NAME, null, values);
        }catch(SQLException e){

        }

        Uri newUri = uri;

       if(id != -1)
          newUri = ContentUris.withAppendedId(uri, id);

       getContext().getContentResolver().notifyChange(newUri, null);

        return newUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

       final int numRows =  helper.getWritableDatabase().delete(TransactionsSchema.TransactionTable.TABLE_NAME, selection, selectionArgs);

       if(numRows > 0)
           if(getContext().getContentResolver()!=null)
               getContext().getContentResolver().notifyChange(uri, null);

        return numRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
            throw new UnsupportedOperationException("");
    }
}
