package com.whompum.PennyFlip.ActivityHistory;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transaction.Transactions;

import java.util.ArrayList;
import java.util.List;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

/**
 * Created by bryan on 1/5/2018.
 */

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.Holder> {

    public static final int LAYOUT = R.layout.history_list_item;

    private List<Transactions> dataSet = new ArrayList<>(0);

    private LayoutInflater inflater;


    private int addColor;
    private int spendColor;

    public TransactionsAdapter(final Context context){
        this(context, null);
    }

    public TransactionsAdapter(final Context context, final List<Transactions> data){
        inflater = LayoutInflater.from(context);
        if(data != null)
            this.dataSet.addAll(data);

        if(Build.VERSION.SDK_INT >= 23){
            addColor = context.getColor(R.color.light_green);
            spendColor = context.getColor(R.color.light_red);
        }else{
            addColor = context.getResources().getColor(R.color.light_green);
            spendColor = context.getResources().getColor(R.color.light_red);
        }

    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(LAYOUT, parent, false), addColor, spendColor);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{

        private TextView transactionLastUpdate;
        private TextView transactionSource;
        private CurrencyEditText transactionAmount;

        private int addColor;
        private int spendColor;

        public Holder(final View layout, final int addClr, final int spendClr){
            super(layout);

            transactionLastUpdate = layout.findViewById(R.id.id_history_date_label);
            transactionSource = layout.findViewById(R.id.id_history_source_name);
            transactionAmount = layout.findViewById(R.id.id_history_transaction_amount);

            this.addColor = addClr;
            this.spendColor = spendClr;
        }


        public void bind(final Transactions transactions){
            transactionLastUpdate.setText(transactions.simpleTime());
            transactionSource.setText(transactions.getSourceName());
            transactionAmount.setText(String.valueOf(transactions.getTransactionAmount()));

            if(transactions.getTransactionType() == Transactions.ADD){
                transactionSource.setTextColor(addColor);
                transactionAmount.setTextColor(addColor);
                transactionSource.setCompoundDrawablesWithIntrinsicBounds(R.drawable.drawable_shape_circle_green,0,0,0);
            }else if(transactions.getTransactionType() == Transactions.SPEND){
                transactionSource.setTextColor(spendColor);
                transactionAmount.setTextColor(spendColor);
                transactionSource.setCompoundDrawablesWithIntrinsicBounds(R.drawable.drawable_shape_circle_red,0,0,0);
            }

        }


    }


}
