package com.whompum.PennyFlip.ActivityHistory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by bryan on 1/4/2018.
 */

public class ActivityHistory extends AppCompatActivity implements TransactionStickyHeaders.StickyData{

    private ImageButton up;
    private TextView timeRange;


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
        pickerTimeline.setFirstVisibleDate(2018, 0, 2);
        pickerTimeline.setLastVisibleDate(2018, 0, 8);
        pickerTimeline.setSelectedDate(2018, 0, 8);


        /**
         * CUIDADO
         *
         * to the TimePicker the month of Dec is 11, and January is 0
         */


        final RecyclerView.ItemDecoration decoration = new TransactionStickyHeaders(this);
        transactionList.addItemDecoration(decoration);
    }



    private void bindTimeLine(final TransactionHeaderItem item){

        final TransactionHeaderItem headerItem = item;

        final Timestamp timestamp = headerItem.getTimestamp();

        final int day = timestamp.day();

        pickerTimeline.setSelectedDate(2018, 0, day);
    }


    /**
     * Boolean value on whether an item is a Header; Used for StickyHeader
     *
     * @param child some child at an arbitrary index (probably 0)
     *
     * @return if the Child is considered a HEADER by the adapter
     */
    @Override
    public boolean isItemAHeader(final View child) {

        final int position = transactionList.getChildAdapterPosition(child);

        final boolean isHeader = adapter.getItemViewType(position) == TransactionListAdapter.HEADER;

        if(isHeader)
            bindTimeLine((TransactionHeaderItem)adapter.getDataAt(position));


        return isHeader;
    }

    /**
     * Binds the header object to the child; NOTE this method is
     * responsible for determining if it should bind the child to the Header
     * or not.
     *
     * @param header Header object to bind to
     * @param child The child to potentially bind
     *
     */
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

        final long day = TimeUnit.DAYS.toMillis(1);

        final long today = System.currentTimeMillis();

        long transactionDate = today;

        for(int i = 0; i < 7; i++){

            final TransactionHeaderItem headerItem = new TransactionHeaderItem(Timestamp.from(transactionDate), 4);

            transactions.add(headerItem);

            for(int a = 0; a < 4; a++){

              Transactions trans = null;

              if(a % 2 == 0)
                trans = new Transactions(Transactions.ADD, 2782L, 2113L, "Car Wash");
              else
                trans = new Transactions(Transactions.SPEND, 9999L, 5556L, "Food");


                final TransactionsItem transItem = new TransactionsItem(trans);
                transactions.add(transItem);
            }
            transactionDate -= day;
        }

    return transactions;
    }

}

















