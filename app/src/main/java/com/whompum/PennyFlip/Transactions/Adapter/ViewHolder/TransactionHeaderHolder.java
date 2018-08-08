package com.whompum.PennyFlip.Transactions.Adapter.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.whompum.PennyFlip.ListUtils.OnItemSelected;
import com.whompum.PennyFlip.Time.Ts;
import com.whompum.PennyFlip.DataBind;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionsGroup;
import com.whompum.PennyFlip.R;

/**
 * Created by bryan on 1/7/2018.
 */

public class TransactionHeaderHolder extends RecyclerView.ViewHolder implements
        DataBind<TransactionsGroup>,
        View.OnClickListener{

    protected AppCompatButton dateDisplay;
    private OnItemSelected<Integer> client;
    public TransactionHeaderHolder(final View layout,@NonNull final OnItemSelected<Integer> selectedListener){
        super(layout);

        this.dateDisplay = layout.findViewById(R.id.id_global_timestamp);
        dateDisplay.setOnClickListener(this);
        this.client = selectedListener;
    }

    @Override
    public void bind(TransactionsGroup headerItem) {
        dateDisplay.setText(Ts.from(headerItem.getMillis()).simpleDate());
    }

    @Override
    public void onClick(View v) {
        client.onItemSelected(getAdapterPosition());
    }
}