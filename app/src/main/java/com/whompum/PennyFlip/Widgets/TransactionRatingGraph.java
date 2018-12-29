package com.whompum.PennyFlip.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.whompum.PennyFlip.R;

public class TransactionRatingGraph extends View {

    public static final int DAY_COUNT_MAX = 31;

    public static final int DAY_COUNT_UNKNOWN = 0;

    static Paint SHARED_COLOR_PAINT = new Paint( Paint.ANTI_ALIAS_FLAG );

    static final int STATE_HIDDEN = -1;
    static final int STATE_OFF = 0;
    static final int STATE_ON = 1;

    public static final int RATING_NA = -1;  //En futuro
    public static final int RATING_NONE = 0; //Day is present / past and no rating for this day today
    public static final int RATING_LOWEST = 1; //Worst Rating
    public static final int RATING_POOR = 2;
    public static final int RATING_NORMAL = 3;
    public static final int RATING_GOOD = 4;
    public static final int RATING_BEST = 5;   //Highest Rating

    @IntDef({ STATE_HIDDEN, STATE_OFF, STATE_ON })
    @interface CellStates{}

    @IntDef({ RATING_NONE, RATING_NA, RATING_LOWEST, RATING_POOR, RATING_NORMAL, RATING_GOOD, RATING_BEST })
    @interface Rating{}

    @ColorInt
    @AttrRes
    private int dotColorOn = Color.WHITE;

    @ColorInt
    @AttrRes
    private int dotColorOff = Color.DKGRAY;

    @ColorInt
    final int dotColorHidden = Color.TRANSPARENT;

    @Dimension
    @AttrRes
    private int dotSize;

    @Dimension
    @AttrRes
    private int dotPadding;

    private boolean invert = false;

    private int dayCount = DAY_COUNT_UNKNOWN;

    private SparseArray<Row> days = new SparseArray<>(); //Each day is a key value

    public TransactionRatingGraph(Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );

        final TypedArray array = context.obtainStyledAttributes( attrs, R.styleable.TransactionRatingGraph );

        init( array );

