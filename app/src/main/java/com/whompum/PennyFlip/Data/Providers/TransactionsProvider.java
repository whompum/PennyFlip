package com.whompum.PennyFlip.Data.Providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return TransactionsSchema.TransactionTable.MIME_TRANSACTIONS_DIRECTORY;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
