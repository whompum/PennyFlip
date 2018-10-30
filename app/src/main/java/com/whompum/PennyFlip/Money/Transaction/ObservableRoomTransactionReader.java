package com.whompum.PennyFlip.Money.Transaction;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.MoneyDao;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyQuery;
import com.whompum.PennyFlip.Money.Queries.QueryReceiver;
import com.whompum.PennyFlip.Money.Queries.RoomMoneyReader;
import com.whompum.PennyFlip.Money.TimeRange;

import java.util.List;

import static com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys.SOURCE_ID;
import static com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys.TIMERANGE;


public class ObservableRoomTransactionReader extends RoomMoneyReader<LiveData<List<Transaction>>, Integer> {


    public ObservableRoomTransactionReader(@NonNull Context context,
                                           @Nullable QueryReceiver<LiveData<List<Transaction>>> itemReceiver) {

        super(context, null, itemReceiver);
    }

    @Override
    public void call(@NonNull MoneyQuery query) {

        final boolean useTitle = isUsableParameter(SOURCE_ID, query);
        final boolean useTimerange = isUsableParameter(TIMERANGE, query);

        if(useTitle)
            postItemToMainThread(((ObservableTransactionsDao) dao).observeBySource(
                    (String) query.getQueryParameter(SOURCE_ID).get()
            ));

        else if(useTimerange) {
            final TimeRange t = (TimeRange) query.getQueryParameter(TIMERANGE).get();
            postItemToMainThread(((ObservableTransactionsDao) dao).observeByTimerange(t.getMillisFloor(),
                    t.getMillisCiel())
            );
        }

        else super.call(query);

    }

    @Override
    protected MoneyDao<LiveData<List<Transaction>>, Integer> getDao(@NonNull MoneyDatabase database) {
        return database.getObservableTransactionAccessor();
    }
}
