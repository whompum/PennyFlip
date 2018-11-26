package com.whompum.PennyFlip.Transactions.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class TransactionViewHolder<T> extends RecyclerView.ViewHolder implements DataBind<T> {

    public TransactionViewHolder(View itemView) {
        super(itemView);
    }

}
