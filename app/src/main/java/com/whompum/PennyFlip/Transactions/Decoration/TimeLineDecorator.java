package com.whompum.PennyFlip.Transactions.Decoration;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionListAdapter;
import com.whompum.PennyFlip.Widgets.HeaderItemView;
import com.whompum.PennyFlip.Widgets.HeaderView;

/**
 * Draws a vertical line straight down the RecyclerView's canvas
 * at the specified position
 */
public class TimeLineDecorator extends RecyclerView.ItemDecoration {

    private Paint linePaint = new Paint();

    private float lineCX; //The center X of the line; Used to draw and align the dots

    private Rect outRect = new Rect();

    private Rect veneer = new Rect();
    private  Paint veneerPaint = new Paint();

    public TimeLineDecorator(@NonNull final Resources resources, @ColorRes final int highlightRes){

        linePaint.setStrokeWidth(resources.getDimensionPixelOffset(R.dimen.dimen_timeline_line_width));

        initializeOutrect(resources.getDimensionPixelOffset(R.dimen.dimen_padding_ver_large));

        lineCX = /*spacing*/ resources.getDimensionPixelOffset(R.dimen.dimen_padding_hor_large) +
                resources.getDimensionPixelOffset(R.dimen.dimen_timeline_inset);

        int color;

        if(Build.VERSION.SDK_INT >= 23)
             color = resources.getColor( highlightRes, null );
        else
            color = resources.getColor( highlightRes );

        linePaint.setColor( color );

    }

    private void initializeOutrect(final int offsetVer){
        //outRect.left = -1;
        outRect.top = offsetVer/2;
        outRect.bottom = offsetVer/2;
        //outRect.right = (int)spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        /*
            insets the content to the right. This will put the time-line in the direct middle
            of pixel 0 and contents start.
         */


        //Skip headers. They're offset in their XML
        if(parent.getChildCount() == 0) return;

        outRect.set(this.outRect);

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //Draws the timeline, and the dots

        //From position zero, to the last headerItem object.

        c.drawRect( veneer, veneerPaint );

        c.drawLine(lineCX, 0, lineCX, //Will draw directly down the middle of lineCX
                getLineHeight(parent), linePaint);
        
    }

    /**
     * Returns the height of the timeline with respect to where we are in the list, and
     * whether data exits
     * @param parent The recyclerView to check againts
     *
     * @return how many pixels to draw for the timelines Height
     */
    private int getLineHeight(@NonNull final RecyclerView parent){
        
        return parent.getHeight();
    }


}






