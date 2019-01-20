package com.whompum.PennyFlip.Widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whompum.PennyFlip.R;

import static com.whompum.PennyFlip.Money.Transaction.TransactionType.EXPENSE;
import static com.whompum.PennyFlip.Money.Transaction.TransactionType.INCOME;


public class IncomeExpenseCheckbox extends LinearLayout implements View.OnClickListener,
Animation.AnimationListener{

    private ImageButton btn;
    private TextView txtDisplay;

    private int type = INCOME;

    private IncomeExpenseChangeListener listener;

    private boolean animInProgress = false;

    public interface IncomeExpenseChangeListener{
        void onTransactionTypeChange(final int type);
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
        toggle();
    }

    public void toggle(){
        if( type == INCOME)
            setType(EXPENSE);

        else if( type == EXPENSE)
            setType(INCOME);

    }

    public void setType(final int newType){

        if( newType == type || animInProgress )
            return;

        type = newType;

        animateForType();

        if( listener != null )
            listener.onTransactionTypeChange( type );

    }

    @Override
    public void onAnimationStart(Animation animation) {
        animInProgress = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        txtDisplay.setText( (type== INCOME) ? R.string.string_income : R.string.string_expense  );
        animInProgress = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) { /*Unused*/ }

    private void animateForType(){

        final Animation animation = fetchAnimationForType();

        animation.setAnimationListener( this );

        btn.startAnimation( animation );

    }

    private Animation fetchAnimationForType(){

        if( type == INCOME)
            return AnimationUtils.loadAnimation( getContext(), R.anim.rotate_clockwise_0_180);

        return  AnimationUtils.loadAnimation( getContext(), R.anim.rotate_clockwise_180_0);
    }

    public int getType() {
        return type;
    }

    public void setTypeChangeListener(@NonNull final IncomeExpenseChangeListener l) {
        this.listener = l;
    }
}
