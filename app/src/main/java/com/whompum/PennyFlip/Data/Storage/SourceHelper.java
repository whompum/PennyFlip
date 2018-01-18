package com.whompum.PennyFlip.Data.Storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.whompum.PennyFlip.Data.Schemas.SourceSchema;

/**
 * Created by bryan on 1/18/2018.
 */

public class SourceHelper extends SQLiteOpenHelper {

    public SourceHelper(final Context context){
        super(context, SourceSchema.SOURCE_DATABASE_NAME, null, SourceSchema.SOURCE_DATABASE_VERIONS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SourceSchema.SourceTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Todo Add upgrading logic
    }
}
