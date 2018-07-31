package com.whompum.PennyFlip.ActivitySourceData;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.MoneyController;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

public class SourceDataController  implements SourceDataConsumer, Observer<Source>{

    private MoneyController repo;

    private SourceDataClient client;

    private LiveData<Source> data;

    public SourceDataController(@NonNull final Context context, @NonNull final SourceDataClient client){
        repo = MoneyController.obtain(context);
        this.client = client;
    }

    @Override
    public void observeSource(@NonNull final String sourceId, @NonNull final LifecycleOwner l){

        repo.fetchObservableSources(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if( msg.obj instanceof LiveData ) {
                   data = ((LiveData<Source>) msg.obj);

                    data.observe(l, SourceDataController.this);
                }

                return true;
            }
        }), sourceId, null, false);

    }

    @Override
    public void saveTransaction(@NonNull Transaction transaction) {
        repo.updateSourceAmount(transaction);
    }

    @Override
    public void deleteSource(@NonNull String sourceId) {
        repo.deleteSource(sourceId);
    }

    @Override
    public void unObserve(@NonNull LifecycleOwner owner) {
        if(data.hasActiveObservers())
            data.removeObservers(owner);
    }

    @Override
    public void onChanged(@Nullable Source source) {
         if(client != null && source != null) client.onSourceChanged(source);
    }

}
