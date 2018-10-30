package com.whompum.PennyFlip.Money.Queries;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.whompum.PennyFlip.Money.MoneyThread;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyQuery;
import com.whompum.PennyFlip.Money.Queries.Query.QueryParameter;
import com.whompum.PennyFlip.Money.Queries.Query.QueryResult;
import com.whompum.PennyFlip.Money.MoneyDatabase;

import java.util.ArrayList;
import java.util.Collection;

public abstract class RoomMoneyReader<DATA_TYPE, ID_TYPE/*MAY REMOVE. */> implements MoneyReader, Handler.Callback{

    public static final int MSG_WHAT_ITEM = 0;
    public static final int MSG_WHAT_GROUP = 1;

    //Used to dispatch results to the Main thread
    private Handler threadBridge = new Handler(this);
    protected MoneyDao<DATA_TYPE, ID_TYPE> dao;

    private QueryReceiver<Collection<DATA_TYPE>> groupReceiver;
    private QueryReceiver<DATA_TYPE> itemReceiver;

    public RoomMoneyReader(@NonNull final Context context,
                           @Nullable final QueryReceiver<Collection<DATA_TYPE>> groupReceiver,
                           @Nullable final QueryReceiver<DATA_TYPE> itemReceiver){
        this.dao = getDao(
                Room.databaseBuilder(context, MoneyDatabase.class, MoneyDatabase.NAME).build()
        );

        this.groupReceiver = groupReceiver;
        this.itemReceiver = itemReceiver;
    }

    @Override
    public void query(@NonNull final MoneyQuery query) {
       new MoneyThread().doInBackground(new MoneyThread.MoneyThreadOperation() {
           @Override
           public void doOperation() {
               call(query);
           }
       });
    }

    @MainThread
    @SuppressWarnings("unchecked call")
    @Override
    public boolean handleMessage(Message msg) {

        //Compose the data into a QueryResult object
        final QueryResult result = new QueryResult<>(msg.obj);

        QueryReceiver receiver = null;

        if(msg.what == MSG_WHAT_ITEM)
            receiver = itemReceiver;

        else if(msg.what == MSG_WHAT_GROUP)
            receiver = groupReceiver;

        if(receiver != null)
        //Unchecked call
        receiver.onQueryReceived(result);

        return true;
    }

    /**
     * Where the dao query logic is held.
     * The super class implementation should be called to fetch values
     * for a default query.
     *
     * @param query the wanted data type
     */
    @WorkerThread
    protected void call(@NonNull final MoneyQuery query){

        final int defKey = MoneyQuery.RESERVED_KEY_IS_EMPTY;

        if(query.containsNonNullQueryAtKey(defKey)) {

            final QueryParameter parameter = query.getQueryParameter(defKey);

            if ( parameter != null && (Boolean) parameter.get() )
                postGroupToMainThread( dao.fetchAll() );
        }
        //Deliver an empty list if we wont fulfill the request
        else postGroupToMainThread( new ArrayList<DATA_TYPE>() );

    }

    @WorkerThread
    protected void postItemToMainThread(DATA_TYPE data){
        post(MSG_WHAT_ITEM, data);
    }

    @WorkerThread
    protected void postGroupToMainThread(Collection<DATA_TYPE> data){
        post(MSG_WHAT_GROUP, data);
    }

    @WorkerThread
    private void post(final int msgType, final Object data){

        final Message m = threadBridge.obtainMessage();

        m.what = msgType;
        m.obj = data;

        threadBridge.sendMessage(m);

    }

    protected boolean isUsableParameter(@NonNull final Integer key, @NonNull final MoneyQuery query){
        return query.containsNonNullQueryAtKey(key);
    }

    protected abstract MoneyDao<DATA_TYPE, ID_TYPE> getDao(@NonNull final MoneyDatabase database);

}













