package com.whompum.PennyFlip.Transactions.Adapter.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.whompum.PennyFlip.ListUtils.OnItemSelected;
import com.whompum.PennyFlip.Transactions.Header.TransactionsGroup;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionListAdapter;
import com.whompum.PennyFlip.R;

/**
 * Created by bryan on 1/7/2018.
 */

public class TransactionHeaderHolder extends RecyclerView.ViewHolder implements TransactionListAdapter.DataBind<TransactionsGroup> {

    protected AppCompatButton dateDisplay;

    public TransactionHeaderHolder(final View layout,@NonNull final OnItemSelected<Integer> selectedListener){
        super(layout);

        this.dateDisplay = layout.findViewById(R.id.id_global_timestamp);

        dateDisplay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectedListener.onItemSelected(getAdapterPosition());
            }
        });

    }

    @Override
    public void bind(TransactionsGroup headerItem) {
       // dateDisplay.setText(headerItem.getDate());
    }
}