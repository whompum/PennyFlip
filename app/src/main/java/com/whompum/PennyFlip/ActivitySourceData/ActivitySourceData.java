package com.whompum.PennyFlip.ActivitySourceData;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whompum.PennyFlip.Animations.PageTitleStrips;
import com.whompum.PennyFlip.Data.Schemas.SourceSchema;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.Adapters.SourceFragmentAdapter;
import com.whompum.PennyFlip.Sources.SourceMetaData;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public abstract class ActivitySourceData extends AppCompatActivity implements View.OnClickListener {

    public static final String SOURCE_KEY = "source.ky";

    @StringRes
    public static final int LAST_UPDATE_PREFIX = R.string.string_last_update;

    protected AppCompatImageButton upNav;
    protected TextView sourceNameLabel;
    protected TextView valueLabel;
    protected CurrencyEditText value;
    protected TextView sourceUpdateTimestamp;

    private LinearLayout titleIndicator;

    protected SourceMetaData data;

    private ViewPager container;

    private SourceFragmentAdapter adapter;

    private PageTitleStrips strips;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.source);

        upNav = findViewById(R.id.id_up_navigation);
        upNav.setOnClickListener(this);
        sourceNameLabel = findViewById(R.id.id_source_data_sourcename);

        valueLabel = findViewById(R.id.id_source_data_value_label);
        value = findViewById(R.id.id_source_data_value);

        sourceUpdateTimestamp = findViewById(R.id.id_source_data_value_timestamp);

        this.titleIndicator = findViewById(R.id.id_source_data_title_indicator);

        this.container = findViewById(R.id.id_source_data_container);

        this.data = initMetaData(getIntent());


        sourceNameLabel.setText(data.getSourceName());
        value.setText(String.valueOf(data.getPennies()));

        final String lastUpdatePrefix = getString(LAST_UPDATE_PREFIX);

        if(data.hasLastUpdate()) {
         final String lastUpdate = lastUpdatePrefix + data.getLastUpdateSimpleTime();
            sourceUpdateTimestamp.setText(lastUpdate);
        }

        strips = new PageTitleStrips(titleIndicator, new PageTitleStrips.StripClick() {
            @Override
            public void onStripClicked(int position) {
                container.setCurrentItem(position);
            }
        });

        adapter = new SourceFragmentAdapter(getSupportFragmentManager());
        populateFragmentAdapter(adapter);

        container.setAdapter(adapter);
        container.addOnPageChangeListener(d);

        findViewById(R.id.ic_delete_source).setOnClickListener(this);
    }

    protected abstract SourceMetaData initMetaData(final Intent intent);



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

        if(v.getId() == R.id.id_up_navigation)
            NavUtils.navigateUpFromSameTask(this);

        if(v.getId() == R.id.ic_delete_source)
            launchDeleteConfDialog();

    }


    private void launchDeleteConfDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog);

        final AlertDialog dialog = builder.setTitle(R.string.string_delete_conf_title)
               .setMessage(R.string.string_delete_conf_message)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteSource();
            }
        }).create();

        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

        if(params != null)
            params.windowAnimations = R.style.StyleDialogAnimate;

        dialog.show();
    }

    private void deleteSource(){

        final String where = SourceSchema.SourceTable._ID + "=?";
        final String[] args = {String.valueOf(data.getId())};

        final int numRows = getContentResolver().delete(SourceSchema.SourceTable.URI, where, args);

        if(numRows > 0)
            finish();

    }

    protected abstract void populateFragmentAdapter(final SourceFragmentAdapter adapter);

    protected abstract void populateFragments();


    protected void bindStrip(final String title){
        strips.bindTitle(this, title);
    }

}
