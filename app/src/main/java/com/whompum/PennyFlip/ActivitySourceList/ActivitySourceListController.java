package com.whompum.PennyFlip.ActivitySourceList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.whompum.PennyFlip.Money.DatabaseUtils;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Responder;
import com.whompum.PennyFlip.Money.Queries.SourceQueries;
import com.whompum.PennyFlip.Money.Search.RoomSearchQueryHandler;
import com.whompum.PennyFlip.Money.Search.SearchQueryHandler;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Source.NewSourceWriter;
import com.whompum.PennyFlip.Money.Source.SourceSortOrder;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.ActivitySourceList.SourceListControllerClient.QueryOp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class ActivitySourceListController implements ActivitySourceListConsumer, Responder<List<Source>>{

    public static final int DATA_ALREADY_EXISTS = -1000;

    private boolean hasQueried = false;

    private boolean notifyClientOnQuery = false;

    private List<Source> localAddSources = null;
    private List<Source> localSpendSources = null;

    private MoneyDatabase database;

    private SourceListControllerClient client;

    private SourceSortOrder order = new SourceSortOrder( SourceSortOrder.SORT_TOTAL_DESC ); //DEFAULT

    private NewSourceWriter newSourceWriter;

    private SearchQueryHandler searchQueryHandler;

    ActivitySourceListController(@NonNull final Context context,
                                 @NonNull final SourceListControllerClient client){

        this.database = DatabaseUtils.getMoneyDatabase( context );

        this.client = client;

        query();

        searchQueryHandler = new RoomSearchQueryHandler(
            database,
            searchResponder
        );

        newSourceWriter = new NewSourceWriter(database) {
            @Override
            protected void onSourceExists(@NonNull Source source) {
                client.onSaveResult( false, DATA_ALREADY_EXISTS );
            }

            @Override
            protected void onSourceSaved(@NonNull Source source) {
                List<Source> data = null;
                QueryOp op = null;

                if( source.getTransactionType() == TransactionType.ADD ) {
                    data = localAddSources;
                    op = QueryOp.DATA_ADD;
                } else if( source.getTransactionType() == TransactionType.SPEND ){
                    data = localSpendSources;
                    op = QueryOp.DATA_SPEND;
                }

                if( data == null )
                    data = new ArrayList<>();

                    data.add( source );
                    sortData( data, order.resolveSorter() );

                    deliverData( data, op );

                client.onSaveResult( true, null );
            }
        };

    }

    @Override
    public List<Source> querySourceData(int transactionType) {

        if( !hasQueried )
            return null;

        if( transactionType == TransactionType.ADD )
            return localAddSources;

        else if( transactionType == TransactionType.SPEND )
            return localSpendSources;

        return null;
    }

    @Override
    public boolean queryWithSearch(CharSequence query) {
        return searchQueryHandler.query( (String)query );
    }

    @Override
    public void setSourceOrder(@NonNull SourceSortOrder sortOrder) {
        this.order = sortOrder;

        if( localAddSources != null && localAddSources.size() > 0 ){
            sortData( localAddSources, order.resolveSorter() );
            deliverData( localAddSources, QueryOp.DATA_ADD );
        }

        if( localSpendSources != null && localSpendSources.size() > 0 ){
            sortData( localSpendSources, order.resolveSorter() );
            deliverData( localSpendSources, QueryOp.DATA_SPEND );
        }

    }

    @Override
    public void saveSource(@NonNull final Source source) {
        newSourceWriter.attemptSave( source );
    }

    @Override
    public void onActionResponse(@NonNull List<Source> data) {

        hasQueried = true; //Only set here to gaurantee non-null data

        localAddSources = new ArrayList<>();
        localSpendSources = new ArrayList<>();

        final Iterator<Source> i = data.iterator();

        while( i.hasNext() ){

            final Source source = i.next();

            if( source.getTransactionType() == TransactionType.ADD )
                localAddSources.add( source );

            else if( source.getTransactionType() == TransactionType.SPEND )
                localSpendSources.add( source );

        }

        if( localAddSources.size() > 0 )
            sortData( localAddSources, order.resolveSorter() );

        if( localSpendSources.size() > 0 )
            sortData( localSpendSources, order.resolveSorter() );

        if( notifyClientOnQuery )
            notifyClient();

    }

    private void query() {

        final MoneyRequest req = new MoneyRequest.QueryBuilder( new HashSet<Integer>() ).getQuery();

        new SourceQueries().queryGroup( req, database ).attachResponder( this );

    }

    public void scheduleQueriedCallback(){
        this.notifyClientOnQuery = true;
    }

    private void notifyClient(){
        //Only call IF the user has attempted to fetch certain data that wasn't readily available
        client.onDataQueried();
    }

    public boolean hasQueried(){
        return hasQueried;
    }

    private void deliverData(@NonNull final List<Source> data, @NonNull final QueryOp op){
        client.onDataUpdate( data, op );
    }


    private void sortData(@NonNull final List<Source> data, Comparator<Source> comparator){
        Collections.sort( data, comparator );
    }


    private Responder<List<Source>> searchResponder = new Responder<List<Source>>() {
        @Override
        public void onActionResponse(@NonNull List<Source> data) {
            deliverData(data, QueryOp.QUERIED_LIKE_TITLE );
        }
    };

}
