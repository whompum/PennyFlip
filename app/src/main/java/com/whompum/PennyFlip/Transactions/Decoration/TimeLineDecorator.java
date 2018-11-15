package com.whompum.PennyFlip.Transactions.Decoration;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionListAdapter;
import com.whompum.PennyFlip.Widgets.HeaderItemView;
import com.whompum.PennyFlip.Widgets.HeaderView;

public class TimeLineDecorator extends RecyclerView.ItemDecoration {

    private Paint linePaint = new Paint();
    private Paint dotPaint = new Paint();

    private float spacing;

    private float lineCX; //The center X of the line; Used to draw and align the dots

    private Rect outRect = new Rect();

    public TimeLineDecorator(@NonNull final Resources resources, @ColorRes final int highlightRes){
        spacing = Math.abs(resources.getDimensionPixelOffset(R.dimen.dimen_padding_hor_large));

        linePaint.setStrokeWidth(resources.getDimensionPixelOffset(R.dimen.dimen_timeline_line_width));
        dotPaint.setStrokeWidth(resources.getDimensionPixelOffset(R.dimen.dimen_timeline_circle_width));

        initializeOutrect(resources.getDimensionPixelOffset(R.dimen.dimen_padding_ver_large));

        lineCX = spacing + Math.abs(linePaint.getStrokeWidth()*0.5F);

        int color;

        if(Build.VERSION.SDK_INT >= 23)
             color = resources.getColor( highlightRes, null );
        else
            color = resources.getColor( highlightRes );

        linePaint.setColor( color );
        dotPaint.setColor( color );
    }

    private void initializeOutrect(final int offsetVer){
        outRect.left = -1;
        outRect.top = offsetVer/2;
        outRect.bottom = offsetVer/2;
        outRect.right = (int)spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        /*
            insets the content to the right. This will put the time-line in the direct middle
            of pixel 0 and contents start.
         */


        //Skip headers. They're offset in their XML
        if(parent.getChildCount() == 0) return;

        else if (view instanceof HeaderView)
            this.outRect.left = (int)(spacing);

        else if(view instanceof HeaderItemView)
            this.outRect.left = (int)((spacing*2)+linePaint.getStrokeWidth());

        outRect.set(this.outRect);

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //Draws the timeline, and the dots

        //From position zero, to the last headerItem object.

        c.drawLine(lineCX, 0, lineCX, //Will draw directly down the middle of lineCX
                getLineHeight(parent), linePaint);

        //find all title Views
        for(int i = 0; i < parent.getChildCount(); i++){

            //If header item, find the title

            final View dotPositioner = parent.getLayoutManager().getChildAt(i);

            View titleView;

            if(dotPositioner == null ||
                    !(dotPositioner instanceof HeaderItemView)  ||
                    (titleView = dotPositioner.findViewById(R.id.id_global_title)) == null ||
                    titleView.getVisibility() == View.GONE) continue;

            final int cY = dotPositioner.getBottom() - (titleView.getHeight()/2);
            final int cX = (int)(lineCX);

            c.drawCircle(cX, cY, dotPaint.getStrokeWidth(), dotPaint);

            //Beer + code works.
        }

    }

    /**
     * Returns the height of the timeline with respect to where we are in the list, and
     * whether data exits
     * @param parent The recyclerView to check againts
     *
     * @return how many pixels to draw for the timelines Height
     */
    private int getLineHeight(@NonNull final RecyclerView parent){

        final RecyclerView.Adapter adapter = parent.getAdapter();

        if(adapter.getItemCount() == 0) //If no data exists
            return 0;

        //Find last adapter index
        final int lastAdapterPosition = adapter.getItemCount()-1;

        final int view_type = adapter.getItemViewType(lastAdapterPosition);

        if(view_type != TransactionListAdapter.DATA && view_type != TransactionListAdapter.HEADER)
            return 0;

        final View lastChild = parent
                .getLayoutManager()
                .findViewByPosition(lastAdapterPosition);

        if(lastChild != null)
            return lastChild.getBottom();

        return parent.getHeight();
    }

}






