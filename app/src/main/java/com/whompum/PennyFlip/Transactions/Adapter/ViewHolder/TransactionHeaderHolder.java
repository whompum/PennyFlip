package com.whompum.PennyFlip.Transactions.Adapter.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.whompum.PennyFlip.ListUtils.OnItemSelected;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionsGroup;
import com.whompum.PennyFlip.R;


public class TransactionHeaderHolder extends TransactionViewHolder<TransactionsGroup> implements
        View.OnClickListener{

    protected AppCompatButton dateDisplay;
    private OnItemSelected<Integer> client;

    public TransactionHeaderHolder(final View layout, @NonNull final OnItemSelected<Integer> selectedListener){
        super(layout);

        this.dateDisplay = layout.findViewById(R.id.id_global_timestamp);
        dateDisplay.setOnClickListener(this);
        this.client = selectedListener;
    }

    @Override
    public void bind(TransactionsGroup headerItem) {

        final Timestamp timestamp = Timestamp.from(headerItem.getMillis());

        final int displayRes = timestamp.getStringPreferentialDate();

        if(displayRes == -1)
            dateDisplay.setText(timestamp.simpleDate());

        else if(displayRes == R.string.string_today)
            dateDisplay.setText(itemView.getContext().getString(displayRes));

    }

    @Override
    public void onClick(View v) {
        client.onItemSelected(getAdapterPosition());
    }
}