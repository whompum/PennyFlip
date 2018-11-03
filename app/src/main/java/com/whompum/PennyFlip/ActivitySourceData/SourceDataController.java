package com.whompum.PennyFlip.ActivitySourceData;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.Money.DatabaseUtils;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Deliverable;
import com.whompum.PennyFlip.Money.Queries.LoggerResponder;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Responder;
import com.whompum.PennyFlip.Money.Queries.SourceQueries;
import com.whompum.PennyFlip.Money.Source.SourceQueryKeys;
import com.whompum.PennyFlip.Money.Writes.MoneyWriter;
import com.whompum.PennyFlip.Money.Writes.RoomMoneyWriter;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.Transaction;

public class SourceDataController  implements SourceDataConsumer, Observer<Source>{

    private SourceDataClient client;

    private LiveData<Source> sourceObservable;

    private MoneyWriter dataWriter;
    private MoneyRequest.QueryBuilder builder = new MoneyRequest.QueryBuilder(
            SourceQueryKeys.KEYS
    );

    private SourceQueries queries = new SourceQueries();

    private MoneyDatabase database;

    public SourceDataController(@NonNull final Context context, @NonNull final SourceDataClient client){
        this.client = client;
        this.database = DatabaseUtils.getMoneyDatabase( context );
        this.dataWriter = new RoomMoneyWriter( database );
    }

    @Override
    public void observeSource(@NonNull final String sourceId, @NonNull final LifecycleOwner l){
        builder.setQueryParameter( SourceQueryKeys.TITLE, sourceId );

        final Deliverable<LiveData<Source>> deliverable =
                queries.queryObservableById( builder.getQuery(), database );

        deliverable.attachResponder(new Responder<LiveData<Source>>() {
            @Override
            public void onActionResponse(@NonNull LiveData<Source> data) {
                sourceObservable = data;
                data.observe( l, SourceDataController.this );
            }
        });

        deliverable.attachCancelledResponder( new LoggerResponder( SourceDataController.class ) );
    }

    @Override
    public void saveTransaction(@NonNull Transaction transaction) {
        dataWriter.saveTransaction( transaction );
    }

    @Override
    public void deleteSource(@NonNull String sourceId) {
        dataWriter.deleteSource( sourceId );
    }

    @Override
    public void unObserve(@NonNull LifecycleOwner owner) {
        if( sourceObservable.hasActiveObservers() )
            sourceObservable.removeObservers( owner );
    }

    @Override
    public void onChanged(@Nullable Source source) {
         if( client != null && source != null ) client.onSourceChanged( source );
    }

}
