package com.whompum.PennyFlip.Data.Storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.whompum.PennyFlip.Data.Schemas.TransactionsSchema;

/**
 * Created by bryan on 1/17/2018.
 */

public class TransactionsHelper extends SQLiteOpenHelper{


    public TransactionsHelper(final Context context){
        super(context, TransactionsSchema.TRANSACTIONS_DATABASE_NAME, null, TransactionsSchema.TRANSACTIONS_DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TransactionsSchema.TransactionTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO add upgrading logic
    }
}
