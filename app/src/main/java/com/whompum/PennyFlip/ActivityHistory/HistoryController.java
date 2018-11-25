package com.whompum.PennyFlip.ActivityHistory;

import android.content.Context;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.DatabaseUtils;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Deliverable;
import com.whompum.PennyFlip.Money.Queries.LoggerResponder;
import com.whompum.PennyFlip.Money.Queries.Responder;
import com.whompum.PennyFlip.Money.Queries.TransactionQueries;
import com.whompum.PennyFlip.Money.TimeRange;
import com.whompum.PennyFlip.Money.Transaction.DescendingSort;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionQueryBuilder;
import com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys;
import com.whompum.PennyFlip.Time.UserStartDate;

import java.util.Collections;
import java.util.List;

/**
 * Exposes a historical view of transaction data, according to a {@link TimeRange},
 * from the {@link ActivityHistoryClient}.
 */
public class HistoryController implements ActivityHistoryConsumer, Responder<List<Transaction>> {

    private ActivityHistoryClient client;

    private TransactionQueryBuilder builder = new TransactionQueryBuilder();

    private TransactionQueries server = new TransactionQueries();

    private MoneyDatabase database;

    public HistoryController(@NonNull final Context context, @NonNull final ActivityHistoryClient client){
        this.client = client;
        database = DatabaseUtils.getMoneyDatabase( context );
    }

    @Override
    public void onActionResponse(@NonNull List<Transaction> data) {
        Collections.sort( data, new DescendingSort() );

        if( client != null )
            client.onDataQueried( data );
    }

    /**
     * Fetch a list of {@link Transaction} objects according to
     * the provided {@link TimeRange}
     *
     * @param range The minimum and maximum dates to fetch Transactions for
     */
    @Override
    public void fetch(@NonNull final TimeRange range){

        builder.setQueryParameter( TransactionQueryKeys.TIMERANGE, range );

        final Deliverable<List<Transaction>> deliverable =
            server.queryGroup( builder.getQuery(), database );

        deliverable.attachCancelledResponder( new LoggerResponder( HistoryController.class ) );
        deliverable.attachResponder( this );

    }

    @Override
    public long fetchStartDate(@NonNull final Context context){
        //Fetch start date

       final long userStartDate = UserStartDate.getUserStartDate( context );

       //return UserStartDate.getUserStartDate(context);
       //Swap with user start data when in production.

    //return 1514793600000L; //Jan 1st, 2018. For testing
    return userStartDate;
    }

}
