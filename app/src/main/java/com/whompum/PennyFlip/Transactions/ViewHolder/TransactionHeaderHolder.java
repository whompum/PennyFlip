package com.whompum.PennyFlip.Transactions.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.whompum.PennyFlip.Transactions.Models.TransactionHeaderItem;
import com.whompum.PennyFlip.Transactions.TransactionListAdapter;
import com.whompum.PennyFlip.R;

/**
 * Created by bryan on 1/7/2018.
 */

public class TransactionHeaderHolder extends RecyclerView.ViewHolder implements TransactionListAdapter.DataBind<TransactionHeaderItem> {

    private TextView dateTextView;
    private TextView cuantos;

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