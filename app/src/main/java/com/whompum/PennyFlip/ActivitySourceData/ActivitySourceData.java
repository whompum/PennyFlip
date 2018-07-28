package com.whompum.PennyFlip.ActivitySourceData;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whompum.PennyFlip.Animations.PageTitleStrips;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.Adapters.SourceFragmentAdapter;
import com.whompum.PennyFlip.Time.Ts;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public abstract class ActivitySourceData extends AppCompatActivity{

    public static final String DATA = "source.ky";

    protected Source data;
    private PageTitleStrips strips;
    private SourceFragmentAdapter adapter;


    @BindView(R.id.id_source_data_sourcename)
    protected TextView sourceNameLabel;

    @BindView(R.id.id_source_data_value_label)
    protected TextView valueLabel;

    @BindView(R.id.id_source_data_value)
    protected CurrencyEditText value;

    @BindView(R.id.id_source_data_value_timestamp)
    protected TextView sourceUpdateTimestamp;

    @BindView(R.id.id_source_data_title_indicator)
    protected LinearLayout titleIndicator;

    @BindView(R.id.id_source_data_container)
    protected ViewPager container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.source);

        ButterKnife.bind(this);

        this.container = findViewById(R.id.id_source_data_container);

        this.data = initMetaData(getIntent());

        sourceNameLabel.setText(data.getTitle());
        value.setText(String.valueOf(data.getPennies()));

        final Ts s = Ts.from(data.getCreationDate());

        final String cD = s.getMonth() + "/" + s.getYear();

        sourceUpdateTimestamp.setText(cD);

        strips = new PageTitleStrips(titleIndicator, new PageTitleStrips.StripClick() {
            @Override
            public void onStripClicked(int position) {
                container.setCurrentItem(position);
            }
        });

        adapter = new SourceFragmentAdapter(getSupportFragmentManager());
        populateFragmentAdapter(adapter);

        container.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateFragments();
    }

    protected abstract Source initMetaData(final Intent intent);
    protected abstract void populateFragmentAdapter(final SourceFragmentAdapter adapter);
    protected abstract void populateFragments();

    @OnPageChange(R.id.id_source_data_container)
    public void updateStrips(final int pos){
        strips.setPosition(pos);
    }


    @OnClick(R.id.id_up_navigation)
    public void navigate(){
        NavUtils.navigateUpFromSameTask(this);
    }

    @OnClick(R.id.ic_delete_source)
    public void delete() {
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

    }

    protected void bindStrip(final String title){
        strips.bindTitle(this, title);
    }

}
