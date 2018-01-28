package com.whompum.PennyFlip.Transaction;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.widget.CursorAdapter;

import com.whompum.PennyFlip.Data.Schemas.TransactionsSchema;
import com.whompum.PennyFlip.PennyFlipCursorAdapter;
import com.whompum.PennyFlip.Transaction.Models.Transactions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class TransactionsCursorAdapter implements PennyFlipCursorAdapter<Transactions> {


    private final String TIMESTAMP = TransactionsSchema.TransactionTable.COL_TIMESTAMP;
    private final String TOTAL = TransactionsSchema.TransactionTable.COL_TOTAL;
    private final String SOURCE_NAME = TransactionsSchema.TransactionTable.COL_SOURCE_NAME;
    private final String SOURCE_TYPE = TransactionsSchema.TransactionTable.COL_SOURCE_TYPE;


    private Cursor cursor = null;


    public TransactionsCursorAdapter(){

    }

    /**
     * Returns a List of Transaction Objects
     * from the Cursor;
     *
     * @return A list of transaction objects.
     *
     */
    @Override
    public List<Transactions> fromCursor(){


        /**
         * COL_TIMESTAMP + " INTEGER DEFAULT " + String.valueOf(Timestamp.now().millis()) + ", " +
         COL_CURR_TOTAL + " INTEGER NOT NULL DEFAULT 0, " +
         COL_SOURCE_ID + " TEXT NOT NULL, " +
         COL_SOURCE_NAME + " TEXT NOT NULL, " +
         COL_SOURCE_TYPE + " INTEGER );";
         */

        final List<Transactions> transactionsList = new ArrayList<>(1);


        if(cursor != null) {
            long transactionDate;
            long transactionPennies;
            String sourceName;
            int sourceType;

            while (cursor.moveToNext()) {

                transactionDate = cursor.getLong(timestampIndex());
                transactionPennies = cursor.getLong(totalIndex());
                sourceName = cursor.getString(sourceNameIndex());
                sourceType = cursor.getInt(sourceTypeIndex());

                final Transactions transactions = new Transactions(sourceType, transactionDate, transactionPennies, sourceName);

                transactionsList.add(transactions);

            }
        }


        return transactionsList;
    }

    @Override
    public void setCursor(final Cursor cursor){
        this.cursor = cursor;

    }

    private int timestampIndex(){
        return cursor.getColumnIndex(TIMESTAMP);
    }

    private int totalIndex(){
        return cursor.getColumnIndex(TOTAL);
    }

    private int sourceNameIndex(){
        return cursor.getColumnIndex(SOURCE_NAME);
    }

    private int sourceTypeIndex(){
        return cursor.getColumnIndex(SOURCE_TYPE);
    }

}
