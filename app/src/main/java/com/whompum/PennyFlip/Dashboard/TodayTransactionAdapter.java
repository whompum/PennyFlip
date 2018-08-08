package com.whompum.PennyFlip.Dashboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.Ts;

import java.util.ArrayList;
import java.util.List;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

/**
 * Created by bryan on 1/12/2018.
 */

public class TodayTransactionAdapter extends RecyclerView.Adapter<TodayTransactionAdapter.TodayHolder> {

    private List<Transaction> dataSet = new ArrayList<>();

    private LayoutInflater inflater;

    public TodayTransactionAdapter(final Context context){
        this(context, null);
    }

    public TodayTransactionAdapter(final Context context, final List<Transaction> data){
        this.inflater = LayoutInflater.from(context);
        if(data != null)
            this.dataSet = data;
    }

    public void swapDataset(final List<Transaction> data){
        if(data != null)
            this.dataSet = data;

        notifyDataSetChanged();
    }

    @Override
    public TodayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TodayHolder(inflater.inflate(R.layout.dashboard_today_list_item, parent, false));
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

            timeStamp = layout.findViewById(R.id.id_global_timestamp);
            sourceName = layout.findViewById(R.id.id_global_title);
            transactionAmount = layout.findViewById(R.id.id_global_total_display);
        }

        public void bind(final Transaction item){
            timeStamp.setText(Ts.from(item.getTimestamp()).simpleTime());
            sourceName.setText(item.getSourceId());
            transactionAmount.setText(String.valueOf(item.getAmount()));
        }

    }

}