        array.recycle();
    }

    public TransactionRatingGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr ); //use custom style attr to resolve width/height
    }

    private void init(@NonNull final TypedArray typedArray){

        for( int i = 0; i < typedArray.getIndexCount(); i++ ){

            final int index = typedArray.getIndex( i );

            if( index == R.styleable.TransactionRatingGraph_dotSize )
                dotSize = typedArray.getDimensionPixelSize( index, 11 );

            if( index == R.styleable.TransactionRatingGraph_dotPadding )
                dotPadding = typedArray.getDimensionPixelSize( index, 2 );

            if( index == R.styleable.TransactionRatingGraph_dotColorOn )
                dotColorOn = typedArray.getColor( index, dotColorOn );

            if( index == R.styleable.TransactionRatingGraph_dotColorOff )
                dotColorOff = typedArray.getColor( index, dotColorOff );

            if( index == R.styleable.TransactionRatingGraph_invert )
                invert = typedArray.getBoolean( index, false );

        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int width = ( dayCount == DAY_COUNT_UNKNOWN ) ? 0 : dayCount * dotSize;
        final int height = ( dayCount == DAY_COUNT_UNKNOWN ) ? 0 : 5 * dotSize; //5 is the max # of dots vertically.

        setMeasuredDimension( width+getPaddingHor(), height+getPaddingVer() );
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        //no call to super.onLayout() needed since we have no children.

        if( dayCount != DAY_COUNT_UNKNOWN ) {
            //scale back by the dot radius, so we can simply += the dot size to get to the new cX position
            int rowCx = getPaddingStart() - ( dotSize >> 1 );

            for ( int i = 1; i <= dayCount; i++ )
                days.get( i ).setXPos( rowCx += dotSize, getPaddingTop() );
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if( dayCount != DAY_COUNT_UNKNOWN )
            for( int i = 1; i <= dayCount; i++ )
                days.get( i ).draw( canvas );
    }

    public int getPaddingHor(){
        return getPaddingStart() + getPaddingEnd();
    }

    public int getPaddingVer(){
        return getPaddingTop() + getPaddingBottom();
    }


    public void setDayCount(@IntRange(from = 0, to = DAY_COUNT_MAX ) final int dayCount){

        if( dayCount > DAY_COUNT_MAX )
            throw new IllegalArgumentException("There isn't that many days in a month :/");

        this.dayCount = ( dayCount <= DAY_COUNT_UNKNOWN ) ? DAY_COUNT_UNKNOWN : dayCount;

        for( int i = 1; i <= dayCount; i++ )
            days.put( i, new Row( dotSize, dotPadding, invert ) );

        invalidate();
    }

    public void setRating(final int day, @Rating final int rating){
        setRating( day, rating, true );
    }

    private void setRating(final int day, @Rating final int rating, final boolean shouldInvalidate){

        if( day > days.size() || days.size() == 0)
            return;

        days.get( day ).setRating( rating );

        if( shouldInvalidate )
            invalidate();
    }

    public void setRatings(@NonNull final int[] days, @NonNull final int[] rating /*RATING VALUES*/){

        if( days.length != rating.length )
            throw new IllegalArgumentException("Rating and Day index are respective to one another");

        for( int i = 0; i < days.length; i++ )
            setRating( days[i], rating[i], false );

        invalidate();
    }

    private static final class Row{

        private int xPos;
        private int dotSize;
        private boolean invert;
        private final Cell[] cells = new Cell[5];

        Row(final int dotSize, final int dotPadding, final boolean invert){

            this.dotSize = dotSize;

            for( int i = 0; i < 5; i++ )
                cells[i] = new Cell( dotSize, dotPadding, STATE_OFF );

            this.invert = invert;
        }

        void setRating(@IntRange(from = -1, to = 5) final int rating){

            if( rating >= 0 ) //Clear ratings. Or, set to hidden if none have occured for this day.
                for( Cell c: cells )
                    c.setState( STATE_HIDDEN );

            else if( rating == -1 ) //The day is in the future.
                for( Cell c: cells )
                    c.setState( STATE_OFF );

            if( rating > 0 )
                turnOnCells( (invert) ? cells.length-1 : 0, (invert) ? (cells.length-1)-rating : rating );

        }

        private void turnOnCells( int currCell, final int cellCiel){

            if( currCell == cellCiel )
                return;

            cells[currCell].setState( STATE_ON );

            turnOnCells( (invert) ? currCell-1 : currCell+1, cellCiel );
        }

        void setXPos(final int xPos){
           setXPos( xPos, 0 );
        }

        void setXPos(final int xPos, final int baseline){
            this.xPos = xPos;

            int cellCy = baseline - ( dotSize >> 1 );

            for( Cell c: cells )
                c.yPos = cellCy += dotSize;
        }

        void draw(@NonNull final Canvas canvas){
            for( Cell c: cells )
                c.draw( canvas, xPos );
        }
        
    }

    private static final class Cell{

        float yPos;
        float size;
        float padding;

        @ColorInt int dotClr;

        Cell(final int size, final int padding, @CellStates final int state){
            this.size = size;
            this.padding = padding;
            setState( state );
        }

        void setState(@CellStates final int state){
            switch( state ){
                case STATE_ON: dotClr = Color.WHITE; break;
                case STATE_OFF: dotClr = Color.DKGRAY; break;
                case STATE_HIDDEN: dotClr = Color.TRANSPARENT;
                default: break;
            }
        }

        void draw(@NonNull final Canvas canvas, final int xPos){
            SHARED_COLOR_PAINT.setColor( dotClr );
            canvas.drawCircle( xPos, yPos, getCircleRadius(), SHARED_COLOR_PAINT );
        }

        private float getCircleRadius(){
            return ( size * 0.5F ) - padding;
        }


    }

}
