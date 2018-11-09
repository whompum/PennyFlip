package com.whompum.PennyFlip.Money.Source;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.OnCancelledResponder;
import com.whompum.PennyFlip.Money.Queries.Deliverable;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Resolvers.QueryHandler;
import com.whompum.PennyFlip.Money.Queries.Responder;
import com.whompum.PennyFlip.Money.Queries.SourceQueries;
import com.whompum.PennyFlip.Money.Writes.RoomMoneyWriter;

public abstract class NewSourceWriter {

    protected MoneyDatabase database;

    protected SourceQueries queries = new SourceQueries();

    protected final MoneyRequest.QueryBuilder byTitleQuery = new MoneyRequest.QueryBuilder(
            SourceQueryKeys.KEYS
    );

    public NewSourceWriter(@NonNull final MoneyDatabase database) {
        this.database = database;
    }

    public void attemptSave(@NonNull final Source source){

        //Attempt to save the data
        byTitleQuery.setQueryParameter( SourceQueryKeys.TITLE, source.getTitle() );

        //Check if source exists
        final Deliverable<Source> data = queries.queryById( byTitleQuery.getQuery(), database );

        data.attachResponder( new Responder<Source>() {
            @Override
            public void onActionResponse(@NonNull Source data) {
                //Data exists
                //Notify client of failure
                onSourceExists( data );
            }
        } );

        data.attachCancelledResponder( new OnCancelledResponder() {
            @Override
            public void onCancelledResponse(int reason, String msg) {
                //data didn't exists
                //Save source, and notify client of save

                if( reason == QueryHandler.NULL_DATA_QUERY ) {
                    new RoomMoneyWriter(database).saveSource(source); //Save
                    onSourceSaved( source );
                }

            }
        } );


    }


    protected abstract void onSourceExists(@NonNull final Source source);
    protected abstract void onSourceSaved(@NonNull final Source source);

}








