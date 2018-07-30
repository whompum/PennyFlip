package com.whompum.PennyFlip.Transactions.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.whompum.PennyFlip.Transactions.Header.TransactionHeaderItem;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionListAdapter;
import com.whompum.PennyFlip.R;

/**
 * Created by bryan on 1/7/2018.
 */

public class TransactionHeaderHolder extends RecyclerView.ViewHolder implements TransactionListAdapter.DataBind<TransactionHeaderItem> {

    protected TextView dateTextView;
    protected TextView cuantos;

    public TransactionHeaderHolder(final View layout){
        super(layout);

        this.dateTextView = layout.findViewById(R.id.id_history_transaction_header_date);
        this.cuantos = layout.findViewById(R.id.id_history_transaction_header_cuantos);

    }

    @Override
    public void bind(TransactionHeaderItem headerItem) {
        dateTextView.setText(headerItem.getDate());
        cuantos.setText(headerItem.getNumTransactions());
    }
}