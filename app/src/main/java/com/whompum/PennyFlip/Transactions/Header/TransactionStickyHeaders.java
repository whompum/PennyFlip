package com.whompum.PennyFlip.Transactions.Header;


import android.animation.ArgbEvaluator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionListAdapter;


public class TransactionStickyHeaders extends RecyclerView.ItemDecoration {

    private View header;
    private Rect headerRect = new Rect();

    private int bgColor = -1;
    private int sColor = Color.GRAY;

    private ArgbEvaluator evaluator = new ArgbEvaluator();

    private StickyData stickyData;

    public TransactionStickyHeaders(final StickyData stickyData){
        this.stickyData = stickyData;
    }
    public TransactionStickyHeaders(final StickyData stickyData, final int bgColor){
        this.stickyData = stickyData;
        this.bgColor = bgColor;
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        if(parent.getChildCount() < 2) //We can't use headers if there's no data to head...
            return;

        if(header == null)
            makeHeader(parent);
        else
            animateColor(parent);

        bind(parent.getChildAt(0));

        int deltaY;

        sColor = parent.getContext().getResources().getColor(R.color.milk_white);

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


    private void animateColor(final RecyclerView v){

        final TransactionListAdapter adapter = (TransactionListAdapter) v.getAdapter();

        /**
         * Ask the layout manager which view is at the position,
         * relative to the current first visible item, of the next visibly displaying
         * Header item.
         */
        final View localHeader = v.getLayoutManager().findViewByPosition(
            adapter.getNextHeaderItemPos(
                v.getChildAdapterPosition(
                    v.getChildAt(0)
                )
            )
        );

        if(localHeader == null) return;

        final int colorDelta = localHeader.getBottom() - v.getScrollY();

        final int color = (Integer) evaluator.evaluate(colorDelta, bgColor, sColor);
        localHeader.setBackgroundColor(color);
    }



    /**
     *
     * @param parent
     */
    private void makeHeader(ViewGroup parent){

        final View header = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.transaction_item_header, parent, false);

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

        if(bgColor != -1)
            header.setBackgroundColor(bgColor);

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