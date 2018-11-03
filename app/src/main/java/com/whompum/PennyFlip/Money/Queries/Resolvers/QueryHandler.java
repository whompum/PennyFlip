package com.whompum.PennyFlip.Money.Queries.Resolvers;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.whompum.PennyFlip.Money.MoneyThread;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Query.QueryResult;
import com.whompum.PennyFlip.Money.Queries.Query.QueryReceiver;

/**
 *
 * @param <T>
 */
public class QueryHandler<T> implements Handler.Callback {

    public static final int QUERY_SUCCESS = 0;
    public static final int QUERY_FAILED = 0;

    public static final int BAD_QUERY_PARAMETER = 1;
    public static final int NULL_DATA_QUERY = 2;

    private QueryReceiver<T> queryReceiver;

    private T data;

    private Handler handler = new Handler(this);

    private QueryResolver<T> resolver;

    public QueryHandler(@NonNull final QueryResolver<T> resolver){
        this.resolver = resolver;
    }

    public void call(@NonNull final MoneyRequest query) {
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
                queryReceiver.onQueryFailed(msg.arg1, ((msg.obj != null) ? (String) msg.obj : null) );
        }

        return true;
    }

    @WorkerThread
    protected void internalQuery(@NonNull final MoneyRequest query){
        if(resolver != null) {
            resolver.setQueryReceiver(new QueryReceiver<T>() { //Set an internal QueryReceiver
                @Override
                public void onQueryReceived(@Nullable QueryResult<T> t) {
                    onDone( t.getT() );
                }

                @Override
                public void onQueryFailed(int reason, @Nullable String msg) {
                    onDone(reason, msg);
                }
            });
            resolver.resolveQuery( query );
        }
    }


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

}
