package com.whompum.PennyFlip.Data.Schemas;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.whompum.PennyFlip.Data.Providers.TransactionsProvider;
import com.whompum.PennyFlip.Time.Timestamp;

/**
 * Created by bryan on 1/17/2018.
 */

public class TransactionsSchema {

    public static final  String TRANSACTIONS_DATABASE_NAME = "TransactionTable.db";
    public static final int TRANSACTIONS_DATABASE_VERSION = 1;

    private TransactionsSchema(){}

    public static class TransactionTable implements BaseColumns{

        private TransactionTable(){}

        public static final String TABLE_NAME = "Transactions";

        public static final String COL_TIMESTAMP = "timestamp";
        public static final String COL_TOTAL = "total";
        public static final String COL_SOURCE_ID = "sourceId"; //Stored as a Blob
        public static final String COL_SOURCE_NAME = "name";
        public static final String COL_SOURCE_TYPE= "type";


        public static Uri URI = Uri.withAppendedPath(TransactionsProvider.URI, "sqlite/" + TABLE_NAME);


        public static final String MIME_TYPE_DIR = "vnd.com.whompum.PennyFlip/dir/transactions";
        public static final String MIME_TYPE_ITEM = "vnd.com.whompum.PennyFlip/item/transactions";


        public static final String MIME_TRANSACTIONS_DIRECTORY =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MIME_TYPE_DIR;

        public static final String MIME_TRANSACTIONS_DIRECTORY_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MIME_TYPE_ITEM;


        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TIMESTAMP + " INTEGER DEFAULT " + String.valueOf(Timestamp.now().millis()) + ", " +
                COL_TOTAL + " INTEGER NOT NULL DEFAULT 0, " +
                COL_SOURCE_ID + " TEXT NOT NULL, " +
                COL_SOURCE_NAME + " TEXT NOT NULL, " +
                COL_SOURCE_TYPE + " INTEGER );";

    }

}
