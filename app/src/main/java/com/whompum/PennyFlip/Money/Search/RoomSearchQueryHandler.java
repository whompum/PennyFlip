package com.whompum.PennyFlip.Money.Search;

import android.support.annotation.NonNull;
import android.util.Log;

import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Responder;
import com.whompum.PennyFlip.Money.Queries.SourceQueries;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Source.SourceQueryKeys;

import java.util.List;

public class RoomSearchQueryHandler extends SearchQueryHandler implements Responder<List<Source>>{

    private MoneyDatabase database;

    protected SourceQueries queries = new SourceQueries();

    private Responder<List<Source>> clientResponder;

    protected MoneyRequest.QueryBuilder builder = new MoneyRequest.QueryBuilder(
            SourceQueryKeys.KEYS
    );

    public RoomSearchQueryHandler(MoneyDatabase database) {
        super();
        this.database = database;
    }

    public RoomSearchQueryHandler(@NonNull final MoneyDatabase database,
                                  @NonNull final Responder<List<Source>> responder){

        this( database );
        this.clientResponder = responder;
    }


    @Override
    protected void runQuery(@NonNull String newText) {

        Log.w(RoomSearchQueryHandler.class.getSimpleName(), "Calling setQueryParameter() creates instantiates new" +
                "objects each time it is called");

        builder.setQueryParameter( SourceQueryKeys.LIKE_TITLE, "%" + newText + "%" );

        queries.queryGroup( builder.getQuery(), database ).attachResponder(
                ( clientResponder != null ) ? clientResponder : this
        );
    }

    @Override
    public void onActionResponse(@NonNull List<Source> data) {
        //STUB method meant to be overriden
    }
}









