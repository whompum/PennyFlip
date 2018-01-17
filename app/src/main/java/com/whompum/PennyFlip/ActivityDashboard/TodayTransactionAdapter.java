package com.whompum.PennyFlip.ActivityDashboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transaction.Models.Transactions;

import java.util.ArrayList;
import java.util.List;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

/**
 * Created by bryan on 1/12/2018.
 */

public class TodayTransactionAdapter extends RecyclerView.Adapter<TodayTransactionAdapter.TodayHolder> {

    private List<Transactions> dataSet = new ArrayList<>();

    private LayoutInflater inflater;

    public TodayTransactionAdapter(final Context context){
        this(context, null);
    }

    public TodayTransactionAdapter(final Context context, final List<Transactions> data){
        this.inflater = LayoutInflater.from(context);
        if(data != null)
            this.dataSet = data;
    }

    public void setDataSet(final List<Transactions> data){
        if(data != null)
            this.dataSet = data;
        
        notifyDataSetChanged();
    }

    @Override
    public TodayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TodayHolder(inflater.inflate(R.layout.layout_dashboard_transaction_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TodayHolder holder, int position) {
        holder.bind(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class TodayHolder extends RecyclerView.ViewHolder{

        private TextView timeStamp;
        private TextView sourceName;
        private CurrencyEditText transactionAmount;

        public TodayHolder(final View layout){
            super(layout);

            timeStamp = layout.findViewById(R.id.id_transaction_timestamp);
            sourceName = layout.findViewById(R.id.id_transaction_source);
            transactionAmount = layout.findViewById(R.id.id_transaction_value);

        }

        public void bind(final Transactions item){
            Log.i("TRANSACTIONS", "bind#TodayHolder");
            timeStamp.setText(item.simpleTime());
            sourceName.setText(item.getSourceName());
            transactionAmount.setText(String.valueOf(item.getTransactionAmount()));
        }

    }

}
