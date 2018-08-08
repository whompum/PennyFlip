package com.whompum.PennyFlip.Transactions.Adapter;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Widgets.HeaderItemView;
import com.whompum.PennyFlip.Widgets.HeaderView;

public class TimeLineDecorator extends RecyclerView.ItemDecoration {

    private Paint linePaint = new Paint();
    private Paint dotPaint = new Paint();

    private float lineStart;
    private float spacing;
    private int offsetVer;

    public TimeLineDecorator(@NonNull final Resources resources){
        lineStart = spacing = Math.abs(resources.getDimensionPixelOffset(R.dimen.dimen_padding_hor_large));
        offsetVer = resources.getDimensionPixelOffset(R.dimen.dimen_padding_ver_large);

        linePaint.setStrokeWidth(resources.getDimensionPixelOffset(R.dimen.dimen_timeline_line_width));
        dotPaint.setStrokeWidth(resources.getDimensionPixelOffset(R.dimen.dimen_timeline_circle_width));

        int color;

        if(Build.VERSION.SDK_INT >= 23)
             color = resources.getColor(R.color.light_blue, null);
        else
            color = resources.getColor(R.color.light_blue);

        linePaint.setColor(color);
        dotPaint.setColor(color);
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
            outRect.left = (int)(spacing);

        else if(view instanceof HeaderItemView)
        outRect.left = (int)((spacing*2)+linePaint.getStrokeWidth());


        outRect.top = offsetVer/2;
        outRect.bottom = offsetVer/2;
        outRect.right = (int)spacing;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //Draws the timeline, and the dots

        //From position zero, to the last headerItem object.

        final float halfLineWidth = Math.abs( linePaint.getStrokeWidth() * 0.5F );

        c.drawLine(lineStart+halfLineWidth, 0, lineStart+halfLineWidth, c.getHeight(), linePaint);

        //find all title Views
        for(int i =0; i < parent.getChildCount(); i++){

            //If header item, find the title

            final View dotPositioner = parent.getLayoutManager().getChildAt(i);

            View titleView;

            if(dotPositioner == null ||
                    !(dotPositioner instanceof HeaderItemView)  ||
                    (titleView = dotPositioner.findViewById(R.id.id_global_title)) == null) continue;

            final int cY = dotPositioner.getBottom() - (titleView.getHeight()/2);
            final int cX = (int)(lineStart+halfLineWidth);

            c.drawCircle(cX, cY, dotPaint.getStrokeWidth(), dotPaint);

            //Beer + code works.
        }

    }
}






