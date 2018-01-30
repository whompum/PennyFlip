package com.whompum.PennyFlip.Transaction;

import android.database.Cursor;

import com.whompum.PennyFlip.Data.Schemas.TransactionsSchema;
import com.whompum.PennyFlip.PennyFlipCursorAdapter;
import com.whompum.PennyFlip.PennyFlipCursorAdapterImpl;
import com.whompum.PennyFlip.Transaction.Models.Transactions;

import java.util.ArrayList;
import java.util.List;


public class TransactionsCursorAdapter extends PennyFlipCursorAdapterImpl<Transactions> {


    private final String TIMESTAMP = TransactionsSchema.TransactionTable.COL_TIMESTAMP;
    private final String TOTAL = TransactionsSchema.TransactionTable.COL_TOTAL;
    private final String SOURCE_ID = TransactionsSchema.TransactionTable.COL_SOURCE_ID;
    private final String SOURCE_NAME = TransactionsSchema.TransactionTable.COL_SOURCE_NAME;
    private final String SOURCE_TYPE = TransactionsSchema.TransactionTable.COL_SOURCE_TYPE;

    public TransactionsCursorAdapter(Cursor data) {
        super(data);
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


        if(data != null) {
            long transactionDate;
            long transactionPennies;
            long sourceId;
            String sourceName;
            int sourceType;

            while (data.moveToNext()) {

                transactionDate = data.getLong(timestampIndex());
                transactionPennies = data.getLong(totalIndex());
                sourceName = data.getString(sourceNameIndex());
                sourceId = data.getLong(sourceIdIndex());
                sourceType = data.getInt(sourceTypeIndex());

                final Transactions transactions = new Transactions(sourceType, transactionDate, transactionPennies, sourceName, sourceId);

                transactionsList.add(transactions);

            }
        }


        return transactionsList;
    }


    private int timestampIndex(){
        return data.getColumnIndex(TIMESTAMP);
    }

    private int totalIndex(){
        return data.getColumnIndex(TOTAL);
    }

    private int sourceNameIndex(){
        return data.getColumnIndex(SOURCE_NAME);
    }

    private int sourceTypeIndex(){
        return data.getColumnIndex(SOURCE_TYPE);
    }

    private int sourceIdIndex(){return data.getColumnIndex(SOURCE_ID);}

}
