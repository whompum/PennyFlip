package com.whompum.PennyFlip.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.AttrRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.whompum.PennyFlip.R;

public class TransactionTypeTitleIndicator extends View {

    @AttrRes
    private int highlight = -1;

    private Path indicatorPath = new Path(); //The pathed outline of the track
    private Path capStart = new Path();
    private Path capEnd = new Path();

    private Paint indicatorPaint = new Paint(
            Paint.ANTI_ALIAS_FLAG
    );

    private boolean isExpandedDefault = true;

    public TransactionTypeTitleIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        final TypedArray array = context.obtainStyledAttributes( attrs, R.styleable.TransactionTypeTitleIndicator );

        for( int i = 0; i < array.getIndexCount(); i++ ){

            final int attrIndex = array.getIndex( i );

            if( attrIndex == R.styleable.TransactionTypeTitleIndicator_highlight )
                highlight = array.getColor( attrIndex, -1 );

            if( attrIndex == R.styleable.TransactionTypeTitleIndicator_isExpanded )
                isExpandedDefault = array.getBoolean( attrIndex, true );

        }

        if( highlight == -1 )
            highlight = Color.BLACK;

        indicatorPaint.setColor( highlight );

        indicatorPaint.setStyle( Paint.Style.FILL );

        array.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if( isExpandedDefault )
            computePathBounds( 1.0F );

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath( indicatorPath, indicatorPaint );

        if( !capStart.isEmpty() )
            canvas.drawPath( capStart, indicatorPaint );

        if( !capEnd.isEmpty() )
            canvas.drawPath( capEnd, indicatorPaint );

    }

    public void setPercentage(final float percentage){
        if( percentage < 0.0F || percentage > 1.0F ) return;
        //Should ONLY EVER BE ASCENDING values.

        computePathBounds( percentage );

        invalidate();

    }

    void computePathBounds(final float percentage){

        indicatorPath.reset();
        capEnd.reset();
        capStart.reset();

        final float cX = getWidth() * 0.5F;
        final float cY = getHeight() * 0.5f;


        Log.i("TITLE_INDICATOR", "getWidth(): " + getWidth());
        Log.i("TITLE_INDICATOR", "getHeight(): " + getHeight());

        //How much width our content should take up based on the percentage
        final float visibleContentWidth = getWidth() * percentage;

        if( visibleContentWidth <= getHeight() ) { //If within caps diameter. (caps diameter is the view height)

            indicatorPath.reset();

            indicatorPath.moveTo( cX, cY ); //Middle of content

            indicatorPath.addCircle( cX,    //Put a single circle in the middle of the content
                    cY,
                    visibleContentWidth * 0.5F, //Radius of available width
                    Path.Direction.CW
            );

            indicatorPath.close();

        }else{

           indicatorPath.reset();
           
           //minus the radius of the caps together (diameter)
           final float trackRadius = ( visibleContentWidth - getHeight() ) * 0.5F;

           indicatorPath.moveTo( cX, 0 );
           indicatorPath.lineTo( cX - trackRadius, 0 );
           indicatorPath.lineTo( cX - trackRadius, getHeight() );
           indicatorPath.lineTo( cX + trackRadius, getHeight() );
           indicatorPath.lineTo( cX + trackRadius, 0);

           indicatorPath.close();

           capStart.moveTo( cX - trackRadius, getHeight() * 0.5F );
           capStart.addCircle( cX - trackRadius,
                                    getHeight() * 0.5F,  //cY
                                getHeight() * 0.5F,  //Radius of circle
                                       Path.Direction.CCW );

           capStart.moveTo( cX + trackRadius, getHeight() * 0.5F );
           capEnd.addCircle( cX + trackRadius,
                                     getHeight() * 0.5F, //cY
                                 getHeight() * 0.5F, //Radius of circle
                                        Path.Direction.CW );

        }

    }

}







