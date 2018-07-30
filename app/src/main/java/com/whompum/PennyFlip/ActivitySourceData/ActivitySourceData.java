package com.whompum.PennyFlip.ActivitySourceData;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.whompum.PennyFlip.Money.MoneyController;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.PennyListener;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.Adapters.SourceFragmentAdapter;
import com.whompum.PennyFlip.SlidePennyDialog;
import com.whompum.PennyFlip.Time.Ts;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.Transactions.OnTitleListener;
import com.whompum.PennyFlip.Transactions.TransactionFragment;
import com.whompum.PennyFlip.Transactions.TransactionTitleDialog;
import com.whompum.pennydialog.dialog.PennyDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import butterknife.OnPageChange;
import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public class ActivitySourceData extends AppCompatActivity{

    public static final String DATA = "source.ky";

    protected Source data;
    private SourceFragmentAdapter adapter;

    @BindView(R.id.id_source_data_container)
    protected ViewPager container;

    private PennyDialog pennyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.source);

        ButterKnife.bind(this);

        this.data = (Source) getIntent().getSerializableExtra(DATA);

        initialize(data);

        adapter = new SourceFragmentAdapter(getSupportFragmentManager(), getFragments());
        container.setAdapter(adapter);

        applyColoring(getHighlight());

        setPageIndicator(0); //Setting Displaying the layout for the first tab
    }

    //Initializes the core UI (title displya, value display, lastUpdate)
    private void initialize(@NonNull final Source data){
        ((TextView)findViewById(R.id.id_source_data_sourcename)).setText(data.getTitle());
        ((CurrencyEditText)findViewById(R.id.id_source_data_value)).setText(String.valueOf(data.getPennies()));

        final String lastUpdate = getString(R.string.string_last_update) + " " + Ts.getPreferentialDate(Ts.from(data.getLastUpdate()));

        ((TextView)findViewById(R.id.id_source_data_value_timestamp)).setText(lastUpdate);

    }

    private List<Fragment> getFragments(){
        List<Fragment> fragments = new ArrayList<>(3);

        final Bundle bundle = new Bundle();
        bundle.putString(TransactionFragment.SOURCE_KEY, data.getTitle());


        int color = -1;
        int colorDark = -1;

        if(data.getTransactionType() == TransactionType.ADD){
            color = R.color.light_green;
            colorDark = R.color.dark_green;
        }else  if(data.getTransactionType() == TransactionType.SPEND){
            color = R.color.light_red;
            colorDark = R.color.dark_red;
        }

        bundle.putInt(TransactionFragment.HIGHLIGHT_KEY, color);
        bundle.putInt(TransactionFragment.HIGHLIGHT_DARK_KEY, colorDark);

        fragments.add(TransactionFragment.newInstance(bundle));
        fragments.add(new tempStatisticsFragment());
        fragments.add(new tempNotesFrag());

     return fragments;
    }

    @OnPageChange(R.id.id_source_data_container)
    public void setPageIndicator(final int pos){

        if(adapter.getItem(pos) instanceof TransactionFragment) {
            findViewById(R.id.id_transactions_label).setVisibility(View.VISIBLE);
            findViewById(R.id.id_notes_label).setVisibility(View.GONE);
            findViewById(R.id.id_statistics_label).setVisibility(View.GONE);
        }
        else if(adapter.getItem(pos) instanceof tempStatisticsFragment) {
            findViewById(R.id.id_transactions_label).setVisibility(View.GONE);
            findViewById(R.id.id_statistics_label).setVisibility(View.VISIBLE);
            findViewById(R.id.id_notes_label ).setVisibility(View.GONE);
        }

        else if(adapter.getItem(pos) instanceof tempNotesFrag) {
            findViewById(R.id.id_notes_label).setVisibility(View.VISIBLE);
            findViewById(R.id.id_statistics_label).setVisibility(View.GONE);
            findViewById(R.id.id_transactions_label).setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.id_up_navigation)
    public void navigate(){
        NavUtils.navigateUpFromSameTask(this);
    }

    @OnClick(R.id.ic_delete_source)
    public void delete() {
        launchDeleteConfDialog();
    }

    @OnClick(R.id.id_fab)
    public void onFabClicked(){
        if(pennyDialog != null)  //Done to block double tapping; Avoids creating multiple instances.
            if(pennyDialog.isAdded()) return;

        //vibrate(500L);
        int styleRes;

        if (data.getTransactionType() == TransactionType.ADD)  //Is adding transaction
            styleRes = R.style.StylePennyDialogAdd;
        else  //Is probably a spending transaction
            styleRes = R.style.StylePennyDialogMinus;

        final Bundle args = new Bundle();
        args.putInt(PennyDialog.STYLE_KEY, styleRes);

        this.pennyDialog = SlidePennyDialog.newInstance(pennyListener, args);

        pennyDialog.show(getSupportFragmentManager(), SlidePennyDialog.TAG);
    }

    @OnClick(R.id.id_nav_transactions)
    public void launchTransactions(){
        container.setCurrentItem(0);
    }

    @OnClick(R.id.id_nav_statistics)
    public void launchStatistics(){
        container.setCurrentItem(1);
    }

    @OnClick(R.id.id_nav_notes)
    public void launchNotes(){
        container.setCurrentItem(2);
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
        MoneyController.obtain(this).deleteSource(data.getTitle());
        finish();
    }

    private PennyListener pennyListener = new PennyListener() {
        @Override
        public void onPenniesChange(final long l) {
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            launchTransactionNameDialog(l);
                        }
                    }
            , 500L);
        }
    };

    private void launchTransactionNameDialog(@NonNull final long pennies){

        new TransactionTitleDialog(this, new OnTitleListener() {
            @Override
            public void onTitleSet(@NonNull String title) {

                final Transaction transaction = new Transaction(data.getTitle()
                ,data.getTransactionType(), pennies);

                transaction.setTitle(title);

                MoneyController.obtain(ActivitySourceData.this)
                        .updateSourceAmount(transaction);
            }
        });

    }

    @ColorRes
    private int getHighlight(){
        return (data.getTransactionType() == TransactionType.ADD ) ? R.color.light_green : R.color.light_red;
    }

    private void applyColoring(final int highlight){
        //Applies the highlight color

        int color;

        if(Build.VERSION.SDK_INT >= 24)
            color = getColor(highlight);
        else
            color = getResources().getColor(highlight);

        findViewById(R.id.source_data_header).setBackgroundColor(color);
        findViewById(R.id.source_data_value_container).setBackgroundColor(color);
        findViewById(R.id.source_data_nav_container).setBackgroundColor(color);

        ((FloatingActionButton)findViewById(R.id.id_fab)).setImageResource(
                (data.getTransactionType() == TransactionType.ADD) ? R.drawable.ic_shape_plus_green :
                R.drawable.ic_shape_minus_red);
    }

}
