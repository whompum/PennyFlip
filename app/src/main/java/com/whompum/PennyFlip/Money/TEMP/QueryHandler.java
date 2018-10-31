package com.whompum.PennyFlip.Money.TEMP;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.whompum.PennyFlip.Money.MoneyThread;
import com.whompum.PennyFlip.Money.Queries.MoneyReader;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Query.QueryResult;
import com.whompum.PennyFlip.Money.Queries.QueryReceiver;

/**
 *
 * @param <T>
 */
public abstract class QueryHandler<T> implements MoneyReader, Handler.Callback {

    public static final int QUERY_SUCCESS = 0;
    public static final int QUERY_FAILED = 0;

    public static final int BAD_QUERY_PARAMETER = 1;
    public static final int NULL_DATA_QUERY = 2;

    private QueryReceiver<T> queryReceiver;
    private T data;
    private Handler handler = new Handler(this);

    public QueryHandler(){

    }

    public QueryHandler(QueryReceiver<T> queryReceiver) {
        this.queryReceiver = queryReceiver;
    }

    @Override
    public void query(@NonNull final MoneyRequest query) {
        new MoneyThread().doInBackground(new MoneyThread.MoneyThreadOperation() {
            @Override
            public void doOperation() {
                internalQuery(query);
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        if(queryReceiver != null) {
            if (msg.what == QUERY_SUCCESS)
                queryReceiver.onQueryReceived(new QueryResult<>(data));
            else if(msg.what == QUERY_FAILED)
                queryReceiver.onQueryFailed(msg.arg1, ((msg.obj != null) ? (String)msg.obj : null) );
        }

        return true;
    }

    @WorkerThread
    protected abstract void internalQuery(@NonNull final MoneyRequest query);


    @WorkerThread
    protected void onDone(@NonNull final T data){
        //Notify the Handler to deliver the data
        this.data = data;
        handler.sendEmptyMessage(QUERY_SUCCESS);
    }

    @WorkerThread
    protected void onDone(final int reason, @Nullable final String msg){
        //TODO notify receivers onCancelled.

        final Message m = handler.obtainMessage();
        m.what = QUERY_FAILED;
        m.arg1 = reason;
        m.obj = msg;

        handler.sendMessage(m);
    }


    public void setQueryReceiver(@NonNull final QueryReceiver<T> queryReceiver){
        this.queryReceiver = queryReceiver;
    }

    public void removeQueryReceiver(){
        this.queryReceiver = null;
    }

}
