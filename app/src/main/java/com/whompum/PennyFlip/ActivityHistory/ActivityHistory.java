package com.whompum.PennyFlip.ActivityHistory;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.github.badoualy.datepicker.DatePickerTimeline;
import com.whompum.PennyFlip.Data.Loader.TransactionsLoader;
import com.whompum.PennyFlip.Data.LoaderArgs;
import com.whompum.PennyFlip.Data.Schemas.TransactionsSchema;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.MidnightTimestamp;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Transactions.Header.TransactionHeaderItem;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionListAdapter;
import com.whompum.PennyFlip.Transactions.Header.TransactionStickyHeaders;

import org.joda.time.DateTime;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by bryan on 1/4/2018.
 */

public class ActivityHistory extends AppCompatActivity implements TransactionStickyHeaders.StickyData, LoaderArgs, LoaderManager.LoaderCallbacks<Cursor>{

    /**
     * These states are binded to the R.array.timerange values.
     *
     * This is a simple mapping.  E.G. TODAY is a reference to the 0-index of
     * the timerange array, which is today
     *
     */
    public static final int TODAY = 0; //Constant Mapping for Today transactins
    public static final int THIS_WEEK = 1; //Constant Mapping for this weeks values
    public static final int THIS_MONTH = 2; //Constant mapping for This Month values
    public static final  int CUSTOM = 3; //Constant Mapping for custom timerange values


    public static final long WEEK = 7; //Num days in a week
    public static final long MONTH = 30; //Num days in a month (Not necesarilly; Adjust later if needed;

    //Where timestamp is greater than the floor, and less t hat the ceiling
    public static final String WHERE = TransactionsSchema.TransactionTable.COL_TIMESTAMP + " >=? AND " +
                                       TransactionsSchema.TransactionTable.COL_TIMESTAMP + " <?";

    public static final String SORT_ORDER = TransactionsSchema.TransactionTable.COL_TIMESTAMP + " DESC";

    //Default timeRange
    private final Timerange TIMERANGE_DEFAULT = Timerange.get(getRangeFloorMillis(WEEK), Timestamp.now());

    private DatePickerTimeline pickerTimeline;

    private RecyclerView transactionList;
    private TransactionListAdapter adapter;


    private boolean doneInitializing = false; //Logic control value for the Spinner, during initialization


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        transactionList = findViewById(R.id.id_global_list);
        transactionList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new TransactionListAdapter(this);

        transactionList.setAdapter(adapter);

