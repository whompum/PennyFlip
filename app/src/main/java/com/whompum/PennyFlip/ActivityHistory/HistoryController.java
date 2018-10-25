package com.whompum.PennyFlip.ActivityHistory;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.TimeRange;
import com.whompum.PennyFlip.Money.Transaction.DescendingSort;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionsDao;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Time.UserStartDate;

import java.util.Collections;
import java.util.List;

/**
 * Data provider object for the History Activity.
 */
public class HistoryController implements ActivityHistoryConsumer, Handler.Callback{

    private TransactionsDao accessor;
    private Handler queryHandler = new Handler(this);
    private ActivityHistoryClient client;


    public HistoryController(@NonNull final Context context, @NonNull final ActivityHistoryClient client){
        this.accessor = Room.databaseBuilder(context, MoneyDatabase.class, MoneyDatabase.NAME)
                .build()
                .getTransactionAccessor();
        this.client = client;
    }

    /**
     * Callback invoked when a query ran and data was received.
     * This method de
     * @param msg
     * @return
     */
    @Override
    public boolean handleMessage(Message msg) {
        Log.i("TRANSACTION_SELECTION", "handleMessage()");
        Log.i("TRANSACTION_SELECTION", "is msg.obj null: " + (msg.obj == null) );

        if(!(msg.obj instanceof List)) return true;

        final List<Transaction> transactions = (List<Transaction>) msg.obj;

        onPostQuery( transactions );

        return true;
    }

    @Override
    public void fetch(@NonNull final TimeRange range){
        //Query LocalMoneyProvider for all Transactions within timerange

        new Thread(){
            @Override
            public void run() {
               List<Transaction> data;

               if( ((data = accessor.fetch(range.getMillisFloor(), range.getMillisCiel())) != null) &&
                       queryHandler != null) {

                   final Message m = Message.obtain(queryHandler);
                    m.obj = data;

                    queryHandler.sendMessage(m);

               }
            }
        }.start();

    }

    @Override
    public long fetchStartDate(@NonNull final Context context){
        //Fetch start date

       final long userStartDate = UserStartDate.getUserStartDate(context);

       //return UserStartDate.getUserStartDate(context);
       //Swap with user start data when in production.

    return 1514793600000L;
    }

    /**
     * Called after a query ran successfully. Prepares the data for the client
     * and delivers
     * @param data
     */
    private void onPostQuery(@NonNull final List<Transaction> data){

        //Sort the transaction data
        Collections.sort( data, new DescendingSort());

        if(client != null)
            client.onDataQueried(data);

    }


}
