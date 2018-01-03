package com.whompum.PennyFlip.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.whompum.PennyFlip.R;

/**
 * Created by bryan on 12/30/2017.
 */

public class CircleTotalView extends TotalView {

    public CircleTotalView(final Context context){
        super(context);
    }

    public CircleTotalView(final Context context, final AttributeSet set){
        super(context, set);
        init(context.obtainStyledAttributes(set, R.styleable.CircleTotalView));
    }

    public CircleTotalView(final Context context, final AttributeSet set, final int de){
        super(context, set, de);
        init(context.obtainStyledAttributes(set, R.styleable.CircleTotalView));
    }


    private void init(final TypedArray array){

        //used as default values; If attrs specified a color, then these will be overriden
        valuePathPaint.setColor(getColor(R.color.light_blue)); //Make an attribute value soon
        setBackgroundViewColor(getColor(R.color.light_green));

        for(int i =0; i < array.getIndexCount(); i++){
            final int index = array.getIndex(i);

            if(index == R.styleable.CircleTotalView_circleTotalViewValueColor)
                setBackgroundViewColor(array.getColor(index, getColor(R.color.light_green)));

            if(index == R.styleable.CircleTotalView_circleTotalViewBackgroundColor)
                valuePathPaint.setColor(array.getColor(index, getColor(R.color.light_blue)));
        }

        setValueTextColor(getColor(R.color.white));

    }


    @Override
    protected Paint.Style getValuePathPaintStyle() {
        return Paint.Style.FILL;
    }

    @Override
    protected void generateValuePath(int width, int height) {

        valuePath.reset();

        valuePathBounds.set(0.0F, 0.0F, width, height);

        /**
         * PASO NUMERO UNO (STRAIGHT LINES)
         * Move to 0 DEGREES (Translated from Cartesian coordinates)
         *
         * PASO NUMERO DOS
         * Line to center X/Y
         *
         * PASO STEP NUMERO TRES
         * Line to SweepAngle (Translated fro Cartesian coordinates)
         *
         * PASO NUMERO CUATROS
         * CLOSE PATH
         *
         * PASO NUMERO CINCO
         * Profit??
         *
         **/




        final float radius = (float)getSize() * 0.5F;

        final float sX = polarX(radius, radius, 0F);
        final float sY = polarY(radius, radius, 0F);

        final float eX = polarX(radius, radius, (float)(Math.toRadians(getValueSweepAngle())));
        final float eY = polarY(radius, radius, (float)(Math.toRadians(getValueSweepAngle())));

        final int sDeg = getValueSweepAngle();
        final int eDeg = TOTAL_PERCENTAGE - getValueSweepAngle();

        humanReadImportantNotePls();

        //STEP UNO
        valuePath.moveTo(sX, sY);

        //STEP DOS
        valuePath.lineTo(radius, radius);

        //STEP TRES
        valuePath.lineTo(eX, eY);

        //STEP CUATROS
        valuePath.addArc(valuePathBounds, sDeg, eDeg);
    }


    private void humanReadImportantNotePls(){
        /**
         * Yes this is a method created enitrely for the purpose of storing this note.
         * What needs to be noted is that the way this view works; Rather than showing
         * the pie piece for the value, the value will show the remaining pie piece for itself.
         * E.G. if the value is at 25%, then the other 75% will be shown by the value path.
         * This is has to be done because of the Paint styling being set to FILL will make the more intuitive
         * way of creating the arc (addArc(valuePathBounds, 0, getSweepAngle()) make a straight line
         * from getSweepAngle() to 0 instead of the wanted pie piece. Solutions for that are too much for
         * too little gain when i can just use this work around:
         * Pretend the background represents the value, and the value path color represents the background
         * then set colors respectively.
         *
         * This is abstracted away in the init() method, but nonetheless is something to note.
         *
         */
    }

    private static float polarX(float start, float howFar, float direction){
        return (start + howFar * (float)Math.cos(direction));
    }

    private static float polarY(float start, float howFar, float direction) {
        return (start + howFar * (float) Math.sin(direction));
    }


}











