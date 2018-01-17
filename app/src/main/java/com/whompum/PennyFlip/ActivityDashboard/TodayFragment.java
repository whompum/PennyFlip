package com.whompum.PennyFlip.ActivityDashboard;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transaction.Models.Transactions;

import java.util.List;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

/**
 * Created by bryan on 1/12/2018.
 */

public abstract class TodayFragment extends Fragment {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_dasbhoard_summary;

    @ColorRes
    protected int VALUE_TEXT_COLOR = R.color.dark_grey;

    private CurrencyEditText value;

    protected RecyclerView transactionsList;
    private RecyclerView.Adapter transactionsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(LAYOUT, container, false);


        this.transactionsList = layout.findViewById(R.id.id_today_transaction_list);
        this.transactionsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        this.transactionsAdapter = new TodayTransactionAdapter(getContext());

        transactionsList.setAdapter(transactionsAdapter);

        this.value = layout.findViewById(R.id.tempValue);
             value.setTextColor(getColor(VALUE_TEXT_COLOR));

        populate();
        populateValue();

        Log.i("TRANSACTIONS", "ADAPTER TOTAL" + transactionsAdapter.getItemCount());
        Log.i("TRANSACTIONS", "RECYCLER NULL" + String.valueOf(transactionsList == null));


    return layout;
    }

    private int getColor(@ColorRes final int colorRes){

        if(Build.VERSION.SDK_INT >= 23)
            return getContext().getColor(colorRes);
        else
            return getContext().getResources().getColor(colorRes);

    }

    public void setTransactions(final List<Transactions> data){
        if(transactionsAdapter != null)
            ((TodayTransactionAdapter)transactionsAdapter).setDataSet(data);
    }

    protected void setValue(final long pennies){
        setValue(String.valueOf(pennies));
    }

    protected void setValue(final String value){
        this.value.setText(value);
    }

    protected abstract void populate();
    protected abstract void populateValue();


}
