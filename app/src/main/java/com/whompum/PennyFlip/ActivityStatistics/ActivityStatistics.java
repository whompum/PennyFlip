package com.whompum.PennyFlip.ActivityStatistics;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.whompum.PennyFlip.Money.TimeRange;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Time.UserStartDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityStatistics extends AppCompatActivity {

    @BindView(R.id.local_month_selector) protected AppCompatSpinner monthSelector;
    @BindView(R.id.local_year_selector) protected AppCompatSpinner yearSelector;

    @BindView(R.id.local_container) protected ViewGroup container; //Used for animations, touch handling

    private DateQueryable queryable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_layout_main);

        ButterKnife.bind(this);

        //Init the Monthly Spinner
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, R.layout.statistics_month_selector_layout,
                        getResources().getStringArray(R.array.timeRanges));
        adapter.setDropDownViewResource(R.layout.drop_down_selector_item_layout);
        monthSelector.setAdapter(adapter);


        //Reset the Adapter to another for the Yearly Spinner
        adapter =
                new ArrayAdapter<>(this, R.layout.statistics_year_selector_layout,
                        UserStartDate.getActiveYears(this));
        adapter.setDropDownViewResource(R.layout.drop_down_selector_item_layout);
        yearSelector.setAdapter(adapter);


        //Initialize The StatisticsFragment/ Queryable IMPL
        final Fragment queryable = StatisticsFragment.newInstance(null);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.local_container, queryable)
                .commit();

        this.queryable = (DateQueryable) queryable;

        //Set range to the current month
        this.queryable.setDate(/*Current Timerange*/ getCurrentMonthlyRange());

    }

    @OnClick(R.id.id_global_nav)
    public void navigate(){
        NavUtils.navigateUpFromSameTask(this);
    }


    private TimeRange getCurrentMonthlyRange(){
       return new TimeRange(Timestamp.fromProjection(Timestamp.now().getMonthDay()).getStartOfDay(),
               System.currentTimeMillis());
    }

}
