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
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.Transactions.Adapter.TransactionListAdapter;
import com.whompum.PennyFlip.Transactions.Header.TransactionStickyHeaders;
import com.whompum.PennyFlip.Transactions.Header.TransactionsGroup;

import org.joda.time.DateTime;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by bryan on 1/4/2018.
 */

public class ActivityHistory extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
    }


}

















