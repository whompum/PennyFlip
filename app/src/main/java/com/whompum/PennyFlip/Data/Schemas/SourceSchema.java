package com.whompum.PennyFlip.Data.Schemas;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.whompum.PennyFlip.Data.Providers.SourceProvider;
import com.whompum.PennyFlip.Data.Providers.TransactionsProvider;

/**
 * Created by bryan on 1/17/2018.
 */

public class SourceSchema {

    public static final String SOURCE_DATABASE_NAME = "Source.db";
    public static final int SOURCE_DATABASE_VERIONS = 1;

    public static class SourceTable implements BaseColumns{


        public static final String TABLE_NAME = "Sources";
        public static final String COL_TITLE = "title";
        public static final String COL_TOTAL = "total";
        public static final String COL_TYPE = "type";

        public static Uri URI = Uri.withAppendedPath(SourceProvider.URI, "sqlite/" + TABLE_NAME);


        public static final String MIME_TYPE_DIR = "vnd.com.whompum.PennyFlip/dir/source";
        public static final String MIME_TYPE_ITEM = "vnd.com.whompum.PennyFlip/item/source";



        public static final String MIME_SOURCES_DIRECTORY =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MIME_TYPE_DIR;

        public static final String MIME_SOURCES_DIRECTORY_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MIME_TYPE_ITEM;



        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " VARCHAR(20) UNIQUE NOT NULL, " +
                COL_TOTAL + " INTEGER CHECK( " + COL_TOTAL + ">-1), " +
                COL_TYPE + " INTEGER);";


        /**
         * --table_source
         ---source_title#varchar(N)
         ---source_total_value#long
         ---source_type#Integer
         ---source_UUID#long

         */

    }


}
