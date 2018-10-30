package com.whompum.PennyFlip.ActivitySourceData;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.MoneyWriter;
import com.whompum.PennyFlip.Money.RoomMoneyWriter;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

public class SourceDataController  implements SourceDataConsumer, Observer<Source>{

    private SourceDataClient client;

    private LiveData<Source> data;

    private MoneyWriter dataWriter;

    public SourceDataController(@NonNull final Context context, @NonNull final SourceDataClient client){
        this.client = client;
        this.dataWriter = new RoomMoneyWriter(context);
    }

    @Override
    public void observeSource(@NonNull final String sourceId, @NonNull final LifecycleOwner l){

    }

    @Override
    public void saveTransaction(@NonNull Transaction transaction) {
        dataWriter.saveTransaction(transaction);
    }

    @Override
    public void deleteSource(@NonNull String sourceId) {
        dataWriter.deleteSource(sourceId);
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
