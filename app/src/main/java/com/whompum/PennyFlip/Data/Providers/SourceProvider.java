package com.whompum.PennyFlip.Data.Providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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

        final Cursor queryCursor = helper.getReadableDatabase()
                .query(SourceSchema.SourceTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);


        if(getContext().getContentResolver() != null)
                queryCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return queryCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return SourceSchema.SourceTable.MIME_SOURCES_DIRECTORY;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        long id = -1;

        try {
            id = helper.getWritableDatabase().insertOrThrow(SourceSchema.SourceTable.TABLE_NAME, null, values);
        }catch(SQLException e){

        }

        Uri newUri = null;

        if(id != -1) {
            newUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(newUri, null);
        }




        return newUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int numRows =  helper.getWritableDatabase().delete(SourceSchema.SourceTable.TABLE_NAME, selection, selectionArgs);

            if(getContext().getContentResolver()!=null)
                getContext().getContentResolver().notifyChange(uri, null);

        return numRows;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int numRows = helper.getWritableDatabase().update(SourceSchema.SourceTable.TABLE_NAME, values, selection, selectionArgs);

            if(getContext().getContentResolver() != null)
                getContext().getContentResolver().notifyChange(uri, null);

        return numRows;
    }

}
