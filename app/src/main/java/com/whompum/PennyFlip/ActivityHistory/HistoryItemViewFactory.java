package com.whompum.PennyFlip.ActivityHistory;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transactions.Adapter.ViewHolder.HolderFactory;
import com.whompum.PennyFlip.Transactions.Adapter.ViewHolder.TransactionViewHolder;

public class HistoryItemViewFactory implements HolderFactory {

    @Override
    public TransactionViewHolder getHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new HistoryItemViewHolder(
                inflater.inflate( R.layout.history_item_view, parent, false)
        );
    }
}
