package com.whompum.PennyFlip.Animations;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.TextView;

import com.whompum.PennyFlip.R;

import java.util.ArrayList;

/**
 * Animates Text Views
 * How this works is each Textview "title"
 * will have a String tag associated with it
 * that should be from the Fragments Tag. Then when that page has been swiped
 * to, this class will animate these changes.
 */

public class PageTitleStrips{
    public static final float TEXT_SIZE_CIEL = 16.0F;
    public static final float TEXT_SIZE_FLOOR = 12.0F;
    public static final float TRACK = (TEXT_SIZE_CIEL - TEXT_SIZE_FLOOR);
    
    
    //Map of the titles
    private ArrayList<TextView> strips;

    private int posToGrow = -1;
    private int posToShrink = -1;

    private int currentPos = -1;
    private int lastPos = -1;

    private ViewGroup container;

    private ValueAnimator growthAnimator = new ValueAnimator();
    private ValueAnimator shrinkAnimator = new ValueAnimator();

    public PageTitleStrips(final ViewGroup container){
        this.container = container;
        strips = new ArrayList<>(2);


        growthAnimator.setFloatValues(0F, 1F);
        growthAnimator.setDuration(450L);
        growthAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        growthAnimator.addUpdateListener(growthListener);

        shrinkAnimator.setFloatValues(0F, 1F);
        shrinkAnimator.setDuration(450L);
        shrinkAnimator.setInterpolator(new AnticipateInterpolator());
        shrinkAnimator.addUpdateListener(shrinkListener);
    }

    public void bindTitle(@NonNull final Context context,
                          @NonNull final String title){

        final LayoutInflater inflater = LayoutInflater.from(context);
        final TextView strip = (TextView) inflater.inflate(R.layout.page_title_strip, container, false);
            strip.setText(title);
            strips.add(strip);
            container.addView(strip);

            if(currentPos == -1) {
                currentPos++;
                lastPos++;
                setTextSize((int)TEXT_SIZE_CIEL, strips.get(currentPos));
                setTextStyle(Typeface.BOLD, strips.get(currentPos));
            }else{
                setTextSize((int)TEXT_SIZE_FLOOR, strip);
                setTextStyle(Typeface.NORMAL, strip);
            }

    }




    public void setPosition(final int position){

        /**
         * Position has changed. Shrink the old one, set the new one
         *
         * When the position changes, we grow the new position, and shrink the old;
         *
         * grow(position)
         * shrink(oldPosition)
         *
         */


        grow(position); // two
        shrink(currentPos);// one

        currentPos = position;//Change current position to the newly given position


        Log.i("PAGE", "LAST ITEM = " + String.valueOf(lastPos));
        Log.i("PAGE", "CURRENT ITEM = " + String.valueOf(currentPos));

    }

    private void logPosTitle(final String growingOrShrinking, final int position){
        Log.i("strip", "STRIP: " + growingOrShrinking + " " + strips.get(position).getText().toString());
    }


    private void grow(final int position){
        this.posToGrow = position;
        logPosTitle("grow",posToGrow);
        growthAnimator.start();
    }

    private void shrink(final int position){
        this.posToShrink = position;
        logPosTitle("shrink" ,posToShrink);
        shrinkAnimator.start();
    }


    private final ValueAnimator.AnimatorUpdateListener growthListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {

            final float deltaSize = (TRACK * animation.getAnimatedFraction());
            final int textSize = (int)(TEXT_SIZE_FLOOR + deltaSize);
            setTextSize(textSize, strips.get(posToGrow));

            if(animation.getAnimatedFraction() >= 0.9)
                setTextStyle(Typeface.BOLD, strips.get(posToGrow));

        }
    };

    private final ValueAnimator.AnimatorUpdateListener shrinkListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            final float deltaSize = (TRACK * animation.getAnimatedFraction());
            final int textSize = (int)(TEXT_SIZE_CIEL - deltaSize);
            setTextSize(textSize, strips.get(posToShrink));

            if(animation.getAnimatedFraction() <= 0.1)
                setTextStyle(Typeface.NORMAL, strips.get(posToShrink));
        }
    };


    private void setTextSize(final int textSize, final TextView item){
        item.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }
    private void setTextStyle(int style, final TextView item){
        item.setTypeface(null, style);
    }





}










