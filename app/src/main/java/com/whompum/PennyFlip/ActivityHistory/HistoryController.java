package com.whompum.PennyFlip.ActivityHistory;

import android.content.Context;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Queries.Query.MoneyQuery;
import com.whompum.PennyFlip.Money.Queries.Query.QueryParameter;
import com.whompum.PennyFlip.Money.Queries.Query.QueryResult;
import com.whompum.PennyFlip.Money.Queries.QueryReceiver;
import com.whompum.PennyFlip.Money.TimeRange;
import com.whompum.PennyFlip.Money.Transaction.DescendingSort;
import com.whompum.PennyFlip.Money.Transaction.RoomTransactionReader;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys;
import com.whompum.PennyFlip.Time.UserStartDate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Exposes a historical view of transaction data, according to a {@link TimeRange},
 * to a {@link ActivityHistoryClient}.
 */
public class HistoryController implements ActivityHistoryConsumer, QueryReceiver<Collection<Transaction>> {

    private RoomTransactionReader reader;
    private ActivityHistoryClient client;

    public HistoryController(@NonNull final Context context, @NonNull final ActivityHistoryClient client){
        reader = new RoomTransactionReader(context, this, null);

        this.client = client;
    }


    @Override
    public void fetch(@NonNull final TimeRange range){
        //QueryParameter LocalMoneyProvider for all Transactions within timerange

        reader.query(new MoneyQuery.QueryBuilder(TransactionQueryKeys.KEYS)
                .setQueryParameter(TransactionQueryKeys.TIMERANGE, new QueryParameter<>(range))
                .getQuery()
        );

    }

    @Override
    public long fetchStartDate(@NonNull final Context context){
        //Fetch start date

       final long userStartDate = UserStartDate.getUserStartDate(context);

       //return UserStartDate.getUserStartDate(context);
       //Swap with user start data when in production.

    return 1514793600000L;
    }

    @Override
    public void onQueryReceived(@NonNull QueryResult<Collection<Transaction>> t) {

        final List<Transaction> dataSet = new ArrayList<>(t.getT());

        Collections.sort(dataSet, new DescendingSort());

        if(client != null)
            client.onDataQueried(dataSet);
    }

}
