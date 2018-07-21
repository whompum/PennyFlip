package com.whompum.PennyFlip;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnticipateInterpolator;

import com.whompum.pennydialog.dialog.PennyDialog;

/**
 * Created by bryan on 12/25/2017.
 */

public class SlidePennyDialog extends PennyDialog {


    public static final String TAG = "SlidePennyDialog";
    public static final long SLIDE_DUR = 500L;

    private int slideX; //How far to translate the left side when dismissing (should be a negative value)

    public static PennyDialog newInstance(@Nullable final Bundle args){
        final PennyDialog pennyDialog = new SlidePennyDialog();
        pennyDialog.setArguments(args);

        return pennyDialog;
    }

    public static PennyDialog newInstance(final CashChangeListener cashChangeListener){
        final PennyDialog pennyDialog = new SlidePennyDialog();
        pennyDialog.setCashChangeListener(cashChangeListener);
        return pennyDialog;
    }


    public static PennyDialog newInstance(final CashChangeListener cashChangeListener, final Bundle args){
        final PennyDialog pennyDialog = new SlidePennyDialog();
        pennyDialog.setArguments(args);
        pennyDialog.setCashChangeListener(cashChangeListener);

        return pennyDialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getView() != null)
            slideX = 0 - getView().getWidth();
        else
            slideX = -1500; //Arbitrary but should be big enough for most dialog widths to dissapear completely.
    }

    @Override
    public void onDone() {

        final View decorView = getDialog().getWindow().getDecorView();

        if(decorView!=null){

            decorView.animate().x(slideX).setInterpolator(new AnticipateInterpolator()).setDuration(SLIDE_DUR).start();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SlidePennyDialog.super.onDone();
                }
            }, SLIDE_DUR);
        }else
            super.onDone();
    }

}
