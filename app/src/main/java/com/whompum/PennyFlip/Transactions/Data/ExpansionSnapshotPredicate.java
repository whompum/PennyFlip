package com.whompum.PennyFlip.Transactions.Data;

import java.util.HashMap;

public class ExpansionSnapshotPredicate implements ExpansionPredicate {

    private HashMap<Long, Boolean> expansionState;

    @Override
    public boolean expand(long startOfDay, int position) {

        if( expansionState != null && expansionState.size() > 0 ){

            Boolean expand;

            if( (expand = expansionState.get( startOfDay ) ) != null )
                return expand;

        }

        return expandDefault( startOfDay, position );
    }

    public boolean expandDefault(final long startOfDay, final int position){
        return ( position == 0 ) ;
    }

    public void setExpansionState(HashMap<Long, Boolean> expansionState) {
        this.expansionState = expansionState;
    }

}
