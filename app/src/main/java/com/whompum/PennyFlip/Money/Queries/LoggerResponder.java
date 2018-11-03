package com.whompum.PennyFlip.Money.Queries;

import android.util.Log;

import com.whompum.PennyFlip.Money.OnCancelledResponder;

public class LoggerResponder implements OnCancelledResponder {

    private String clsName;

    public LoggerResponder(Class clsName) {
        this.clsName = clsName.getSimpleName();
    }

    @Override
    public void onCancelledResponse(int reason, String msg) {
        if( msg != null )
            Log.e(clsName, msg + "\n CODE: " + reason);
        else Log.e(clsName, "CODE: " + reason);
    }
}
