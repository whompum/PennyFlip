package com.whompum.PennyFlip.Animations;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by bryan on 12/23/2017.
 */

public class AnimateScale {

    private View subject;

    public AnimateScale(final View subject){
        this.subject = subject;
    }


    public void show(final long duration){

        subject.animate()
                .scaleY(1F)
                .scaleX(1F)
                .setDuration( (duration==0) ? 250L : duration )
                .start();

    }

    public void hide(final long duration){
        subject.animate()
                .scaleX(0F)
                .scaleY(0F)
                .setDuration( (duration==0) ? 250L : duration )
                .start();
    }

}
