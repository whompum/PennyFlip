package com.whompum.PennyFlip.Transaction;


import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.R;


public class TransactionStickyHeaders extends RecyclerView.ItemDecoration {

    private View header;
    private Rect headerRect = new Rect();


    private StickyData stickyData;

    public TransactionStickyHeaders(final StickyData stickyData){
        this.stickyData = stickyData;
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        if(header == null)
            makeHeader(parent);

        bind(parent.getChildAt(0));

        int deltaY;

        if(isHeader(parent, 1)){

            if(parent.getChildAt(1).getTop() <= headerRect.bottom){
                deltaY = parent.getChildAt(1).getTop();
            }else {
                deltaY = header.getHeight(); //Will draw @ zero REF drawHeader
            }

        }else{
            deltaY = header.getHeight(); //Will draw @ zero REF drawHeader
        }



        drawHeader(c, deltaY);
    }

    /**
     * Checks if the item is a header item or not
     * @return whether item is a header or not.
     */
    private boolean isHeader(final ViewGroup parent, final int parentIndex){
        return stickyData.isItemAHeader(parent.getChildAt(parentIndex));
    }

    private void bind(final View child){
        stickyData.bindHeader(header, child);
    }

    /**
     *
     * @param parent
     */
    private void makeHeader(ViewGroup parent){

        final View header = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.history_list_item_header, parent, false);


        final int parentPaddingHor = parent.getPaddingStart() + parent.getPaddingRight();
        final int parentPaddingVer = parent.getPaddingTop() + parent.getPaddingBottom();


        //Measure specs from the parent for width, and height. Given to the child
        final int parentWidth = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), View.MeasureSpec.EXACTLY);
        final int parentHeight = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredHeight(), View.MeasureSpec.UNSPECIFIED);

        final int childWidth = ViewGroup.getChildMeasureSpec(parentWidth, parentPaddingHor, header.getLayoutParams().width);
        final int childHeight = ViewGroup.getChildMeasureSpec(parentHeight, parentPaddingVer, header.getLayoutParams().height);

        header.measure(childWidth, childHeight);

        headerRect.set(0,0,header.getMeasuredWidth(), header.getMeasuredHeight());
        header.layout(headerRect.left, headerRect.top,
                      headerRect.right, headerRect.bottom);


    this.header = header;
    }

    /**
     * Draw a header, with an optional translation.
     * The deltaY is the new Top value of the new Header.
     *
     *
     * @param canvas The RecyclerViews canvas
     * @param deltaY The Y of the new Header
     */
    private void drawHeader(final Canvas canvas, final int deltaY){
        canvas.save();
        canvas.translate(0, deltaY - header.getHeight());
        header.draw(canvas);
        canvas.restore();
    }


    public interface StickyData {
        boolean isItemAHeader(final View child);
        void bindHeader(final View header, final View child);
    }


}