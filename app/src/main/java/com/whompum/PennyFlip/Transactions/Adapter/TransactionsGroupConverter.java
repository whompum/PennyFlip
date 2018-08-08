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
public class TransactionsGroupConverter {

    private static Ts utility = Ts.now();

    /**
     * Converts a list of transaction objects into AdapterItems capable
     * of being expanded, and grouped.
     *
     * This method assumes the transactions are ordered chronologically DESC
     *
     * @param data The transactions to convert
     * @param predicate A strategy object that decides whether we want default expansion on a certain group
     * @return A List of Header objects (with a few potential item objects)
     */
    public static List<AdapterItem> fromTransactions(@NonNull final List<Transaction> data,
                                                     @NonNull final ExpansionPredicate predicate){


        List<AdapterItem> items = new ArrayList<>(0); //Set to zero
        TransactionsGroup currentHeader = null;

        for(Transaction t: data){

            final long dateOfTransaction = startOfDay(t.getTimestamp());

            //New header is needed since we have a new day in the list
            if(currentHeader == null || dateOfTransaction != currentHeader.getMillis()) {
                currentHeader = new TransactionsGroup(dateOfTransaction);
                items.add(currentHeader);

                if(predicate.expand(dateOfTransaction, items.size()-1))
                    currentHeader.toggle(); // Turning on.
            }

            final TransactionsContent content = new TransactionsContent(t);

            if(currentHeader.isExpanded())
                items.add(content);

            currentHeader.addChild(content);
        }

    return items;
    }

    private static long startOfDay(final long millis){
        utility.set(millis);
        return utility.getStartOfDay();
    }

}
