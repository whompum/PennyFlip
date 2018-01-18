package com.whompum.PennyFlip.Data.Providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Data.Schemas.SourceSchema;
import com.whompum.PennyFlip.Data.Storage.SourceHelper;

/**
 * Created by bryan on 1/18/2018.
 */

public class SourceProvider extends ContentProvider {


    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.whompum.PennyFlip.Data.Providers.SourceProvider";

    public static final Uri URI = Uri.parse(SCHEME + AUTHORITY);


    private SQLiteOpenHelper helper;



    @Override
    public boolean onCreate() {
        this.helper = new SourceHelper(getContext());

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
        return SourceSchema.SourceTable.MIME_SOURCES_DIRECTORY;
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
