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

import static com.whompum.PennyFlip.Money.Transaction.TransactionType.ADD;
import static com.whompum.PennyFlip.Money.Transaction.TransactionType.SPEND;


public class IncomeExpenseCheckbox extends LinearLayout implements View.OnClickListener,
Animation.AnimationListener{

    private ImageButton btn;
    private TextView txtDisplay;

<<<<<<< HEAD
    private int type = TransactionType.INCOME;
=======
    private int type = ADD;
>>>>>>> statistics

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

<<<<<<< HEAD
        if( type == TransactionType.INCOME)
            type = TransactionType.EXPENSE;

        else if( type == TransactionType.EXPENSE)
            type = TransactionType.INCOME;
=======
    public void toggle(){
        if( type == ADD )
            setType( SPEND );

        else if( type == SPEND )
            setType( ADD );
>>>>>>> statistics

    }

    public void setType(final int newType){

        if( newType == type || animInProgress )
            return;

        type = newType;

        animateForType();

        if( listener != null )
            listener.onTransactionTypeChange( type );

    }

<<<<<<< HEAD
            @Override
            public void onAnimationEnd(Animation animation) {
                if( type == TransactionType.INCOME)
                    txtDisplay.setText( R.string.string_income );

                else if( type == TransactionType.EXPENSE)
                    txtDisplay.setText( R.string.string_expense );
=======
    @Override
    public void onAnimationStart(Animation animation) {
        animInProgress = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        txtDisplay.setText( (type==ADD) ? R.string.string_income : R.string.string_expense  );
        animInProgress = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) { /*Unused*/ }
>>>>>>> statistics

    private void animateForType(){

        final Animation animation = fetchAnimationForType();

        animation.setAnimationListener( this );

        btn.startAnimation( animation );

    }

    private Animation fetchAnimationForType(){

<<<<<<< HEAD
        if( type == TransactionType.INCOME)
            return AnimationUtils.loadAnimation( getContext(), R.anim.rotate_clockwise_0_180);

        else if( type == TransactionType.EXPENSE)
            return  AnimationUtils.loadAnimation( getContext(), R.anim.rotate_clockwise_180_0);
=======
        if( type == ADD )
            return AnimationUtils.loadAnimation( getContext(), R.anim.rotate_clockwise_0_180);

        return  AnimationUtils.loadAnimation( getContext(), R.anim.rotate_clockwise_180_0);
    }
>>>>>>> statistics

    public int getType() {
        return type;
    }

    public void setTypeChangeListener(@NonNull final IncomeExpenseChangeListener l) {
        this.listener = l;
    }
}
