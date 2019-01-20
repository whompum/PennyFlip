package com.whompum.PennyFlip.ActivityStatistics.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.PennyFlip.ActivityStatistics.Data.ReportData;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Widgets.IncomeExpenseCheckbox;
import com.whompum.PennyFlip.Widgets.StatisticsReportItemCurrency;
import com.whompum.PennyFlip.Widgets.StatisticsReportItemText;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.whompum.PennyFlip.Money.Transaction.TransactionType.INCOME;
import static com.whompum.PennyFlip.Money.Transaction.TransactionType.EXPENSE;

public class ReportDataFragment extends Fragment implements StatisticsViewLayerContract.Fragment {

    public static final String TAG = ReportDataFragment.class.getSimpleName();

    @LayoutRes
    public static final int LAYOUT = R.layout.statistics_reported_content;

    @BindView(R.id.id_local_net_amount) public StatisticsReportItemCurrency netAmount;
    @BindView(R.id.id_local_daily_average) public StatisticsReportItemCurrency dailyAverage;
    @BindView(R.id.id_local_income_expense_ratio) public StatisticsReportItemText typeRatio;
    @BindView(R.id.id_local_transaction_count) public StatisticsReportItemText transactionCount;
    @BindView(R.id.id_local_transaction_average) public StatisticsReportItemCurrency transactionAverage;
    @BindView(R.id.id_local_last_transaction_source) public StatisticsReportItemText lastTransactionSourceTitle;
    @BindView(R.id.id_local_last_transaction_amount) public StatisticsReportItemCurrency lastTransactionAmount;
    @BindView(R.id.id_local_top_sources_container) public StatisticsReportItemText topSourcesLabel;
    @BindView(R.id.id_global_list) public RecyclerView topSourcesList;

    @BindView(R.id.id_local_income_expense_checkbox) public IncomeExpenseCheckbox incomeExpenseCheckbox;

    private Unbinder unbinder;

    private StatisticsViewLayerContract.Activity activity;

    private TopSourcesListAdapter topSourcesListAdapter = new TopSourcesListAdapter();

    private TransactionState incomeState;

    private TransactionState expenseState;

    private TransactionState currentState = incomeState; //Set to default

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (StatisticsViewLayerContract.Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( LAYOUT, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.unbinder = ButterKnife.bind( this, view );

        topSourcesList.setLayoutManager(
                new LinearLayoutManager( getContext(), LinearLayoutManager.HORIZONTAL, false )
        );

        incomeState = new IncomeState();
        expenseState = new ExpenseState();
        currentState = incomeState;

        topSourcesList.setAdapter( topSourcesListAdapter );

        incomeExpenseCheckbox.setTypeChangeListener( this::onTransactionTypeChange );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void displayReportData(@NonNull ReportData data) {

        if( data.getTransactionType() != currentState.transactionType )
            return;

        currentState.setUi();

        bindLastTransactionData( data );

        netAmount.setCurrency( data.getNetAmount() );
        dailyAverage.setCurrency( data.getDailyAverage() );
        transactionCount.setValue( String.valueOf( data.getNumTransactions() ) );
        transactionAverage.setCurrency( data.getTransactionAverage() );
    }

    @Override
    public void displayTopSources(@Nullable List<Source> topSources, final int type) {

        if( this.currentState.transactionType != type )
            return;

        if( topSources == null || topSources.size() == 0 ) {
            topSourcesLabel.setVisibility( View.GONE );
            topSourcesList.setVisibility( View.GONE );
        }

        else {
            topSourcesLabel.setVisibility( View.VISIBLE );
            topSourcesList.setVisibility( View.VISIBLE );
            populateSources(topSources);
        }
    }

    private void populateSources(@NonNull final List<Source> topSources){
        topSourcesListAdapter.swapDataSet( topSources );
    }

    public void onTransactionTypeChange(int type) {
        this.currentState = ( type == INCOME) ? incomeState : expenseState;
        notifyActivityOfInternalStateChange();
    }

    private void notifyActivityOfInternalStateChange(){
        activity.onTransactionTypeChange( currentState.transactionType );
    }

    private void bindLastTransactionData(@NonNull final ReportData data){

        final boolean recentTransaction = data.hasRecentTransaction();

        if( recentTransaction ) {
            lastTransactionAmount.setCurrency(data.getLastTransactionAmount());
            lastTransactionAmount.setVisibility( View.VISIBLE );
            lastTransactionSourceTitle.setValue( data.getLastTransactionSourceTitle() );
        }else{
            lastTransactionAmount.setVisibility( View.GONE );
            lastTransactionSourceTitle.setValue( R.string.string_no_data_default_label );
        }

    }

    private abstract class TransactionState{

        @IntRange(from = INCOME, to = EXPENSE)
        private int transactionType;

        @ColorRes
        private int color;

        private TextView[] dynamicColoredViews;
        private Map<Integer, WeakReference<TextView>> dynamicTextualViews;

        TransactionState(@IntRange(from = INCOME, to = EXPENSE) final int transactionType,
                         @ColorRes final int color){
            this.transactionType = transactionType;
            this.color = color;

            dynamicColoredViews = new TextView[]{
                    netAmount.getCurrencyView(),
                    dailyAverage.getCurrencyView(),
                    transactionAverage.getCurrencyView(),
                    lastTransactionSourceTitle.getValueView(),
                    lastTransactionAmount.getCurrencyView()
            };


            dynamicTextualViews = getDynamicTextualViews();

        }

        public final void setUi(){

            for( TextView v: dynamicColoredViews )
                v.setTextColor( ContextCompat.getColor( getContext(), color ));

            for (Map.Entry<Integer, WeakReference<TextView>> item : dynamicTextualViews.entrySet())
                item.getValue().get().setText( item.getKey() );

        }

        @IntRange(from = INCOME, to = EXPENSE)
        protected int getTransactionType(){
            return transactionType;
        }

        @NonNull
        protected abstract Map<Integer, WeakReference<TextView>> getDynamicTextualViews();

    }

    private class IncomeState extends TransactionState{

        public IncomeState() {
            super( TransactionType.INCOME, R.color.dark_green );
        }

        @SuppressLint("UseSparseArrays")
        @NonNull
        @Override
        protected Map<Integer, WeakReference<TextView>> getDynamicTextualViews() {

            final Map<Integer, WeakReference<TextView>> views = new HashMap<>( 3 );

            views.put( R.string.string_report_net_income, new WeakReference<>( netAmount.getLabel() ) );
            views.put( R.string.string_report_income_expense_ratio, new WeakReference<>( typeRatio.getLabel() ) );
            views.put( R.string.string_report_top_income_sources, new WeakReference<>( topSourcesLabel.getLabel() ) );

            return views;
        }
    }

    private class ExpenseState extends TransactionState{

        public ExpenseState() {
            super(EXPENSE, R.color.dark_red );
        }


        @SuppressLint("UseSparseArrays")
        @NonNull
        @Override
        protected Map<Integer, WeakReference<TextView>> getDynamicTextualViews() {

            final Map<Integer, WeakReference<TextView>> views = new HashMap<>( 3 );

            views.put( R.string.string_report_net_expense, new WeakReference<>( netAmount.getLabel() )  );
            views.put( R.string.string_report_expense_to_income_ratio, new WeakReference<>( typeRatio.getLabel() )  );
            views.put( R.string.string_report_top_expense_sources, new WeakReference<>( topSourcesLabel.getLabel() ) );

            return views;
        }
    }

}

