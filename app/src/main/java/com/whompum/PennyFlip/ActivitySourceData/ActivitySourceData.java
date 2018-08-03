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
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Notes.Model.Note;
import com.whompum.PennyFlip.Notes.NotesListFragment;
import com.whompum.PennyFlip.OnItemSelected;
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

public class ActivitySourceData extends AppCompatActivity implements SourceDataClient, OnItemSelected<Note>{

    public static final String DATA = "source.ky";

    private Source data;
    private SourceDataConsumer server;

    private SourceFragmentAdapter adapter;

    @BindView(R.id.id_source_data_container)
    protected ViewPager container;

    @BindView(R.id.id_fab)
    protected FloatingActionButton fab;

    private PennyDialog pennyDialog;

    private ValueAnimator noteAnimator = ValueAnimator.ofFloat(0F, 1F);

    private ArgbEvaluator clrEvaluator = new ArgbEvaluator();
    private FloatEvaluator evaluator = new FloatEvaluator();

    private int highlight;
    private int notesHighlight;

    private boolean onNotes;

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

        applyColoring(getHighlight());

        setPageIndicator(0); //Setting Displaying the layout for the first tab

    }

    @Override
    public void onSourceChanged(@NonNull Source source) {
        this.data = source; //Will cause temporary data inconsistency issue on configuration change.
        initialize(data);
    }

    @Override
    public void onItemSelected(Note note) {
        Toast.makeText(this, note.getSourceId(), Toast.LENGTH_SHORT).show();
    }

    //Initializes the core UI (title displays, value display, lastUpdate)
    private void initialize(@NonNull final Source data){
        ((TextView)findViewById(R.id.id_source_data_sourcename)).setText(data.getTitle());
        ((CurrencyEditText)findViewById(R.id.id_source_data_value)).setText(String.valueOf(data.getPennies()));

        final String lastUpdate = getString(R.string.string_last_update) + " " + Ts .from(data.getLastUpdate()).getPreferentialDate();

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
        fragments.add(NotesListFragment.obtain(this, data.getTransactionType(), data.getTitle()));

     return fragments;
    }

    @OnPageChange(R.id.id_source_data_container)
    public void setPageIndicator(final int pos){

        if(adapter.getItem(pos) instanceof TransactionFragment) {
            showIndicator(R.id.id_transactions_label, true);
            showIndicator(R.id.id_notes_label, false);
            showIndicator(R.id.id_statistics_label, false);

            if(onNotes) {
                animateForNotes(notesHighlight, highlight, 0F, 1F);
                onNotes = false;
                fab.setClickable(true);
                fab.setFocusable(true);
            }

        }
        else if(adapter.getItem(pos) instanceof tempStatisticsFragment) {
            showIndicator(R.id.id_statistics_label, true);
            showIndicator(R.id.id_transactions_label, false);
            showIndicator(R.id.id_notes_label, false);

            if(onNotes) {
                animateForNotes(notesHighlight, highlight, 0F, 1F);
                onNotes = false;
                fab.setClickable(true);
                fab.setFocusable(true);
            }

        }

        else if(adapter.getItem(pos) instanceof NotesListFragment) {
            showIndicator(R.id.id_notes_label, true);
            showIndicator(R.id.id_statistics_label, false);
            showIndicator(R.id.id_transactions_label, false);
            animateForNotes(highlight, notesHighlight, 1F, 0F);
            onNotes = true;
            fab.setClickable(false);
            fab.setFocusable(false);

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

    private void showIndicator(final int id, boolean show){
        if(show)
            findViewById(id).setVisibility(View.VISIBLE);
        else
            findViewById(id).setVisibility(View.GONE);
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

    @ColorRes
    private int getHighlight(){
        return (data.getTransactionType() == TransactionType.ADD ) ? R.color.light_green : R.color.light_red;
    }

    private void applyColoring(final int clr){
        //Applies the highlight color

        if(Build.VERSION.SDK_INT >= 24) {
            highlight = getColor(clr);
            notesHighlight = getColor(R.color.light_blue);
        }else {
            highlight = getResources().getColor(clr);
            notesHighlight = getResources().getColor(R.color.light_blue);
        }
        color(highlight);

        ((FloatingActionButton)findViewById(R.id.id_fab)).setImageResource(
                (data.getTransactionType() == TransactionType.ADD) ? R.drawable.graphic_plus_green :
                R.drawable.graphic_minus_red);
    }

    private void color(final int clr/**Resolved Color*/){
        findViewById(R.id.source_data_header).setBackgroundColor(clr);
        findViewById(R.id.source_data_value_container).setBackgroundColor(clr);
        findViewById(R.id.source_data_nav_container).setBackgroundColor(clr);
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


    private void animateForNotes(final int clrFrom, final int clrTo, final float scaleFrom, final float scaleTo){

        noteAnimator.setInterpolator(new DecelerateInterpolator());

        noteAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final int color = (Integer)
                        clrEvaluator.evaluate(animation.getAnimatedFraction(), clrFrom, clrTo);
                color(color);

                final Float fabScale = evaluator.evaluate(animation.getAnimatedFraction(), scaleFrom, scaleTo);

                fab.setScaleX(fabScale);
                fab.setScaleY(fabScale);

            }
        });
        noteAnimator.start();
    }

}
