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
import android.widget.TextView;

import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ListPopulator;
import com.whompum.PennyFlip.Transaction.Models.HeaderItem;
import com.whompum.PennyFlip.Transaction.Models.TransactionHeaderItem;
import com.whompum.PennyFlip.Transaction.Models.Transactions;

import java.util.List;

/**
 * Created by bryan on 12/30/2017.
 */

public class TransactionFragment extends Fragment implements ListPopulator<HeaderItem>, TransactionStickyHeaders.StickyData {

    @LayoutRes
    private static final int LAYOUT_RES = R.layout.layout_transaction;

    private RecyclerView recyclerView;
    private TransactionListAdapter adapter;

    private TransactionStickyHeaders headers;

    private List<HeaderItem> dataSet;

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

        if(pendingData) {
            bindAdapter(dataSet);
            recyclerView.addItemDecoration(new TransactionStickyHeaders(this));
        }

        recyclerView.setAdapter(adapter);


    return layout;
    }


    /**
     * PendingData is used in the instance where the heirarchy hasn't been created yet, but data is available.
     * We know the layout hasn't been created because the adapter is only initialized there. Thus, if it is null
     * @ the time data comes in, set pending data to true, that way when the layout is created we can init
     * the recycler view
     *
     * @param data
     */
    @Override
    public void populate(@NonNull List<HeaderItem> data) {

        if(adapter == null){
            pendingData = true;
            this.dataSet = data;
        }

        else {
            bindAdapter(data);
            recyclerView.addItemDecoration(new TransactionStickyHeaders(this));
        }

    }


    @Override
    public boolean isItemAHeader(View child) {
        final int position = recyclerView.getChildAdapterPosition(child);

        if(position == RecyclerView.NO_POSITION)
            return false;

        return adapter.getItemViewType(position) == TransactionListAdapter.HEADER;
    }

    @Override
    public void bindHeader(View header, View child) {
        final int fromPos = recyclerView.getChildAdapterPosition(child);

        final TransactionHeaderItem headerItem = adapter.getLastHeader(fromPos);

        if(headerItem != null){
            ((TextView)header.findViewById(R.id.id_history_transaction_header_date)).setText(headerItem.getDate());
            ((TextView)header.findViewById(R.id.id_history_transaction_header_cuantos)).setText(headerItem.getNumTransactions());
        }
    }

    private void bindAdapter(final List<HeaderItem> dataSet){
        //((TransactionsAdapter)adapter).swapDataSet(dataSet);
    }


}
