package com.whompum.PennyFlip.Transactions.Adapter.ViewHolder;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionsContent;
import com.whompum.PennyFlip.R;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public class TransactionHolder extends RecyclerView.ViewHolder implements DataBind<TransactionsContent> {

    private TextView transactionLastUpdate;
    private TextView transactionSource;
    private View timelineDot;
    private CurrencyEditText transactionAmount;

    public TransactionHolder(final View layout){
        super(layout);
        transactionLastUpdate = layout.findViewById(R.id.id_global_timestamp);
        transactionSource = layout.findViewById(R.id.id_global_title);
        transactionAmount = layout.findViewById(R.id.id_global_total_display);
        timelineDot = layout.findViewById( R.id.test );
    }

    @Override
    public void bind(@NonNull final TransactionsContent item) {

        final Transaction t = item.getTransaction();

        if( t.getTransactionType() == TransactionType.ADD )
            timelineDot.setBackgroundResource( R.drawable.graphic_timeline_add );

        if( t.getTransactionType() == TransactionType.SPEND )
            timelineDot.setBackgroundResource( R.drawable.graphic_timeline_spend );

        transactionLastUpdate.setText(Timestamp.from(t.getTimestamp()).simpleTime());
        transactionSource.setText(t.getTitle());
        transactionAmount.setText(String.valueOf(t.getAmount()));

        if( Build.VERSION.SDK_INT >= 23 )
            setTextColor( resolveColor( itemView.getContext(), t ) );

        else
            setTextColor( resolveColorPreMarshmallow( itemView.getContext(), t ) );
    }

    private void setTextColor(final int color){
        transactionAmount.setTextColor(color);
    }

    @TargetApi(23)
    private int resolveColor(@NonNull final Context context, @NonNull final Transaction transaction){

        final Resources r = context.getResources();

        if( transaction.getTransactionType() == TransactionType.ADD )
            return r.getColor( R.color.dark_green, null );

        else if( transaction.getTransactionType() == TransactionType.SPEND )
            return r.getColor( R.color.dark_red, null );

        return -1;
    }

    private int resolveColorPreMarshmallow(@NonNull final Context context, @NonNull final Transaction transaction){

        final Resources r = context.getResources();

        if( transaction.getTransactionType() == TransactionType.ADD )
            return r.getColor( R.color.dark_green );
        
        else if( transaction.getTransactionType() == TransactionType.SPEND )
            return r.getColor( R.color.dark_red );

        return -1;
    }
}