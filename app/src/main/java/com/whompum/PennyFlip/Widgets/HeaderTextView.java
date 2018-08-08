package com.whompum.PennyFlip.Widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class HeaderTextView extends TextView implements HeaderView {

    public HeaderTextView(Context context) {
        super(context);
    }

    public HeaderTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public HeaderTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
