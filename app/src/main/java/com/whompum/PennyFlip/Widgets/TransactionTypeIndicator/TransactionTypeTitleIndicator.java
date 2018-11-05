package com.whompum.PennyFlip.Widgets.TransactionTypeIndicator;

import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.AttrRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

public class TransactionTypeTitleIndicator {

    public static final int EXPANDED = 0;
    public static final int COLLAPSED = 1;

    @AttrRes
    @IntRange(from = EXPANDED, to = COLLAPSED)
    private int currentState = EXPANDED; //Whether its expanded or collapsed

    @AttrRes
    private int highlight = -1;

    //Literal width of the track; Since this view will only use one path object
    //When expanded, it will be the full distance of the path
    //minus the radius of the paths ends ( semi-circles )

    private ContentDeltaPredicate predicate;

    //STATIC once set
    private float expandedTrackWidth = 0F;    //Literal width of the track

    //STATIC once set
    private float expandedCapRadius = 0F;    //The radius of the caps

    private float currentTrackWidth = 0F;
    private float currentCapRadius = 0F;

    private Path indicatorPath = new Path(); //The pathed outline of the track

    private Paint indicatorPaint = new Paint(
            Paint.ANTI_ALIAS_FLAG
    );


    public void setPercentage(final float percentage){
        if( percentage < 0.0F || percentage > 1.0F ) return;
        //Should ONLY EVER BE ASCENDING values.

        if( predicate.percentageWithinDiameter( percentage ) )
            updateCaps( computeNewCapRadius( predicate, percentage ) );

        else
            updateTrackWidth( computeNewTrackWidth( predicate, percentage ) );

    }

    /**
     * Determines the new width of the track based on the percentage
     * and currentState
     *
     * @return the newly computed track width
     */
    private float computeNewTrackWidth(@NonNull final ContentDeltaPredicate predicate, final float percentage){
        return predicate.computeTrackLength( percentage );
    }

    private float computeNewCapRadius(@NonNull final ContentDeltaPredicate predicate, final float percentage){
        return predicate.computeCapRadius( percentage );
    }

    private void updateTrackWidth(final float newWidth){
        currentTrackWidth = newWidth;
        update( );
    }

    private void updateCaps(final float newRadius){
        currentCapRadius = newRadius;
    }

    private void update(){

        //Set the distance of the path from each other.

        indicatorPath.reset();

        //Resolved via newWidth. e.g. (left = centerX - newWidth/2)
        //These values are also of the track only.
        final float centerX /* = getWidth() * 0.5F; */ ;
        final float centerY /* = getHeight() * 0.5F; */ ;

        final float left = ( centerX - ( currentTrackWidth * 0.5F ) );
        final float right = ( centerX + ( currentTrackWidth * 0.5F ) );


        indicatorPath.moveTo( left, 0 );
        indicatorPath.lineTo( right, 0 );
        indicatorPath.lineTo( right, 0 /* getHeight() */ );
        indicatorPath.lineTo( left, 0 /* getHeight() */ );

        indicatorPath.close();

        indicatorPath.addCircle( left, centerY, currentCapRadius, Path.Direction.CCW );
        indicatorPath.addCircle( right, centerY, currentCapRadius, Path.Direction.CW );

        indicatorPath.close();

        /* invalidate() */
    }


    /*
        The view will shrink its track, then radius to close its.  Kinda like its imploding.

        After the center is shrunk, and only a circle remains, it will continue shrinking
        until gone. The radius of the circle will shrink. After fully, shrinking it will set an internal
        flag of "Collapsed"
     */

}







