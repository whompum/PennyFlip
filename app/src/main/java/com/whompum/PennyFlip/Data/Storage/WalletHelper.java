package com.whompum.PennyFlip.Data.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.whompum.PennyFlip.Data.Schemas.WalletSchema;

/**
 * Created by bryan on 1/17/2018.
 */

public class WalletHelper extends SQLiteOpenHelper {

    private Context context;

    public WalletHelper(final Context c){
        super(c, WalletSchema.WALLET_DATABASE_NAME, null, WalletSchema.WALLET_DATABASE_VERSION);
        this.context = c;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WalletSchema.Wallet.CREATE_TABLE);
        insertRecord(db); //Insert


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO Day job, Add support for versioned Sqlite files
        //Follow tutorial here: https://riggaroo.co.za/android-sqlite-database-use-onupgrade-correctly/
    }


    /**
     * INSERTS THE DEFAULT RECORD INTO THE DATABASE @ Creation Time
     *
     * @param database
     */
    private void insertRecord(final SQLiteDatabase database){

        final ContentValues values = new ContentValues();
        values.put(WalletSchema.Wallet.COL_CURR_TOTAL, 0L);
        values.put(WalletSchema.Wallet.COL_TOTAL_ADDED, 0L);
        values.put(WalletSchema.Wallet.COL_TOTAL_SPENT, 0L);

        try {
            database.insertOrThrow(WalletSchema.Wallet.TABLE_NAME, null, values);
        }catch (SQLException e){
            Log.i("WALLET", "HERE COMES THE ERROR");
            Log.i("WALLET", e.getMessage());
            e.printStackTrace();
        }finally {

        }

    }

}
