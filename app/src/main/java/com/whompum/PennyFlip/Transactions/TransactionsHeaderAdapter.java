package com.whompum.PennyFlip.Transactions;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Time.MidnightTimestamp;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Transactions.Models.HeaderItem;
import com.whompum.PennyFlip.Transactions.Models.TransactionHeaderItem;
import com.whompum.PennyFlip.Transactions.Models.Transactions;
import com.whompum.PennyFlip.Transactions.Models.TransactionsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Takes Transaction objects and converts them into Header objects,
 * along with day timestamps
 */

public class TransactionsHeaderAdapter {


    public static List<HeaderItem> fromList(@NonNull final List<Transactions> t){
        /**
         * Converting The transactions into Transaction items is relatively straight-forward
         * First, every transaction can automatically be wrapped into a TransactionsItem object
         * So that part is done.  Whats next is to check the day of each transactions object, and convert it
         * to a day
         *
         *
         *
         */

        final List<HeaderItem> items = new ArrayList<>(t.size());

        TransactionHeaderItem header = null;

        int headerCount = 1;

        long currMidnight = Long.MIN_VALUE;



        for(Transactions transactions : t){

            final long midnightMillis = getDayMillis(transactions);

            if(currMidnight != midnightMillis){
                currMidnight = midnightMillis;

                header = new TransactionHeaderItem(Timestamp.from(currMidnight), -1);
                headerCount=1;
                final TransactionsItem transItem = new TransactionsItem(transactions);

                items.add(header);
                items.add(transItem);
                header.setNumTransactions(headerCount);
            }else{
                headerCount++;
                header.setNumTransactions(headerCount);
                items.add(new TransactionsItem(transactions));
            }


        }



    return items;
    }



    private static long getDayMillis(final Transactions transactions){
        final long transactionMillis = transactions.getTimestamp().millis();

        return MidnightTimestamp.from(transactionMillis).getTodayMidnightMillis();

    }



}
