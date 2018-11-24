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
import com.whompum.PennyFlip.Money.Queries.TransactionQueries;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Source.SourceQueryKeys;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys;
import com.whompum.PennyFlip.Money.Writes.RoomMoneyWriter;

import java.util.List;

public class ActivitySourceDataController implements SourceDataConsumer{

    private SourceDataClient client;

    private String sourceId;

    private LiveData<List<Transaction>> transactionData;
    private LiveData<Source> sourceData;

    private RoomMoneyWriter writer;

    public ActivitySourceDataController(@NonNull final SourceDataClient client,
                                        @NonNull final Context context,
                                        @NonNull final String sourceId){

        this.client = client;

        this.sourceId = sourceId;

        final MoneyDatabase database = DatabaseUtils.getMoneyDatabase( context );

        query( database );

        writer = new RoomMoneyWriter( database );
    }


    @Override
    public void saveTransaction(@NonNull Transaction transaction) {
        writer.saveTransaction( transaction );
    }

    @Override
    public void deleteSource(@NonNull String sourceId) {
        writer.deleteSource( sourceId );
    }

    public void observeTransactions(@NonNull final LifecycleOwner owner){
        if( transactionData != null )
            transactionData.observe(owner, new Observer<List<Transaction>>() {
                @Override
                public void onChanged(@Nullable List<Transaction> transactions) {
                    if( transactions != null )
                        client.onTransactionChanged( transactions );
                }
            });
    }

    public void observeSource(@NonNull final LifecycleOwner owner){
        if( sourceData != null )
            sourceData.observe(owner, new Observer<Source>() {
                @Override
                public void onChanged(@Nullable Source source) {
                    if( source != null )
                        client.onSourceChanged( source );
                }
            });
    }

    public void unObserveTransactions(@NonNull final LifecycleOwner owner){
        if( transactionData != null && transactionData.hasActiveObservers() )
            transactionData.removeObservers( owner );
    }

    public void unObserverSource(@NonNull final LifecycleOwner owner){
        if( sourceData != null && sourceData.hasActiveObservers() )
            sourceData.removeObservers( owner );
    }

    protected void query(@NonNull final MoneyDatabase database){

        queryTransactions( database );
        querySource( database );

    }

    private void queryTransactions(@NonNull final MoneyDatabase database){

        MoneyRequest.QueryBuilder transactionRequest = new MoneyRequest.QueryBuilder( TransactionQueryKeys.KEYS );
        transactionRequest.setQueryParameter( TransactionQueryKeys.SOURCE_ID, sourceId );

        final Deliverable<LiveData<List<Transaction>>> deliverable =
                new TransactionQueries().queryObservableObservableGroup( transactionRequest.getQuery(), database );

        deliverable.attachCancelledResponder( new LoggerResponder( ActivitySourceData.class ) );

        deliverable.attachResponder(new Responder<LiveData<List<Transaction>>>() {
            @Override
            public void onActionResponse(@NonNull LiveData<List<Transaction>> data) {
                transactionData = data;
                observeTransactions( client.getLifecycleOwner() );
            }
        });
    }

    private void querySource(@NonNull final MoneyDatabase database){

        MoneyRequest.QueryBuilder sourceRequest = new MoneyRequest.QueryBuilder( SourceQueryKeys.KEYS );
        sourceRequest.setQueryParameter( SourceQueryKeys.TITLE, sourceId );

        final Deliverable<LiveData<Source>> deliverable =
                new SourceQueries().queryObservableById( sourceRequest.getQuery(), database );

        deliverable.attachCancelledResponder( new LoggerResponder( ActivitySourceData.class ) );

        deliverable.attachResponder(new Responder<LiveData<Source>>() {
            @Override
            public void onActionResponse(@NonNull LiveData<Source> data) {
                sourceData = data;
                observeSource( client.getLifecycleOwner() );
            }
        });

    }

}
