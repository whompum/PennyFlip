package com.whompum.PennyFlip.Transactions.Adapter;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Time.Ts;
import com.whompum.PennyFlip.Transactions.Header.HeaderItem;
import com.whompum.PennyFlip.Transactions.Header.TransactionHeaderItem;
import com.whompum.PennyFlip.Transactions.Header.TransactionsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Takes Transaction objects and converts them into Header objects,
 * along with day timestamps
 */

public class HeaderAdapter {


    public static List<HeaderItem> fromList(@NonNull final List<Transaction> t){
        /**
         * This guy is the major brains of the Header/Transaction flow.
         * In order for a RecyclerView to use Headers, it must assume that both a header and a list item
         * are each considered items within i'ts adapter. Thus the two needs to be merged via a common interface
         * into the same data structure
         *
         * WHAT IT DOES
         * Takes a List of Transaction objects, and for each new date, create a new header item
         * and store it in the list, along with all subsequent Transaction objects for that date.
         *
         */

        final List<HeaderItem> items = new ArrayList<>(t.size());

        TransactionHeaderItem header = null;

        int headerCount = 1;

        long currMidnight = -1;

        for(Transaction tran : t){

            final Ts timestamp = Ts.from(tran.getTimestamp());

            if(currMidnight != timestamp.getStartOfDay()){
                currMidnight = timestamp.getStartOfDay();

                header = new TransactionHeaderItem(timestamp, -1);
                headerCount=1;
                final TransactionsItem transItem = new TransactionsItem(tran);

                items.add(header);
                items.add(transItem);
                header.setNumTransactions(headerCount);
            }else{
                headerCount++;
                header.setNumTransactions(headerCount);
                items.add(new TransactionsItem(tran));
            }

        }

    return items;
    }

}
