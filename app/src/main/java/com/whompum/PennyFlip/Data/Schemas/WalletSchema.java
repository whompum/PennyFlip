package com.whompum.PennyFlip.Data.Schemas;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.whompum.PennyFlip.Data.Providers.WalletProvider;

/**
 * Schema of the app wallet. The wallet very simply stores how much money the app user has; That's it.
 */

public class WalletSchema {

    private WalletSchema(){} //Hide the constructor


    public static final String WALLET_DATABASE_NAME = "Wallet.db";
    public static final int WALLET_DATABASE_VERSION = 1; //TODO DECREMENT TO ONE BEFORE APP LAUNCH


    public static final class Wallet implements BaseColumns{

        private Wallet(){}


        public static final String TABLE_NAME = "WALLET";
        public static final String COL_TOTAL = "WalletTotal";

        public static final String CONSTRAINT_DEFAULT = " DEFAULT";

        public static final String DEF_VALUE_CONSTRAINT = " 0";

        public static final String[] COLUMNS_WITH_ID = {_ID, COL_TOTAL};
        public static final String[] COLUMNS_NO_ID = {COL_TOTAL};


        public static final Uri URI = Uri.withAppendedPath(WalletProvider.URI, "sqlite/"+TABLE_NAME );

        public static final String MIME_TYPE_DIR = "vnd.com.whompum.PennyFlip/dir/money";
        public static final String MIME_TYPE_ITEM = "vnd.com.whompum.PennyFlip/item/money";


        public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( " +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + " CHECK(" + BaseColumns._ID + " < 2), " +
                COL_TOTAL + " INTEGER" + CONSTRAINT_DEFAULT + DEF_VALUE_CONSTRAINT +
                " CHECK(" + COL_TOTAL + " >= 0)" + " CHECK(" + COL_TOTAL + " <=" + String.valueOf(Long.MAX_VALUE) + ")" + ");";

        /**
         * Check constraint against _id is to ensure only 1 record exists
         * Checks against walletTotal is to ensure a negative value is never entered, AND something is always entered.
         *
         * This record should only be set once from the app (@ DATABASE CREATION TIME), and then altered/updated by clients, never added to.
         *
         */


    }

}
