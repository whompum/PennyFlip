package com.whompum.PennyFlip.ActivitySourceList.Fragments;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SourceListMarginDecorator extends RecyclerView.ItemDecoration {

    private int margin;

    public SourceListMarginDecorator(int margin) {
        this.margin = margin * 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if( parent.getChildAdapterPosition( view ) == 0 )
            outRect.top = margin * 2;

        outRect.bottom = margin;
    }
}
