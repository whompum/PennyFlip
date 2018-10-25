package com.whompum.PennyFlip.ActivityHistory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.date.MonthAdapter;
import com.whompum.PennyFlip.Money.TimeRange;
import com.whompum.PennyFlip.Money.Transaction.DescendingSort;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionListAdapter;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionsGroup;
import com.whompum.PennyFlip.Transactions.Data.ExpansionPredicate;
import com.whompum.PennyFlip.Transactions.Data.TransactionsGroupConverter;
import com.whompum.PennyFlip.Transactions.Decoration.TimeLineDecorator;
import com.whompum.PennyFlip.Transactions.Decoration.TransactionStickyHeaders;
import com.whompum.PennyFlip.Widgets.HeaderFrameLayout;
import com.whompum.PennyFlip.Widgets.HeaderView;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Bryan
 *
 * Represents all transactions made during a certain Time period
 *
 */
public class ActivityHistory extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        ActivityHistoryClient {

    private static final Timestamp utility = Timestamp.now();

    @BindView(R.id.id_global_list)
    protected RecyclerView transactionList;

    @BindView(R.id.local_day_count)
    protected TextView dayCount;

    private ActivityHistoryConsumer consumer;
    private TransactionListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        ButterKnife.bind(this);

        consumer = new HistoryController(this, this);

        adapter = new TransactionListAdapter(this);

        transactionList.addItemDecoration(new TimeLineDecorator(getResources()));
        transactionList.addItemDecoration(new TransactionStickyHeaders(adapter));

        transactionList.setLayoutManager(new LinearLayoutManager(this));

        transactionList.setAdapter(adapter);

        /* IS CAUSING TOO MUCH JANK WHEN SCROLLING
        transactionList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                final int adapterPos = transactionList.getChildAdapterPosition(recyclerView.getChildAt(0));

                if(adapter.getItemViewType(adapterPos) == TransactionListAdapter.HEADER){

                    utility.set( ((TransactionsGroup)adapter.getDataAt(adapterPos)).getMillis() );

                    final long headerDay = utility.getStartOfDay();

                    utility.set(System.currentTimeMillis());

                    final long todayDay = utility.getStartOfDay();

                    final long dayDifference = todayDay-headerDay;

                    final int dayDelta = (int)(dayDifference / TimeUnit.DAYS.toMillis(1));

                    final String dayCount = (dayDelta == 0)
                            ? getString(R.string.string_today)
                            : dayDelta + " " + getString(R.string.string_day_count);

                    ActivityHistory.this.dayCount.setText(dayCount);

                }

            }
        });
        */

        final long now = System.currentTimeMillis();

        final long then = Timestamp.fromPastProjection(6)
                .getMillis(); //One Week

        fetch(new TimeRange(then, now));
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int sY, int sM, int sD, int eY, int eM, int eD) {
        long floorMillis;
        long cielMillis;

        final Calendar calendar = Calendar.getInstance();

        calendar.set(sY, sM, sD);

        floorMillis = calendar.getTimeInMillis();

        calendar.set(eY, eM, eD);

        cielMillis = calendar.getTimeInMillis();

        //Now clean the data using Timestamp

        floorMillis = Timestamp.from(floorMillis).getStartOfDay();
        cielMillis = Timestamp.from(cielMillis).getStartOfDay()+(    TimeUnit.DAYS.toMillis(1)- 1L);

        fetch(new TimeRange(floorMillis, cielMillis));
    }

    @Override
    public void onDataQueried(@NonNull List<Transaction> data) {
        final long today = Timestamp.now().getStartOfDay();

        this.adapter.swapDataset(TransactionsGroupConverter.fromTransactions(data, new ExpansionPredicate() {
            @Override
            public boolean expand(long startOfDay, int position) {
                return today == startOfDay || position  < 2; //If today, or on first/second header.
            }
        }));
    }

    @OnClick(R.id.id_global_nav)
    public void onNavigation(){
        NavUtils.navigateUpFromSameTask(this);
    }

    @OnClick(R.id.local_date_picker)
    public void launchDatePicker(){

        final Timestamp floor = Timestamp.from(consumer.fetchStartDate(this));
        final Timestamp ciel = Timestamp.now();


        final int floorY = floor.getYear();
        final int floorM = floor.getMonth()-1;
        final int floorD = floor.getMonthDay();

        final int cielY = ciel.getYear();
        final int cielM = ciel.getMonth()-1;
        final int cielD = ciel.getMonthDay();

       final DatePickerDialog datePicker = DatePickerDialog.newInstance(this,
                floorY,
                floorM,
                floorD,
                cielY,
                cielM,
                cielD);

        final Calendar floorCalendar = Calendar.getInstance();
        final Calendar cielCalendar = Calendar.getInstance();

        floorCalendar.set(floorY, floorM, floorD);
        cielCalendar.set(cielY, cielM, cielD);

        datePicker.setMinDate(floorCalendar);
        datePicker.setMaxDate(cielCalendar);

        datePicker.show(getFragmentManager(), "DatePicker");

    }


    private void fetch(TimeRange timerange){
        consumer.fetch(timerange);
    }

}
