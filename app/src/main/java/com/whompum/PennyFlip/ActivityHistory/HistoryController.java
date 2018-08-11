package com.whompum.PennyFlip.ActivityHistory;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.TimeRange;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionsDao;
import com.whompum.PennyFlip.Time.UserStartDate;

import java.util.List;

/**
 * Data provider object for the History Activity.
 */
public class HistoryController implements ActivityHistoryConsumer{

    private TransactionsDao accessor;

    private Handler client;

    public HistoryController(@NonNull final Context context, final Handler client){
        this.accessor = Room.databaseBuilder(context, MoneyDatabase.class, MoneyDatabase.NAME)
                .build()
                .getTransactionAccessor();

        this.client = client;
    }

    @Override
    public void fetch(@NonNull final TimeRange range){
        //Query MoneyController for all Transactions within timerange

        new Thread(){
            @Override
            public void run() {
               List<Transaction> data;

               if( ((data = accessor.fetch(range.getMillisFloor(), range.getMillisCiel())) != null) &&
                       client != null) {

                   final Message m = Message.obtain(client);
                    m.obj = data;

                    client.sendMessage(m);

               }
            }
        }.start();

    }

    @Override
    public long fetchStartDate(@NonNull final Context context){
        //Fetch start date

       final long userStartDate = UserStartDate.getUserStartDate(context);

       //Start of 2018; For testing purposes
    return 1514793600000L;
    }


}
