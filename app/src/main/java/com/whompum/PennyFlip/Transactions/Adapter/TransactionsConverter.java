package com.whompum.PennyFlip.Transactions.Adapter;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.ListUtils.AdapterItem;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Time.Ts;
import com.whompum.PennyFlip.Transactions.Header.TransactionsGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts Transaction Objects into Expandable / Collapsable objects
 */
public class TransactionsConverter {

    private static Ts utility = Ts.now();

    public static List<AdapterItem> fromTransactions(@NonNull final List<Transaction> data){

        /*
           STEP ONE: Find start of day for transaction millis
           STEP TWO: If CurrentHeader is null, or if the newest transaction millis doesn't match the headers
                     Create a new Header, and insert into list, Assign a group position
           STEP THREE: For each Transaction to equals the currentHeaders millis, create and set its GroupPosition

         */

        List<AdapterItem> items = new ArrayList<>(0); //Set to zero

        TransactionsGroup currentHeader = null;

        for(Transaction t: data){

            final TransactionsContent content = new TransactionsContent(t);

            final long dateOfTransaction = startOfDay(t.getTimestamp());

            //New header is needed since we have a new day in the list
            if(currentHeader == null || dateOfTransaction != currentHeader.getMillis()){

                //Create.
                currentHeader = new TransactionsGroup(dateOfTransaction);

                //Insert.
                items.add(currentHeader);

                //Position.
                currentHeader.setPosition(items.size()-1);

            }

            content.setPosition(currentHeader.getPosition());

            items.add(content);
        }

    return items;
    }

    private static long startOfDay(final long millis){
        utility.set(millis);
        return utility.getStartOfDay();
    }

}
