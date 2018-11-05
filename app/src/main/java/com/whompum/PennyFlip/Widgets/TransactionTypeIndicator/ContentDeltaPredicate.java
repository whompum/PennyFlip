package com.whompum.PennyFlip.Widgets.TransactionTypeIndicator;

public abstract class ContentDeltaPredicate {

    protected float capRadius;
    protected float viewSize;

    public ContentDeltaPredicate(float capRadius, float viewSize) {
        this.capRadius = capRadius;
        this.viewSize = viewSize;
    }

    public abstract float computeTrackLength(final float percentage);

    public abstract float computeCapRadius(final float percentage);

    public boolean percentageWithinDiameter(final float percentage){
        return  ( viewSize * percentage ) <= ( capRadius * 2.0F );
    }

}
