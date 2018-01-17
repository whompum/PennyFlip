package com.whompum.PennyFlip.ActivityDashboard;

import android.view.View;

/**
 * Created by bryan on 1/12/2018.
 */

public class SimpleLayoutChange implements View.OnLayoutChangeListener {
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        onLayoutChange(top);
    }

    public void onLayoutChange(final int top){

    }

}
