package com.whompum.PennyFlip.Money.Search;

public class RoomSearchQueryPredicate implements SearchQueryPredicate {

    @Override
    public boolean isTextClean(String query) {
        return ( query != null && query.length() > 0 );
    }
}
