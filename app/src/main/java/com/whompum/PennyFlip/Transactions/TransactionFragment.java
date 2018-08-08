package com.whompum.PennyFlip.Transactions;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.whompum.PennyFlip.Money.MoneyController;
import com.whompum.PennyFlip.Money.Transaction.DescendingSort;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.Ts;
import com.whompum.PennyFlip.Transactions.Adapter.TimeLineDecorator;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionListAdapter;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionsConverter;
import com.whompum.PennyFlip.Transactions.Header.TransactionStickyHeaders;
import com.whompum.PennyFlip.Transactions.Header.TransactionsGroup;

import java.util.Collections;
import java.util.List;

/**
 * Created by bryan on 12/30/2017.
 */

public class TransactionFragment extends Fragment implements TransactionStickyHeaders.StickyData, Handler.Callback,
        Observer<List<Transaction>> {

    public static final String SOURCE_KEY = "source.ky";
    public static final String HIGHLIGHT_KEY = "highlight.ky";
    public static final String HIGHLIGHT_DARK_KEY = "highlightDark.ky";

    @LayoutRes
    private static final int LAYOUT_RES = R.layout.transaction_list;

    private RecyclerView transactionsList;
    private TransactionListAdapter adapter;

    private Handler resultReceiver = new Handler(this);

    private int highlight;
    private int highlightDark;

    public static Fragment newInstance(@NonNull final Bundle args){
        final TransactionFragment fragment = new TransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String sourceId = getArguments().getString(SOURCE_KEY);

        if(sourceId == null) throw new IllegalStateException("We must have a Source ID");

        if(Build.VERSION.SDK_INT >= 23){
            highlight = getContext().getColor(getArguments().getInt(HIGHLIGHT_KEY));
            highlightDark = getContext().getColor(getArguments().getInt(HIGHLIGHT_DARK_KEY));
        }else{
            highlight = getContext().getResources().getColor(getArguments().getInt(HIGHLIGHT_KEY));
            highlightDark = getContext().getResources().getColor(getArguments().getInt(HIGHLIGHT_DARK_KEY));
        }

        this.adapter = new TransactionListAdapter(getContext());
        MoneyController.obtain(getContext())
                .fetchTransactions(resultReceiver, sourceId, null, null);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(LAYOUT_RES, container, false);

        this.transactionsList = layout.findViewById(R.id.id_global_list);
        this.transactionsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        this.transactionsList.setAdapter(adapter);
        this.transactionsList.addItemDecoration(new TransactionStickyHeaders(this, highlightDark, highlight));
        this.transactionsList.addItemDecoration(new TimeLineDecorator(getContext().getResources()));

    return layout;
    }

    @Override
    public boolean handleMessage(Message msg) {

        if( !(msg.obj instanceof LiveData) ) return true;

        ((LiveData<List<Transaction>>)msg.obj).observe(this, this);

        return true;
    }

    @Override
    public void onChanged(@Nullable List<Transaction> transactions) {

        if(transactions != null && transactions.size() > 0) {

            Collections.sort(transactions, new DescendingSort());

            adapter.swapDataset(TransactionsConverter.fromTransactions(transactions));

            toggleNoTransactionsDisplay(false);

        }else{
            toggleNoTransactionsDisplay(true);
        }
    }

    @Override
    public boolean isItemAHeader(View child) {
        final int position = transactionsList.getChildAdapterPosition(child);

        if(position == RecyclerView.NO_POSITION)
            return false;

        return adapter.getItemViewType(position) == TransactionListAdapter.HEADER;
    }

    @Override
    public void bindHeader(View header, View child) {
        final int fromPos = transactionsList.getChildAdapterPosition(child);

        final TransactionsGroup headerItem = adapter.getLastHeader(fromPos);

        if(headerItem != null)
            ((TextView)header.findViewById(R.id.id_global_timestamp)).setText(Ts.from(headerItem.getMillis()).simpleDate());
    }

    private void toggleNoTransactionsDisplay(final boolean toggle){

        if(getView() != null && !toggle)
            getView().findViewById(R.id.no_transactions_container).setVisibility(View.INVISIBLE);

        if(getView() != null && toggle)
            getView().findViewById(R.id.no_transactions_container).setVisibility(View.VISIBLE);
    }

}
