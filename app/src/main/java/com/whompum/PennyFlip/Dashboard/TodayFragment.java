package com.whompum.PennyFlip.Dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.Money.MoneyController;
import com.whompum.PennyFlip.Money.TimeRange;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.Ts;
import com.whompum.PennyFlip.Money.Transaction.DescendingSort;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

/**
 * Created by bryan on 1/12/2018.
 */

public class TodayFragment extends Fragment implements Handler.Callback, Observer<List<Transaction>>{

    @LayoutRes
    public static final int LAYOUT = R.layout.dashboard_today_layout;

    @ColorRes
    protected int VALUE_TEXT_COLOR = R.color.dark_grey;

    protected int transactionType = Integer.MIN_VALUE;

    @BindView(R.id.total_transaction_value_display) protected CurrencyEditText value;

    @BindView(R.id.id_today_transaction_list)
    protected RecyclerView transactionsList;

    private RecyclerView.Adapter transactionsAdapter;

    //Touch delegate for the TransactionsList
    final Rect delegateBoundary = new Rect();

    private Unbinder unbinder;

    private Handler resultReceiver = new Handler(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        observeTransactions();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(LAYOUT, container, false);

        unbinder = ButterKnife.bind(this, layout);

        this.transactionsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        this.transactionsAdapter = new TodayTransactionAdapter(getContext());

        transactionsList.setAdapter(transactionsAdapter);

        value.setTextColor(getColor(VALUE_TEXT_COLOR));

        //TRIFECTA of touch handling!
        layout.post(delegate); //Post a runnable for to be ran AFTER the the layout passes.
        layout.setOnTouchListener(delegateListener);
        transactionsList.requestDisallowInterceptTouchEvent(true);

    return layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if(msg.obj != null)
            if(msg.obj instanceof LiveData)
                ((LiveData<List<Transaction>>)msg.obj).observe(this, this);

        return true;
    }

    @Override
    public void onChanged(@Nullable List<Transaction> transactions) {

        if(transactions == null || transactionsAdapter == null) return;

       Collections.sort(transactions, new DescendingSort());

        /**
         * set anchor at 0, check if any index has a larger value than index 0.
         * If yes, then anchor represents that index. Else, nothing.
         * After one full iteration where we found the best anchor to use.
         */

        ((TodayTransactionAdapter)transactionsAdapter).swapDataset(transactions);
        updateValue(transactions);
    }

    private void observeTransactions(){
        MoneyController.obtain(getContext())
                .fetchTransactions(resultReceiver, null, transactionType, fetchRange());
    }

    //Creates a TimeRange at our floor and ciel for the day
    private TimeRange fetchRange(){

        final long today = Ts.now().getStartOfDay();
        final long tomorrow = Ts.fromProjection(1).getStartOfDay(); //Manana

        return new TimeRange(today, tomorrow);
    }

    /**
     * Updates the Total Display value for Today's Transactions
     *
     * @param transactions The current list of transactions
     */
    protected void updateValue(final List<Transaction> transactions){

        long total = 0L;

        for(Transaction t: transactions)
            total += t.getAmount();

        this.value.setText(String.valueOf(total));

    }

    protected void setValue(final long pennies){
        setValue(String.valueOf(pennies));
    }

    protected void setValue(final String value){
        this.value.setText(value);
    }

    private int getColor(@ColorRes final int colorRes){

        if(Build.VERSION.SDK_INT >= 23)
            return getContext().getColor(colorRes);
        else
            return getContext().getResources().getColor(colorRes);

    }

    private final Runnable delegate = new Runnable() {
        @Override
        public void run() {
            if(getView() == null)
                return;
            delegateBoundary.set(getView().getLeft(), transactionsList.getTop(), getView().getRight(), getView().getBottom());
        }
    };


    View.OnTouchListener delegateListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            boolean consume = delegateBoundary.contains((int)event.getX(), (int)event.getY());

            if(consume)
                transactionsList.onTouchEvent(event);

            return consume;
        }
    };

}
