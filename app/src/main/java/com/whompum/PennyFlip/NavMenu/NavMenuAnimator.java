package com.whompum.PennyFlip.NavMenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayDeque;
import java.util.Iterator;

/**
  *
 * His job is reallllly simple. He controls animations for all the child views of the navLayout.
 * How? By creating a tag for every layout based on their position
 *
 * FLOW:
 * Animate each child from bottom views and up, starting @ anchor to their real position on screen.
 * After a specified amount of the animation has completed, open their text bubbles
 * to appear using a fade or a translation
 *
 */

public class NavMenuAnimator {

    public static final String TAG = "NavMenuAnimator";


    private FloatingActionButton anchor;

    private final ArrayDeque<ViewWrapper> containers = new ArrayDeque<>(3);

    private boolean isOpen = false;
    private boolean isInitialized = false;

    public void bindMenu(final ViewGroup navLayout){

        /**
         * Step one, comb backwards through the navLayout, and populate the sets with ViewWrapper objects
         */

        for(int a = navLayout.getChildCount(); a >= 0; a--){
             View child = navLayout.getChildAt(a);

            if(child instanceof FloatingActionButton)
                this.anchor = (FloatingActionButton) child;

            if(child instanceof ViewGroup)
                containers.push(new ViewWrapper(((ViewGroup)child)) );

        }
    }


    public void animate(){

        if(!isInitialized){

            final Iterator<ViewWrapper> iterator = containers.iterator();

            while(iterator.hasNext())
                iterator.next().init(anchor.getY());

            isInitialized = true;
        }

        if(isOpen)
            close();

        else
            open();


    }

    private void open(){

        final Iterator<ViewWrapper> iterator = containers.iterator();

        long delay = 0L;

        while(iterator.hasNext()){
            final ViewWrapper wrapper = iterator.next();
                wrapper.init(anchor.getY());

            wrapper.open(wrapper.openTop, delay);
            delay += 250L;
        }
        isOpen = true;
    }

    private void close(){
        final Iterator<ViewWrapper> iterator = containers.iterator();

        long delay = 500L;

        while(iterator.hasNext()){
            final ViewWrapper wrapper = iterator.next();
                wrapper.close(anchor.getTop(), delay);

            delay -= 250L;
        }
        isOpen = false;
    }



    private class ViewWrapper{

        ViewGroup container;
        View fab;
        ViewGroup cardview;

        int openTop;

        ViewWrapper(final ViewGroup container){
            this.container = container;
            container.post(new Runnable() {
                @Override
                public void run() {
                    openTop = container.getTop();
                    Log.i(TAG, "CONTAINER TOP: " + String.valueOf(openTop));
                }
            });

            for(int a = 0; a < container.getChildCount(); a++){
                final View child = container.getChildAt(a);

                if(child instanceof FloatingActionButton)
                    this.fab = child;

                else if(child instanceof CardView) {
                    this.cardview = ((ViewGroup) child);
                    this.cardview.setAlpha(0F);
                }
            }
        }

        void open(int to, final long delay){
            fab.setVisibility(View.VISIBLE);
            container.animate()
                    .setStartDelay(delay)
                    .setDuration(250L)
                    .y(to)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            cardview.animate()
                                    .alpha(1F)
                                    .setDuration(150L)
                                    .start();
                        }
                    })
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
        }

        void close(int to, final long delay){
            container.animate()
                    .setStartDelay(delay)
                    .setDuration(250L)
                    .y(to)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            cardview.animate()
                                    .alpha(0F)
                                    .setDuration(150L)
                                    .start();
                        }
                    })
                    .start();
        }

        void init(float y){
            container.setY(y);
        }


    }







}








