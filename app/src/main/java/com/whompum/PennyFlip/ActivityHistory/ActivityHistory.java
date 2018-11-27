package com.whompum.PennyFlip.ActivityHistory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.whompum.PennyFlip.ListUtils.ListFragment;
import com.whompum.PennyFlip.Money.TimeRange;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Transactions.Adapter.BackgroundResolver;
import com.whompum.PennyFlip.Transactions.Adapter.ViewHolder.HolderFactory;
import com.whompum.PennyFlip.Transactions.Adapter.ViewHolder.TransactionViewHolder;
import com.whompum.PennyFlip.Transactions.TransactionFragment;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Bryan
 *
 * Represents a historical snapshot of all transactions made during adjustable time-periods
 *
 */
public class ActivityHistory extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        ActivityHistoryClient {

    public static final String TIMERANGE_KEY = "timerange.ky";

    //The controller
    private ActivityHistoryConsumer consumer;

    @BindView(R.id.id_local_today_date_to_display) public TextView toRangeDateDisplay;
    @BindView(R.id.id_local_from_date_display) public TextView fromRangeDateDisplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        ButterKnife.bind(this);

        consumer = new HistoryController(this, this);

        final Fragment fragment = initializeFragment();

        commitFragment( getSupportFragmentManager().beginTransaction(), fragment );

        /*
            Fetch the last used TimeRange object (Cached during a config-change)
            and re-query with that object. If i can not use that data, fetch using the default
            which is today, to one week ago.
         */

        if( savedInstanceState != null && savedInstanceState.getParcelable( TIMERANGE_KEY ) != null )
            query( (TimeRange) savedInstanceState.getParcelable( TIMERANGE_KEY ) );
        else
            query( new TimeRange( System.currentTimeMillis(), Timestamp.fromPastProjection(6).getMillis() ) );
    }

    private void commitFragment(@NonNull final FragmentTransaction transaction, @NonNull final Fragment fragment){

        if( fragmentExists() )
            transaction.replace( R.id.id_global_container, fragment );

        else transaction.add( R.id.id_global_container, fragment, "TAG" );

        transaction.commit();
    }

    public Fragment initializeFragment(){

        final TransactionFragment fragment =
                (TransactionFragment) ( ( fragmentExists() ) ? getExistingFragment() : getNewFragment() );

        fragment.setItemDotBackgroundResolver(new BackgroundResolver() {
            @Override
            public int getBackground(int transactionType) {
                return R.drawable.graphic_timeline_blue;
            }
        });

        fragment.setItemViewHolderFactory( new HistoryItemViewFactory() );

        return fragment;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(
                TIMERANGE_KEY,
                ((HistoryController)consumer).getLastKnownTimerange()
        );

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

        //Now clean the data using Timestamp &&
        //set displays

        query( new TimeRange(
                Timestamp.from(floorMillis).getStartOfDay(),
                Timestamp.from(cielMillis).getStartOfDay()+( TimeUnit.DAYS.toMillis( 1 )- 1L )
        ));
    }

    private void query(@NonNull final TimeRange range){
        setRangeDisplays( range );
        fetch( range );
    }

    private void setRangeDisplays(@NonNull final TimeRange range){

        final Timestamp from = Timestamp.from( range.getMillisFloor() );
        final Timestamp to = Timestamp.from( range.getMillisCiel() );

        if( from.getStringPreferentialDate() != -1 )
            fromRangeDateDisplay.setText( from.getStringPreferentialDate() );

        else
            fromRangeDateDisplay.setText( from.simpleDate() );

        if( to.getStringPreferentialDate() != -1 )
            toRangeDateDisplay.setText( to.getStringPreferentialDate() );

        else
            toRangeDateDisplay.setText( to.simpleDate() );

    }

    @Override
    public void onDataQueried(@Nullable List<Transaction> data) {
        if( data == null || data.size() == 0 )
            getExistingFragment().onNoData();

        else
            getExistingFragment().display( data );
    }

    @OnClick(R.id.id_global_nav)
    public void onNavigation(){
        NavUtils.navigateUpFromSameTask(this);
    }

    @OnClick(R.id.local_date_picker)
    public void launchDatePicker(){

        final Timestamp floor = Timestamp.from( consumer.fetchStartDate( this ) );
        final Timestamp ciel = Timestamp.now();

        final int floorY = floor.getYear();
        final int floorM = floor.getMonth()-1;
        final int floorD = floor.getMonthDay();

        final int cielY = ciel.getYear();
        final int cielM = ciel.getMonth()-1;
        final int cielD = ciel.getMonthDay();

       final DatePickerDialog datePicker = DatePickerDialog.newInstance( this,
                floorY,
                floorM,
                floorD,
                cielY,
                cielM,
                cielD );

        final Calendar floorCalendar = Calendar.getInstance();
        final Calendar cielCalendar = Calendar.getInstance();

        floorCalendar.set( floorY, floorM, floorD );
        cielCalendar.set( cielY, cielM, cielD );

        datePicker.setMinDate( floorCalendar );
        datePicker.setMaxDate( cielCalendar );

        datePicker.show( getFragmentManager(), "DatePicker" );

    }

    public boolean fragmentExists(){
        return getSupportFragmentManager().findFragmentByTag( "TAG" ) != null;
    }

    public ListFragment<Transaction> getNewFragment(){
        return TransactionFragment.newInstance(  );
    }

    public ListFragment<Transaction> getExistingFragment(){
        return (ListFragment) getSupportFragmentManager().findFragmentByTag( "TAG" );
    }


    private void fetch(TimeRange timerange){
        consumer.fetch(timerange);
    }

}
