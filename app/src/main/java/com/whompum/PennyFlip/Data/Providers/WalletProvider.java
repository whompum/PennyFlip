package com.whompum.PennyFlip.Data.Providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Data.Schemas.WalletSchema;
import com.whompum.PennyFlip.Data.Storage.WalletHelper;

/**
 * Created by bryan on 1/17/2018.
 */

public class WalletProvider extends ContentProvider{

    public static final String UOE = "The Wallet DB is Read/Update only";

    private WalletHelper helper;

    private static final String WHERE = BaseColumns._ID + "=?";
    private static final String[] WHERE_ARGS = new String[]{"1"};

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.whompum.PennyFlip.Data.Providers.WalletProvider";

    public static final Uri URI = Uri.parse(SCHEME + AUTHORITY);

    private static UriMatcher matcher;

    static{
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        /**
         * May match in future if needed
         */
    }

    @Override
    public boolean onCreate() {
        this.helper = new WalletHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;  //NO USE
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final Cursor queryCursor = helper.getReadableDatabase().query(WalletSchema.Wallet.TABLE_NAME,
                new String[]{WalletSchema.Wallet.COL_TOTAL}, WHERE, WHERE_ARGS, null, null, null);

        if(queryCursor.getColumnCount() > 0)
            if(getContext().getContentResolver() != null);
                queryCursor.setNotificationUri(getContext().getContentResolver(), uri);

    return queryCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new UnsupportedOperationException(UOE);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException(UOE);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String where, @Nullable String[] whereArgs) {

       final SQLiteDatabase sqlite = helper.getWritableDatabase();
        /**
         * In future, run the URI through a matcher, and fetch an appended record index
         * e.g. content://authority/table/recordIndex
         */

       sqlite.update(WalletSchema.Wallet.TABLE_NAME, values, WHERE, WHERE_ARGS);

    return 1;
    }
}







