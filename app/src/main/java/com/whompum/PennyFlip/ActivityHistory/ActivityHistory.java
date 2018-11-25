package com.whompum.PennyFlip.ActivityHistory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.whompum.PennyFlip.ListUtils.ListFragment;
import com.whompum.PennyFlip.Money.TimeRange;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Transactions.TransactionFragment;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    //The controller
    private ActivityHistoryConsumer consumer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        ButterKnife.bind(this);

        consumer = new HistoryController(this, this);

        if( fragmentExists() )
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace( R.id.id_global_container, getExistingFragment() )
                    .commit();

        else
            getSupportFragmentManager()
                    .beginTransaction()
                    .add( R.id.id_global_container, getNewFragment(), "TAG" )
                    .commit();


        //Fetch default value from today and one week ago.
        fetch(new TimeRange(System.currentTimeMillis(), Timestamp.fromPastProjection(6).getMillis()));
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

        fetch( new TimeRange(floorMillis, cielMillis) );
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
