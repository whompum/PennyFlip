package com.whompum.PennyFlip;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.IntRange;
import android.util.AttributeSet;


/**
 * Created by bryan on 12/28/2017.
 */

public class PurseTotalView extends TotalView {

    public static final String NA = "N/A";

    private boolean NAEnabled = false;

    private Path valueRemainingPath = new Path();
    private Paint valueRemainingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public PurseTotalView(final Context context) {
        super(context);
    }
    public PurseTotalView(final Context context, final AttributeSet set) {
        super(context, set);
        init(context.obtainStyledAttributes(set, R.styleable.PurseTotalView));
    }
    public PurseTotalView(final Context context, final AttributeSet set, final int st){
        super(context, set, st);
        init(context.obtainStyledAttributes(set, R.styleable.PurseTotalView));
    }

    private void init(final TypedArray array){

        for(int i =0; i < array.getIndexCount(); i++){

            final int index = array.getIndex(i);

            if(index == R.styleable.PurseTotalView_NAEnabled)
                setNAEnabled(array.getBoolean(index, false));

        }

        setValueTextColor(getColor(R.color.light_red));
        setBackgroundViewColor(getColor(R.color.milk_white));
        setValuePathColor(getColor(R.color.light_red));

        valueRemainingPaint.setColor(getColor(R.color.dark_grey));
        valueRemainingPaint.setStyle(Paint.Style.STROKE);

    }

    public void setNAEnabled(final boolean naEnabled){

        if(naEnabled)
            setValue(0);

        this.NAEnabled = naEnabled;
    }

    public boolean getIsNAEnabled(){
        return NAEnabled;
    }

    @Override
    public int getValue() {

        if(NAEnabled)
            return -1;

        return super.getValue();
    }

    @Override
    public void setValue(@IntRange(from = 0, to = 100) int value) {

        if(NAEnabled)
            return;

        super.setValue(value);
    }

    @Override
    public String getValueAsString() {

        if(NAEnabled)
            return NA;

        return super.getValueAsString();
    }

    @Override
    protected Paint.Style getValuePathPaintStyle() {
        return Paint.Style.STROKE;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(valueRemainingPath, valueRemainingPaint);
    }

    @Override
    protected void generateValuePath(final int width, final int height) {


        //15 is an arbitrary size, but it looks fine @ any view size
        final int strokeWidth = getWidth() / 15;

        //Positions the outter edge of our stroke to the inner edge of the background circle
        final int strokeOffset = strokeWidth / 2;

        valuePathPaint.setStrokeWidth(strokeWidth);
        valueRemainingPaint.setStrokeWidth(strokeWidth);

        valuePathBounds.set(strokeOffset, strokeOffset, width-strokeOffset, height-strokeOffset);
        valuePath.addArc(valuePathBounds, 0, getValueSweepAngle());

        final int valueRemainingPathEndAngle = TOTAL_PERCENTAGE - getValueSweepAngle();

        valueRemainingPath.addArc(valuePathBounds, getValueSweepAngle(), valueRemainingPathEndAngle) ;
    }




}
