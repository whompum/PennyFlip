package com.whompum.PennyFlip.Widgets;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.R;

public class TransactionGraphContainer extends LinearLayout {

    public static final int LAYOUT_ID = R.layout.statistics_graph_layout;

    private SparseArray<TransactionRatingGraph> graphs = new SparseArray<>( 2 );

    public TransactionGraphContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init( context );
    }

    private void init(@NonNull final Context context){

        setOrientation( VERTICAL );

        final LayoutInflater inflater = LayoutInflater.from( context );

        inflater.inflate( LAYOUT_ID, this, true );

        graphs.put(
                TransactionType.INCOME,
                (TransactionRatingGraph) findViewById( R.id.id_stat_graph_income )
        );

        graphs.put(
                TransactionType.EXPENSE,
                (TransactionRatingGraph) findViewById( R.id.id_stat_graph_expense )
        );

        setIncomeDayCount( 30 );
        setExpenseDayCount( 30 );

    }

    public void setDayCount(@IntRange(from = TransactionType.INCOME, to = TransactionType.EXPENSE) final int type,
                            @IntRange(from = 1, to = TransactionRatingGraph.DAY_COUNT_MAX) final int dayCount){

        graphs.get( type )
                .setDayCount( dayCount );

    }

    public void setIncomeDayCount(final int dayCount){
        setDayCount( TransactionType.INCOME, dayCount );
    }

    public void setExpenseDayCount(final int dayCount){
        setDayCount( TransactionType.EXPENSE, dayCount );
    }

    public void setIncomeRating(@NonNull  final int[] days, @NonNull final int[] ratings){

    }

    public void setExpenseRating(@NonNull final int days, @NonNull final int ratings){

    }


}
