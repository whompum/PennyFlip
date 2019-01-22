package com.whompum.PennyFlip.Widgets;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by bryan on 1/16/2018.
 */

public class StickyViewPager extends ViewPager {

    public static final String TAG = StickyViewPager.class.getSimpleName();

    private static final int RIGHT = 1;
    private static final int LEFT = -1;

    private enum BOUNDS {LEFT, RIGHT, XOR}

    private ViewDragHelper dragger;
    private int dragLimit;

    private BOUNDS bounds = null;

    //Will use to sort out multi-touches since this is a single touch environment
    int touchPointer = -1;

    private onStickyDrag OnStickyDrag;

    private boolean dragging = false;

    private SparseArray<View> childrenStart = new SparseArray<>(2);

    public StickyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        dragger = ViewDragHelper.create(this, .5F, draggerCallbacks);
    }


    private final ViewDragHelper.Callback draggerCallbacks = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            resolveDragDirection();


            //If pointerId is from a secondary pointer, then discard
            if(pointerId != StickyViewPager.this.touchPointer)
                return false;


            final View currChild = getChildAt(getCurrentItem());
            dragger.captureChildView(currChild, pointerId);

            /**
             * For future reference, I return false in this method so i can override ViewDragHelpers default behavior
             * of using the parameter "child" as the draging child. It does this because on initial touch I.E. ACTION_DOWN
             * It will find the drag child based on the x/y of the touch, and since a view pager will position its children
             * with values well past the screen dimensions, we'll never be able to find those values using simple
             * X/Y touch values, so to get around that, we return false here, and re-route the ViewDragger to store the curr
             * element of the view pager as its child.
             *
             * TLDR
             * View pager stores children w/ coordinates Less Than OR Greater Than the screens graph,
             * but ViewDragHelper can only find children whose coordinates are within the screens grahps
             * thus the "child" parameter will always be the same child (first child) regardless of what item we're at, since that view
             * has its coordinates within the screen (0 - W / 0 - H)
             *
             *
             */
        return false;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return dragLimit;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {

            int direction = 0;

            int leftBounds = 0;
            int rightBounds = 0;

            int draggedLeft = left;

            if(bounds == BOUNDS.LEFT) {
                leftBounds = getPaddingLeft();
                rightBounds = Math.min((getWidth() - getPaddingRight()), getViewHorizontalDragRange(child));
                direction = RIGHT;
            }


            if(bounds == BOUNDS.RIGHT){
                leftBounds = Math.max(getPaddingStart(), getWidth() - getViewHorizontalDragRange(child) );
                rightBounds = getWidth() - getPaddingEnd();
                direction = LEFT;
            }

            if(draggedLeft < leftBounds)
                draggedLeft = leftBounds;

            else if(draggedLeft > rightBounds)
                draggedLeft = rightBounds;

            final int childrenStartIndex = childrenStart.indexOfValue( child );

            notifySubscriber(direction,
                    Math.max(childrenStart.keyAt( childrenStartIndex ), draggedLeft)
                         - Math.min(childrenStart.keyAt( childrenStartIndex), draggedLeft) );

            return draggedLeft;
        }

        @Override
        public void onViewReleased(@NonNull final View releasedChild, float xvel, float yvel) {

            final int childrenStartIndex = childrenStart.indexOfValue( releasedChild );

            if (bounds == BOUNDS.LEFT)
                dragger.smoothSlideViewTo(releasedChild, childrenStart.keyAt( childrenStartIndex ), releasedChild.getTop());

            invalidate();

        }

        @Override
        public void onViewDragStateChanged(int state) {

            if(state == ViewDragHelper.STATE_DRAGGING)
                dragging = true;
            else if(state == ViewDragHelper.STATE_IDLE)
                dragging = false;

        }
    };


    /**
     * Notifies the subscriber of the drag that a change has happened
     * @param direction Right(1) / Left(-1)
     * @param drag
     */
    private void notifySubscriber(@IntRange(from = LEFT, to = RIGHT) final int direction, final int drag){
        if(OnStickyDrag != null)
            OnStickyDrag.onDrag(drag * direction);
    }

    @Override
    public void computeScroll() {
        if(dragger.continueSettling(true))
            ViewCompat.postInvalidateOnAnimation(this);

        super.computeScroll();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if(dragger.shouldInterceptTouchEvent(ev))
            return true;

        return super.onInterceptTouchEvent(ev);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
            this.touchPointer = event.getPointerId(0);

        dragger.processTouchEvent(event);

        return super.onTouchEvent(event);

    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        captureChildrensLeft();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        captureDragLimit();
    }

    private void captureDragLimit(){
        this.dragLimit = getWidth() / 4;
    }

    /**
     * Captures the left value of the children so they can be reset after the release of the Drag operation
     * Since the Left value is the subject, we use the view itself as a Key
     */
    private void captureChildrensLeft(){
        for(int a = 0; a < getChildCount(); a++)
            this.childrenStart.put(getChildAt(a).getLeft(), getChildAt(a));
    }


    /**
     * Very handy, dandy method that lets a client see if this View is currently dragging its children
     * @return
     */
    public boolean isDragging(){
        return dragging;
    }

    /**
     *Determines the direction a Drag can be in
     * E.G. If we are at the end item, then a sticky drag can only be to the right of the view
     */
    private void resolveDragDirection(){

        final int pos = getCurrentItem();

        final PagerAdapter adapter = getAdapter();

        final boolean ltr = getLayoutDirection() == LAYOUT_DIRECTION_LTR;

        if(pos == 0) {
            if (ltr)
                bounds = BOUNDS.LEFT;
            else
                bounds = BOUNDS.RIGHT;
        }

        else if( adapter != null )
            if (pos == adapter.getCount() - 1) {
                if (ltr)
                    bounds = BOUNDS.RIGHT;
                else
                    bounds = BOUNDS.LEFT;
            }

        if(adapter != null && adapter.getCount() == 0)
            bounds = BOUNDS.XOR; //Can drag either Left, or right;
    }


    public void subscribe(final onStickyDrag client){
        this.OnStickyDrag = client;
    }


    public interface onStickyDrag {
        /**
         * An interface a client will use to receieve Information about a drag
         *
         * Positive values are to the left, and negative are to the left.
         *
         * @param drag The drag in pixels, either positive or negative
         */
        void onDrag(final int drag);
    }



}
