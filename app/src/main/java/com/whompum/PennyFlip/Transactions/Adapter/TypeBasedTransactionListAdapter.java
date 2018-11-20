package com.whompum.PennyFlip.Transactions.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.ListUtils.AdapterItem;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.R;

import java.util.List;

public class TypeBasedTransactionListAdapter extends TransactionListAdapter {

    private int transactionType;

    public TypeBasedTransactionListAdapter(final int transactionType) {
        this( null, transactionType );
    }

    public TypeBasedTransactionListAdapter(List<AdapterItem> list, final int transactionType) {
        super( list );

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
