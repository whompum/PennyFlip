package com.whompum.PennyFlip.Transaction;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ListPopulator;
import com.whompum.PennyFlip.Transaction.Models.Transactions;

import java.util.List;

/**
 * Created by bryan on 12/30/2017.
 */

public class TransactionFragment extends Fragment implements ListPopulator<Transactions> {

    @LayoutRes
    private static final int LAYOUT_RES = R.layout.layout_transaction;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<Transactions> dataSet;

    private boolean pendingData = false;

    public static Fragment newInstance(){
        final TransactionFragment fragment = new TransactionFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View layout = inflater.inflate(LAYOUT_RES, container, false);

        recyclerView = layout.findViewById(R.id.id_source_data_transaction_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        adapter = new TransactionListAdapter(getContext());

        if(pendingData)
            bindAdapter(dataSet);

        recyclerView.setAdapter(adapter);

    return layout;
    }



    @Override
    public void populate(@NonNull List<Transactions> data) {

        if(adapter == null){
            pendingData = true;
            this.dataSet = data;
        }

        else
            bindAdapter(data);

    }



    private void bindAdapter(final List<Transactions> dataSet){
        //((TransactionsAdapter)adapter).swapDataSet(dataSet);
    }


}
