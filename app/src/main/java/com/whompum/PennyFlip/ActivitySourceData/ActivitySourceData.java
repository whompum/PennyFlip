package com.whompum.PennyFlip.ActivitySourceData;


import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whompum.PennyFlip.Animations.PageTitleStrips;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.Adapters.SourceFragmentAdapter;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public abstract class ActivitySourceData extends AppCompatActivity implements View.OnClickListener {

    public static final String SOURCE_NAME_KEY = "SOURCE_NAME";
    public static final String SOURCE_TIMESTAMP = "SOURCE_TIMESTAMP";
    public static final String SOURCE_TOTAL = "SOURCE_TOTAL";


    protected AppCompatImageButton upNav;
    protected TextView sourceNameLabel;
    protected TextView valueLabel;
    protected CurrencyEditText value;
    protected TextView sourceUpdateTimestamp;

    private LinearLayout titleIndicator;

    protected String sourceName;
    protected String lastUpdate;
    protected long sourcePennies;

    private ViewPager container;

    private SourceFragmentAdapter adapter;

    private PageTitleStrips strips;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.source);

        upNav = findViewById(R.id.id_source_data_up);
        upNav.setOnClickListener(this);
        sourceNameLabel = findViewById(R.id.id_source_data_sourcename);

        valueLabel = findViewById(R.id.id_source_data_value_label);
        value = findViewById(R.id.id_source_data_value);

        sourceUpdateTimestamp = findViewById(R.id.id_source_data_value_timestamp);

        this.titleIndicator = findViewById(R.id.id_source_data_title_indicator);

        this.container = findViewById(R.id.id_source_data_container);

        sourceName = getIntent().getStringExtra(SOURCE_NAME_KEY);
        lastUpdate = getIntent().getStringExtra(SOURCE_TIMESTAMP);
        sourcePennies = getIntent().getLongExtra(SOURCE_TOTAL, 900L);

        if(sourceName != null)
            sourceNameLabel.setText(sourceName);

        if(lastUpdate != null) {
         final String lastUpdateConcat = getString(R.string.string_last_update) + " " + lastUpdate;
            sourceUpdateTimestamp.setText(lastUpdateConcat);
        }

        value.setText(String.valueOf(sourcePennies));

        strips = new PageTitleStrips(titleIndicator, null);

        adapter = new SourceFragmentAdapter(getSupportFragmentManager());
        populateFragmentAdapter(adapter);

        container.setAdapter(adapter);
        container.addOnPageChangeListener(d);
    }




    final ViewPager.SimpleOnPageChangeListener d = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            strips.setPosition(position);
        }

    };



    @Override
    protected void onStart() {
        super.onStart();
        populateFragments();
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.id_source_data_up)
            NavUtils.navigateUpFromSameTask(this);

    }

    protected abstract void populateFragmentAdapter(final SourceFragmentAdapter adapter);

    protected abstract void populateFragments();


    protected void bindStrip(final String title){
        strips.bindTitle(this, title);
    }

}
