package com.whompum.PennyFlip.Transactions.Adapter.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public interface HolderFactory {
    TransactionViewHolder getHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent);
}
