package com.whompum.PennyFlip.Transactions.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Time.Ts;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionListAdapter;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionsContent;
import com.whompum.PennyFlip.R;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

/**
 * Created by bryan on 1/7/2018.
 */
public class TransactionHolder extends RecyclerView.ViewHolder implements TransactionListAdapter.DataBind<TransactionsContent> {

    private TextView transactionLastUpdate;
    private TextView transactionSource;
    private CurrencyEditText transactionAmount;

    private int addColor;
    private int spendColor;

    public TransactionHolder(final View layout, final int addClr, final int spendClr){
        super(layout);
        transactionLastUpdate = layout.findViewById(R.id.id_global_timestamp);
        transactionSource = layout.findViewById(R.id.id_global_title);
        transactionAmount = layout.findViewById(R.id.id_global_total_display);

        this.addColor = addClr;
        this.spendColor = spendClr;
    }

    @Override
    public void bind(TransactionsContent headerItem) {

        final Transaction t = headerItem.getTransaction();

        transactionLastUpdate.setText(Ts.from(t.getTimestamp()).getPreferentialDate());
        transactionSource.setText(t.getTitle());
        transactionAmount.setText(String.valueOf(t.getAmount()));

        if(t.getTransactionType() == TransactionType.ADD){
            transactionSource.setTextColor(addColor);
            transactionAmount.setTextColor(addColor);
        }else if(t.getTransactionType() == TransactionType.SPEND){
            transactionSource.setTextColor(spendColor);
            transactionAmount.setTextColor(spendColor);
        }
    }



}