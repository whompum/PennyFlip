package com.whompum.PennyFlip.ActivityHistory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.badoualy.datepicker.DatePickerTimeline;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Transaction.Models.HeaderItem;
import com.whompum.PennyFlip.Transaction.Models.TransactionHeaderItem;
import com.whompum.PennyFlip.Transaction.TransactionListAdapter;
import com.whompum.PennyFlip.Transaction.Models.Transactions;
import com.whompum.PennyFlip.Transaction.Models.TransactionsItem;
import com.whompum.PennyFlip.Transaction.TransactionStickyHeaders;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryan on 1/4/2018.
 */

public class ActivityHistory extends AppCompatActivity implements TransactionStickyHeaders.StickyData{

    private DatePickerTimeline pickerTimeline;

    private RecyclerView transactionList;
    private TransactionListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        transactionList = findViewById(R.id.id_history_recycler);
        transactionList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new TransactionListAdapter(this, temp());

        transactionList.setAdapter(adapter);


        pickerTimeline = findViewById(R.id.id_history_time_picker);
        pickerTimeline.setFirstVisibleDate(2017, 11, 31);
        pickerTimeline.setLastVisibleDate(2018, 0, 6);


        /**
         * CUIDADO
         *
         * to the TimePicker the month of Dec is 11, and January is 0
         */


        final RecyclerView.ItemDecoration decoration = new TransactionStickyHeaders(this);
        transactionList.addItemDecoration(decoration);




    }


    @Override
    public boolean isItemAHeader(final View child) {

        final int position = transactionList.getChildAdapterPosition(child);

        final boolean isHeader = adapter.getItemViewType(position) == TransactionListAdapter.HEADER;

        return isHeader;
    }

    @Override
    public void bindHeader(View header, final View child) {

        final int fromPos = transactionList.getChildAdapterPosition(child);

        final TransactionHeaderItem headerItem = adapter.getLastHeader(fromPos);

        if(headerItem != null){
            ((TextView)header.findViewById(R.id.id_history_transaction_header_date)).setText(headerItem.getDate());
            ((TextView)header.findViewById(R.id.id_history_transaction_header_cuantos)).setText(headerItem.getNumTransactions());
        }

    }

    List<HeaderItem> temp(){

        final List<HeaderItem> transactions = new ArrayList<>();

        final long month = 2629746000L;
        final long day = 86499999L;
        final long now = System.currentTimeMillis();

        long time = now;

        for(int i = 0; i < 50; i++){

            final Timestamp timestamp = Timestamp.from(time);

            if(i==0 | i % 5 == 0) {
                transactions.add(new TransactionHeaderItem(timestamp,8));
                time -= month;
            }else if (i % 2 == 0){
                final Transactions tran = new Transactions(Transactions.ADD, time, 2782, 1120, "Car Wash" );

                transactions.add(new TransactionsItem(tran));
                time -= day;
            }else if (i % 3 == 0){

                final Transactions trans = new Transactions(Transactions.SPEND, time, 2782, 5782, "Food");
                transactions.add(new TransactionsItem(trans));
                time -= day;
            }

        }

    return transactions;
    }

}

















