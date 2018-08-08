package com.whompum.PennyFlip.Widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class HeaderItemLinearLayout extends LinearLayout implements HeaderItemView {

    public HeaderItemLinearLayout(Context context) {
        super(context);
    }

    public HeaderItemLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderItemLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public HeaderItemLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
