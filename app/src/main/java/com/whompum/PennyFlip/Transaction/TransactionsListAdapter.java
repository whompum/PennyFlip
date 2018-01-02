package com.whompum.PennyFlip.Transaction;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.PennyFlipTimeFormatter;
import com.whompum.PennyFlip.Time.Timestamp;

import java.util.ArrayList;
import java.util.List;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

/**
 * Created by bryan on 12/31/2017.
 */

public class TransactionsListAdapter extends RecyclerView.Adapter<TransactionsListAdapter.Holder> {


    private List<Transactions> dataSet = new ArrayList<>();

    private LayoutInflater inflater;

    private int todayTextColor = 0;

    public TransactionsListAdapter(final Context context){
        this(context, null);
    }

    public TransactionsListAdapter(final Context context, @Nullable final List<Transactions> data){

        if(Build.VERSION.SDK_INT >= 23)
            todayTextColor = context.getColor(R.color.light_blue);
        else
            todayTextColor = context.getResources().getColor(R.color.light_blue);

        inflater = LayoutInflater.from(context);

        if(data != null)
            dataSet = data;

        Log.i("test", "HELLO FROM CONSTRUCTOR TRANSACTIONLISTADAPTER");
    }

    public void setDataSet(final List<Transactions> dataSet){
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("test", "creating view holder");

        return new Holder(inflater.inflate(R.layout.layout_transaction_list_item, parent, false), todayTextColor);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
         holder.bind(dataSet.get(position));
    }


    @Override
    public int getItemCount() {
        Log.i("test", "TRANSACTION LIST SIZE: " + String.valueOf(dataSet.size()));
        return dataSet.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{

        private static Timestamp today = Timestamp.now();

        private TextView timeStamp;
        private CurrencyEditText value, originalValue;

        private int todayTextColor;
        private int originalTextColor;


        public Holder(final View layout, int todayTextColor){
            super(layout);

            timeStamp = layout.findViewById(R.id.id_transctions_list_item_timestamp);
            value = layout.findViewById(R.id.id_transactions_list_item_value);
            originalValue = layout.findViewById(R.id.id_transactions_list_item_original_value);

            this.todayTextColor = todayTextColor;
            this.originalTextColor = timeStamp.getCurrentTextColor();
        }

        public void bind(final Transactions transactions){

            final String transDate = transactions.simpleDate();

            Log.i("test", "TRANSACTION DATE: " + transDate);

            if(transDate.equals(PennyFlipTimeFormatter.simpleDate(today)))
                adjustTextForToday();
            else
                adjustForPast(transDate);

            value.setText(String.valueOf(transactions.getAmount()));
            originalValue.setText(String.valueOf(transactions.getOriginalAmount()));
        }

        private void adjustTextForToday(){

            timeStamp.setText(R.string.string_today_cap);
            timeStamp.setTypeface(null, Typeface.BOLD);
            timeStamp.setTextColor(todayTextColor);
        }
        private void adjustForPast(final String date){
            timeStamp.setTextColor(originalTextColor);
            timeStamp.setTypeface(null, Typeface.NORMAL);
            timeStamp.setText(date);
        }

    }


}
