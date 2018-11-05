package com.whompum.PennyFlip.Widgets.TransactionTypeIndicator;

class StateCollapsedPredicate extends ContentDeltaPredicate {

    public StateCollapsedPredicate(float capRadius, float viewSize) {
        super(capRadius, viewSize);
    }

    /**
     * Used to `expand` a track based on the current percentage.
     * This class returns increasing values since its meant to be called when
     * coming from a collapsed state
     *
     * @param percentage The percentage of the expansion we're at
     * @return The new width of the track based on the `percentage`
     */
    @Override
    public float computeTrackLength(float percentage) {

        if( percentageWithinDiameter( percentage ) )
            return -1F;

        /*
            Return the float value of `viewSize` from percentage, then offset with the cap diameter
            E.g. if viewSize is 100, percentage is 0.4, and capRadius is 30, we'd want,
            technically, 10% of the viewSize for the new track radius
         */
        return ( viewSize * percentage ) - ( capRadius * 2.0f );
    }

    @Override
    public float computeCapRadius(float percentage) {
        return 0;
    }
}





