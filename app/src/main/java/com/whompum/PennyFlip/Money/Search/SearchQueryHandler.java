package com.whompum.PennyFlip.Money.Search;

import android.support.annotation.NonNull;

public abstract class SearchQueryHandler {

    private SearchQueryPredicate helper;

    public SearchQueryHandler(){
        this( new RoomSearchQueryPredicate() );
    }

    public SearchQueryHandler(SearchQueryPredicate helper) {
        this.helper = helper;
    }

    public boolean query(@NonNull final String newText){

        if( !helper.isTextClean( newText  ) )
        return false;

        runQuery( newText );

        return true;
    }

    protected abstract void runQuery(@NonNull final String newText);


}
