package com.whompum.PennyFlip.Transactions.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Time.Ts;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionListAdapter;
import com.whompum.PennyFlip.Transactions.Header.TransactionsItem;
import com.whompum.PennyFlip.R;

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
    private int callibrateColor;


    public TransactionHolder(final View layout, final int addClr, final int spendClr){
        super(layout);
        transactionLastUpdate = layout.findViewById(R.id.id_history_date_label);
        transactionSource = layout.findViewById(R.id.id_history_source_name);
        transactionAmount = layout.findViewById(R.id.id_history_transaction_amount);

        this.addColor = addClr;
        this.spendColor = spendClr;

        Log.i("TRANSACTION_FRAG", "Creating new TransactionHolder object");

    }

    @Override
    public void bind(TransactionsItem headerItem) {

        Log.i("TRANSACTION_FRAG", "Binding TransactionsItem to TransactionHolder");

        final Transaction t = headerItem.getTransactions();

        transactionLastUpdate.setText(Ts.getPreferentialDate(Ts.from(t.getTimestamp())));
        transactionSource.setText(t.getTitle());
        transactionAmount.setText(String.valueOf(t.getAmount()));

        if(t.getTransactionType() == TransactionType.ADD){
            transactionSource.setTextColor(addColor);
            transactionAmount.setTextColor(addColor);
            transactionSource.setCompoundDrawablesWithIntrinsicBounds(R.drawable.drawable_shape_circle_green,0,0,0);
        }else if(t.getTransactionType() == TransactionType.SPEND){
            transactionSource.setTextColor(spendColor);
            transactionAmount.setTextColor(spendColor);
            transactionSource.setCompoundDrawablesWithIntrinsicBounds(R.drawable.drawable_shape_circle_red,0,0,0);
        }
    }
}