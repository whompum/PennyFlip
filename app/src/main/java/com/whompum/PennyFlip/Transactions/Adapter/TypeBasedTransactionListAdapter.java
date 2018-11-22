package com.whompum.PennyFlip.Transactions.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.R;

import java.util.HashMap;


public class TypeBasedTransactionListAdapter extends TransactionListAdapter {

    private int transactionType;

    public TypeBasedTransactionListAdapter(final int transactionType) {
        super();
    }

    public TypeBasedTransactionListAdapter(final int transactionType,
                                           @NonNull final HashMap<Long, Boolean> snapshot) {
        super( snapshot );

        this.transactionType = transactionType;

    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        final RecyclerView.ViewHolder holder = super.getHeaderViewHolder( inflater, parent );

        if( transactionType == TransactionType.ADD )
            holder.itemView.setBackgroundResource( R.drawable.background_rounded_rect_right_green );

        else if( transactionType == TransactionType.SPEND )
            holder.itemView.setBackgroundResource( R.drawable.background_rounded_rect_right_red );

        return holder;

    }
}
