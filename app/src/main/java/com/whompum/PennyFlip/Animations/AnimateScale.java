package com.whompum.PennyFlip.Animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by bryan on 12/23/2017.
 */

public class AnimateScale {

    private View subject;

    private boolean isShowing = false;

    public AnimateScale(final View subject, final boolean isShowing){
        this.subject = subject; this.isShowing = isShowing;
    }


    public void show(final long duration){

        subject.animate()
                .scaleY(1F)
                .scaleX(1F)
                .setDuration( (duration==0) ? 250L : duration )
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        subject.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    isShowing = true;
    }

    public void hide(final long duration){
        subject.animate()
                .scaleX(0F)
                .scaleY(0F)
                .setDuration( (duration==0) ? 250L : duration )
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        subject.setVisibility(View.INVISIBLE);
                    }
                })
                .start();
        isShowing = false;
    }

    public boolean isShowing(){
        return isShowing;
    }

}
