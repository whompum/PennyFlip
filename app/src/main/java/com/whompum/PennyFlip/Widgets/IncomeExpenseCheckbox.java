package com.whompum.PennyFlip.Widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.R;


public class IncomeExpenseCheckbox extends LinearLayout implements View.OnClickListener{

    private ImageButton btn;
    private TextView txtDisplay;

    private int type = TransactionType.ADD;

    private IncomeExpenseChangeListener listener;

    public interface IncomeExpenseChangeListener{
        void onChange(final int type);
    }

    public IncomeExpenseCheckbox(Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );

        LayoutInflater.from( context ).inflate( R.layout.layout_income_expense_checkable, this, true );

        btn = findViewById( R.id.id_local_checkable );
        txtDisplay = findViewById( R.id.id_local_label );

        txtDisplay.setText( R.string.string_income );

        btn.setOnClickListener( this );

    }

    @Override
    public void onClick(View v) {

        if( type == TransactionType.ADD )
            type = TransactionType.SPEND;

        else if( type == TransactionType.SPEND )
            type = TransactionType.ADD;

        animateForType();

        if( listener != null )
            listener.onChange( type );

    }

    private void animateForType(){

        final Animation animation = fetchAnimationForType();

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if( type == TransactionType.ADD )
                    txtDisplay.setText( R.string.string_income );

                else if( type == TransactionType.SPEND )
                    txtDisplay.setText( R.string.string_expense );

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        btn.startAnimation(
                animation
        );

    }

    private Animation fetchAnimationForType(){

        if( type == TransactionType.ADD )
            return AnimationUtils.loadAnimation( getContext(), R.anim.rotate_clockwise_0_180);

        else if( type == TransactionType.SPEND )
            return  AnimationUtils.loadAnimation( getContext(), R.anim.rotate_clockwise_180_0);

        return null;
    }

    public void setTypeChangeListener(@NonNull final IncomeExpenseChangeListener l) {
        this.listener = l;
    }
}
