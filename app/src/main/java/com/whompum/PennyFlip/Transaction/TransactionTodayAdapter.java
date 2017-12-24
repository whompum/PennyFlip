package com.whompum.PennyFlip.Transaction;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.PennyFlip.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

/**
 * Created by bryan on 12/23/2017.
 */

public class TransactionTodayAdapter extends RecyclerView.Adapter<TransactionTodayAdapter.TransactionViewCache> {

    @LayoutRes
    private final int LAYOUT = R.layout.layout_item_list_today;

    private Stack<Transactions> transactionsList = null;

    private LayoutInflater inflater = null;


    public TransactionTodayAdapter(final Context context){
        this(context, null);
    }

    public TransactionTodayAdapter(final Context context, final Stack<Transactions> dataSet){

        if(dataSet!=null)
            transactionsList = new Stack<>();
        else
            transactionsList = new Stack<>();

        inflater = LayoutInflater.from(context);
    }

    public void insert(@NonNull final Transactions transactions){
        transactionsList.push(transactions);
        notifyDataSetChanged();
    }

    @Override
    public TransactionViewCache onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TransactionViewCache(inflater.inflate(LAYOUT, parent, false));
    }

    @Override
    public void onBindViewHolder(TransactionViewCache holder, int position) {
        final Transactions trans = transactionsList.get(position);

        holder.timeView.setText(trans.simpleTime());
        holder.amount.setText(Long.toString(trans.getAmount()));
        holder.source.setText(trans.getName());
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    public class TransactionViewCache extends RecyclerView.ViewHolder{

        private TextView timeView;
        private CurrencyEditText amount;
        private TextView source;

        TransactionViewCache(final View layout){
            super(layout);
            timeView = layout.findViewById(R.id.id_dashboard_transactionlist_timestamp);
            amount = layout.findViewById(R.id.id_dashboard_transactionlist_total);
            source = layout.findViewById(R.id.id_dashboard_transactionlist_source);
        }

    }

}
