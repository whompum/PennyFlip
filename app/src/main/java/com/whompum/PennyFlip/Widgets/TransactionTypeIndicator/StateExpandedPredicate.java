package com.whompum.PennyFlip.Widgets.TransactionTypeIndicator;

class StateExpandedPredicate extends ContentDeltaPredicate {

    public StateExpandedPredicate(float capRadius, float viewSize) {
        super(capRadius, viewSize);
    }

    @Override
    public float computeTrackLength(float percentage) {

        if( percentageWithinDiameter( percentage ) )
            return -1F;

        return viewSize - ( ( viewSize * percentage) - ( capRadius * 2.0F ) );
    }

    @Override
    public float computeCapRadius(float percentage) {
        return 0;
    }
}