        findViewById(R.id.id_global_nav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(ActivityHistory.this);
            }
        });

        pickerTimeline = findViewById(R.id.local_history_date_selector);
        pickerTimeline.setFollowScroll(false);

        /**
         * CUIDADO
         * to the TimePicker, the month of Dec is 11, and January is 0
         */

        final RecyclerView.ItemDecoration decoration = new TransactionStickyHeaders(this, 0, 0);
        transactionList.addItemDecoration(decoration);

        initializeTimeRange(); //Initializes the TimeRange Spinner

        generateDefaultLoaderArgs();
        bindTimelineCapValues(); //Sets the starting/ending values of the Picker Timeline;

        getSupportLoaderManager().initLoader(DEFAULT_LOADER_ID, loaderArgs, this);
    }


    @Override
    public void generateDefaultLoaderArgs() {
        loaderArgs.putString(WHERE_KEY, WHERE);
        loaderArgs.putStringArray(WHERE_ARGS_KEY, fetchFromTimerange(TIMERANGE_DEFAULT));
        loaderArgs.putString(SORT_ORDER_KEY, SORT_ORDER);
    }

    private void initializeTimeRange(){

        final ArrayAdapter<CharSequence> timeranges =
                ArrayAdapter.createFromResource(this, R.array.timeRanges, R.layout.layout_timerange_header);

        timeranges.setDropDownViewResource(R.layout.layout_timerange_item);


        ((Spinner)findViewById(R.id.local_history_time_range_selector)).setAdapter(timeranges);
        ((Spinner)findViewById(R.id.local_history_time_range_selector)).setSelection(THIS_WEEK);
        ((Spinner)findViewById(R.id.local_history_time_range_selector)).setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                /**
                 * The spinner calls onitemSelected when its initializing, so
                 * using doneInitializing is a logic value to handle that situation;
                 */
               if(doneInitializing)
                   changeTimerange(position);

               doneInitializing = true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final TransactionsLoader loader = new TransactionsLoader(this);


        /**
         * The statement should look like this:
         *
         * select [PROJECTION] from transactions
         * where timestamp >= floor AND timestamp < Ciel
         */

        loader.setSelection(args.getString(WHERE_KEY)); //Set selection
        loader.setSelectionArgs(args.getStringArray(WHERE_ARGS_KEY)); //Sets the arguments
        loader.setSortOrder(args.getString(SORT_ORDER_KEY)); //Sets the sort order

    return loader;
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
//        final List<Transactions> transactionsList = new TransactionsCursorAdapter(data).fromCursor();
        //final List<HeaderItem> headerItems = HeaderAdapter.fromList(transactionsList);

        //adapter.swapDataset(headerItems);

        //final TransactionHeaderItem item = adapter.getFirstHeader();

        //if(item != null) { //Will be null if the adapter has no data
          //  final Timestamp firstDate = item.getTimestamp();
            //pickerTimeline.setSelectedDate(firstDate.year(), firstDate.month() - 1, firstDate.day());
        //}
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    /**
     * Maybe i'm abusing the API, idk but to map the selected spinner item
     * to its corresponding resource index is too un-intuitive, so a tight coupling
     * is needed here. Please update accordingly if i ever add more selections.
     *
     * MAPING
     *  0: timerange Today
     *  1: timerange This Week
     *  2: timerange This Month
     *  3: timerange Custom
     *
     * @param position
     */
    private void changeTimerange(@IntRange(from=TODAY , to=CUSTOM) final int position){

        //Position references is a pre-defined timerange
        if(position < CUSTOM){

            final Timestamp floor = captureFloorDateTimestamp(position);
            final Timestamp ciel = Timestamp.now();

            final Timerange timerange = Timerange.get(floor, ciel);

            loaderArgs.putStringArray(WHERE_ARGS_KEY, fetchFromTimerange(timerange));
        }

        //Position is a reference to the custom
        if(position == CUSTOM){
            launchTimerangeSelector();
            return;
        }

        bindTimelineCapValues();

    requery();
    }



    /**
     * Returns the floor dates midnight Timestamp.
     * The date is determined by the position, which like changeTimerange()
     * is highly coupled to the array resource. To see the mappings
     * looks at changeTimerange() documentation
     *
     * @param position Which date to find
     * @return The Timestamp date of the floors
     */
    @NonNull
    private Timestamp captureFloorDateTimestamp(final int position){

        if(position == 0) //The timestamp is for today only
            return Timestamp.from(MidnightTimestamp.today().getTodayMidnightMillis());

        else if(position == 1)
            return getRangeFloorMillis(WEEK);

        else if(position == 2)
            return getRangeFloorMillis(MONTH);

        return Timestamp.now();
    }


    /**
     * Returns the floor Timestamp value for (now - days.toMillis())
     * @param days Num of days to set the floor to. Probably a value of  THIS_WEEK / THIS_MONTH
     * @return
     */
    public Timestamp getRangeFloorMillis(final long days){
        final long nowMills = Timestamp.now().millis();

        final long rangeMillis = TimeUnit.DAYS.toMillis(days);

        final MidnightTimestamp floor = MidnightTimestamp.from(nowMills - rangeMillis);

        return Timestamp.from(floor.getTodayMidnightMillis());
    }


    /**
     * Captures the Floor / Ciel millis of the Timerange in a String array
     * that is used to query from the Transactions Database;
     * @param timerange
     * @return
     */
    private String[] fetchFromTimerange(final Timerange timerange){
        return new String[]{String.valueOf(timerange.getFloorTimerange()), String.valueOf(timerange.getCielTimerange())};
    }


    /**
     * Restarts the Loader with the updated LoaderArgs Bundle
     */
    private void requery(){


        /**
         * Please be aware that the ciel is always going to have a day that is one day
         * ahead of the current day, since we fetch all transactions that are less-than
         * the ciel (Which is the first midnight of the next day)
         *
         * So if i were to run a log on ciel.day()
         * and today is 2/1/18,
         * day would return a 2, not a 1.
         * Then we hand that long value to the loader which will return any transaction
         * thats happened before that date;
         */

        getSupportLoaderManager().restartLoader(DEFAULT_LOADER_ID, loaderArgs, this);

    }


    /**
     * Selects dates from the date picker
     *
     * CUADIDO:
     * Dates from the Timestamp (I.E. Joda Time) ARE NOT ZERO-BASED INDEX!!!!!!!
     * Whereas dates from the Datepicker dialog API are zero-based... WTF.
     * Also note that the dialog has its months as Zero-based, but the days are not.
     * A trivial but quintessential implication for any use-case of that dialog.
     */
    private void launchTimerangeSelector(){

        final Timestamp floor = Timestamp.from(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(33));

        final Timestamp ciel = Timestamp.now();

        int cielMonth = ciel.month()-1; //Months are zero based, so we offset them by minus one (joda time (timestamp) is 1-based. The dialog is not;


        final DatePickerDialog datePicker = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

                final int floorMonth = monthOfYear+1; //Add one to account for my API
                final int cielMonth =  monthOfYearEnd+1; //Offset both these guys by one

                final DateTime floorDate = new DateTime(year, floorMonth, dayOfMonth, 0,0,0);
                final DateTime cielDate = new DateTime(yearEnd, cielMonth, dayOfMonthEnd, 0,0,0);

                final Timestamp floorTimestamp = Timestamp.from(floorDate);
                final Timestamp cielTimestamp = Timestamp.from(cielDate);

                long floorMillis;
                long cielMillis;

                floorMillis = Math.min(
                        floorTimestamp.millis(),
                        cielTimestamp.millis()
                );

                cielMillis = Math.max(
                        floorTimestamp.millis(),
                        cielTimestamp.millis()
                );

                final String[] whereArgs = fetchFromTimerange(
                     Timerange.get(floorMillis, cielMillis)
                );

                loaderArgs.putStringArray(WHERE_ARGS_KEY, whereArgs);

                bindTimelineCapValues();

                requery();
            }
        }, ciel.year(), cielMonth  , ciel.day() );


        datePicker.setMinDate(floor.getTimestamp().toCalendar(Locale.getDefault()));
        datePicker.setMaxDate(ciel.getTimestamp().toCalendar(Locale.getDefault()));

        datePicker.show(getFragmentManager(), "TimerangeSelector");
    }




    /**
     * Binds the current millis to the Timeline
     * with the timerange
     *
     * BINDS Start/End values of the Timeline
     *
     */
    private void bindTimelineCapValues(){

        final String[] timerange = loaderArgs.getStringArray(WHERE_ARGS_KEY);

        if(timerange == null)
            return;

        final long floorMillis = Math.min(Long.parseLong(timerange[0]), Long.parseLong(timerange[1]) );
        final long cielMillis = Math.max(Long.parseLong(timerange[0]), Long.parseLong(timerange[1]));


        final Timestamp floor = Timestamp.from(floorMillis);
        final Timestamp ciel = Timestamp.from(cielMillis);


        final int yearFloor = floor.year();
        final int monthFloor = floor.month()-1;
        final int dayFloor = floor.day();

        final int yearCiel = ciel.year();
        final int monthCiel = ciel.month()-1;
        final int dayCiel = ciel.day()-1; //For querying purposes, the cieling day is always 1 day ahead of its intrinsic bounds, so to account for  that, i subtract one

        pickerTimeline.setFirstVisibleDate(yearCiel, monthFloor, dayFloor);
        pickerTimeline.setLastVisibleDate(yearFloor, monthCiel, dayCiel);


    }


    /**
     * Binds the Current Header Items date
     * to the pickerTimeline
     *
     * @param item The current header item with the Date associated to it.
     */
    private void bindTimelineDate(final TransactionHeaderItem item){
      //  final int year = item.getTimestamp().year();
       // final int month = item.getTimestamp().month()-1;
        //final int day = item.getTimestamp().day();

   //     pickerTimeline.setSelectedDate(year, month, day);
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
            bindTimelineDate((TransactionHeaderItem)adapter.getDataAt(position));


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
            ((TextView)header.findViewById(R.id.id_global_timestamp)).setText(headerItem.getDate());
            ((TextView)header.findViewById(R.id.local_transaction_count_header)).setText(headerItem.getNumTransactions());
            bindTimelineDate(headerItem);
        }

    }


    private void tempData(){

        long now = System.currentTimeMillis();

        final long cincoDia = TimeUnit.DAYS.toMillis(2);

        final long $1Min = TimeUnit.MINUTES.toMillis(1);

        for(int i = 0 ; i < 100; i++) {

            if(i % 5 == 0)
                now -= cincoDia;

            final long time = (now += $1Min);

            final ContentValues values = new ContentValues();
            values.put(TransactionsSchema.TransactionTable.COL_TIMESTAMP, time);
            values.put(TransactionsSchema.TransactionTable.COL_TOTAL, 2782L);
            values.put(TransactionsSchema.TransactionTable.COL_SOURCE_ID, 1L);
            values.put(TransactionsSchema.TransactionTable.COL_SOURCE_NAME, String.valueOf(i));
            values.put(TransactionsSchema.TransactionTable.COL_SOURCE_TYPE, TransactionType.ADD);

            getContentResolver().insert(TransactionsSchema.TransactionTable.URI, values);


        }


    }

}

















