package com.whompum.PennyFlip.Money.Transaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.MoneyDao;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyQuery;
import com.whompum.PennyFlip.Money.Queries.QueryReceiver;
import com.whompum.PennyFlip.Money.Queries.RoomMoneyReader;

import java.util.Collection;

public class RoomTransactionReader extends RoomMoneyReader<Transaction, Integer> {


    public RoomTransactionReader(@NonNull Context context,
                                 @Nullable QueryReceiver<Collection<Transaction>> groupReceiver,
                                 @Nullable QueryReceiver<Transaction> itemReceiver) {

        super(context, groupReceiver, itemReceiver);
    }


    @Override
    public void call(@NonNull MoneyQuery query) {

        //Give the super-class a chance to handle the query in case of defaults or ID
        super.call(query);

        /*
            TODO: Map query keys to DAO methods
            First account for simple cases (default - id)
            Then start from largest DAO methods to the smallest DAO methods
            This will be accomplished using boolean flags to determine if we have data
         */

    }

    @Override
    protected MoneyDao<Transaction, Integer> getDao(@NonNull MoneyDatabase database) {
        return database.getTransactionAccessor();
    }


}














