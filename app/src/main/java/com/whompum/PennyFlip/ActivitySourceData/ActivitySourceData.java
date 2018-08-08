package com.whompum.PennyFlip.ActivitySourceData;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.whompum.PennyFlip.Animations.PageTitleStrips;
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

public class ActivitySourceData extends AppCompatActivity implements SourceDataClient{

    public static final String DATA = "source.ky";

    private Source data;
    private SourceDataConsumer server;

    private SourceFragmentAdapter adapter;

    @BindView(R.id.id_global_pager)
    protected ViewPager container;

    @BindView(R.id.id_global_fab)
    protected FloatingActionButton fab;

    private PennyDialog pennyDialog;

    private PageTitleStrips strips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.source);

        ButterKnife.bind(this);

        this.data = (Source) getIntent().getSerializableExtra(DATA);

        server = new SourceDataController(this, this);
        server.observeSource(data.getTitle(), this);

        initialize(data);

        adapter = new SourceFragmentAdapter(getSupportFragmentManager(), getFragments());
        container.setAdapter(adapter);

        setPageIndicator(0); //Setting Displaying the layout for the first tab

    }

    @Override
    public void onSourceChanged(@NonNull Source source) {
        this.data = source; //Will cause temporary data inconsistency issue on configuration change.
        initialize(data);
    }


    //Initializes the core UI (title displays, value display, lastUpdate)
    private void initialize(@NonNull final Source data){
        ((TextView)findViewById(R.id.id_global_title)).setText(data.getTitle());
        ((CurrencyEditText)findViewById(R.id.id_global_total_display)).setText(String.valueOf(data.getPennies()));

        final String lastUpdate = getString(R.string.string_last_update) + " " + Ts .from(data.getLastUpdate()).getPreferentialDate();

        ((TextView)findViewById(R.id.id_global_timestamp)).setText(lastUpdate);

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

        strips = new PageTitleStrips((ViewGroup)findViewById(R.id.id_global_strips_indicator), null);

        strips.bindTitle(this, "Transactions");
        strips.bindTitle(this, "Statistics");

     return fragments;
    }

    @OnPageChange(R.id.id_global_pager)
    public void setPageIndicator(final int pos){
        strips.setPosition(pos);
    }

    @OnClick(R.id.id_global_nav)
    public void navigate(){
        NavUtils.navigateUpFromSameTask(this);
    }

    @OnClick(R.id.ic_delete_source)
    public void delete() {
        launchDeleteConfDialog();
    }

    @OnClick(R.id.id_global_fab)
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
        server.deleteSource(data.getTitle());
        server.unObserve(this);
        finish();
    }


    private void launchTransactionNameDialog(@NonNull final long pennies){

        new TransactionTitleDialog(this, new OnTitleListener() {
            @Override
            public void onTitleSet(@NonNull String title) {
                final Transaction transaction = new Transaction(data.getTitle() ,data.getTransactionType(), pennies);
                transaction.setTitle(title);
                server.saveTransaction(transaction);
            }
        });

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


}
