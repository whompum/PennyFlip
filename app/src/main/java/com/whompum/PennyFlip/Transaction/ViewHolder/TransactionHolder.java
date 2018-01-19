package com.whompum.PennyFlip.Transaction.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.whompum.PennyFlip.Transaction.Models.TransactionType;
import com.whompum.PennyFlip.Transaction.TransactionListAdapter;
import com.whompum.PennyFlip.Transaction.Models.TransactionsItem;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transaction.Models.Transactions;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

/**
 * Created by bryan on 1/7/2018.
 */

public class TransactionHolder extends RecyclerView.ViewHolder implements TransactionListAdapter.DataBind<TransactionsItem> {

    private TextView transactionLastUpdate;
    private TextView transactionSource;
    private CurrencyEditText transactionAmount;

    private int addColor;
    private int spendColor;


    public TransactionHolder(final View layout, final int addClr, final int spendClr){
        super(layout);
        transactionLastUpdate = layout.findViewById(R.id.id_history_date_label);
        transactionSource = layout.findViewById(R.id.id_history_source_name);
        transactionAmount = layout.findViewById(R.id.id_history_transaction_amount);

        this.addColor = addClr;
        this.spendColor = spendClr;
    }

    @Override
    public void bind(TransactionsItem headerItem) {

        final Transactions transactions = headerItem.getTransactions();

        transactionLastUpdate.setText(transactions.simpleTime());
        transactionSource.setText(transactions.getSourceName());
        transactionAmount.setText(String.valueOf(transactions.getTransactionAmount()));

        if(transactions.getTransactionType() == TransactionType.ADD){
            transactionSource.setTextColor(addColor);
            transactionAmount.setTextColor(addColor);
            transactionSource.setCompoundDrawablesWithIntrinsicBounds(R.drawable.drawable_shape_circle_green,0,0,0);
        }else if(transactions.getTransactionType() == TransactionType.SPEND){
            transactionSource.setTextColor(spendColor);
            transactionAmount.setTextColor(spendColor);
            transactionSource.setCompoundDrawablesWithIntrinsicBounds(R.drawable.drawable_shape_circle_red,0,0,0);
        }
    }
}