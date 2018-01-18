package com.whompum.PennyFlip.ActivityDashboard;

import android.animation.ArgbEvaluator;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.whompum.PennyFlip.ActivityHistory.ActivityHistory;
import com.whompum.PennyFlip.Animations.PageTitleStrips;
import com.whompum.PennyFlip.Data.Providers.WalletProvider;
import com.whompum.PennyFlip.Data.Schemas.SourceSchema;
import com.whompum.PennyFlip.Data.Schemas.TransactionsSchema;
import com.whompum.PennyFlip.Data.Schemas.WalletSchema;
import com.whompum.PennyFlip.Data.Storage.WalletHelper;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.SlidePennyDialog;
import com.whompum.PennyFlip.ActivitySourceList.ActivitySourceList;
import com.whompum.PennyFlip.DialogSourceChooser.AddSourceDialog;
import com.whompum.PennyFlip.DialogSourceChooser.SourceDialog;
import com.whompum.PennyFlip.Source.SourceWrapper;
import com.whompum.PennyFlip.DialogSourceChooser.SpendingSourceDialog;
import com.whompum.PennyFlip.Widgets.StickyViewPager;
import com.whompum.pennydialog.dialog.PennyDialog;

import java.util.UUID;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public class ActivityDashboard extends AppCompatActivity {

    private View colorBackground;
    private ArgbEvaluator argb = new ArgbEvaluator();
    private int sClr;
    private int eClr;



    private CurrencyEditText value;

    private ViewPager addSpendContainer;
    private ViewGroup stripsLayout;

    private PageTitleStrips strips;

    private FloatingActionButton addFab;

    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        //View whose background will change color as the ViewPager is swiped :)
        colorBackground = findViewById(R.id.dashboard_colored_background);

        if(Build.VERSION.SDK_INT >= 23) {
            sClr = getColor(R.color.light_green);
            eClr = getColor(R.color.light_red);
        }
        else {
            sClr = getResources().getColor(R.color.light_green);
            eClr = getResources().getColor(R.color.light_red);
        }

        this.addFab = findViewById(R.id.id_fab );
             addFab.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                    choreographFabClick();
                 }
             });

        value = findViewById(R.id.id_dashboard_value);

        addSpendContainer = findViewById(R.id.id_fragment_container);

        stripsLayout = findViewById(R.id.id_strips_indicator);

        initTodayFragments();

        this.vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        Log.i("SCHEMA", SourceSchema.SourceTable.CREATE_TABLE);
        //TODO Add Purse/MessageSchema
    }

    private void initTodayFragments(){
        addSpendContainer.setAdapter(new TodayFragmentAdapter(getSupportFragmentManager()));

        strips = new PageTitleStrips(stripsLayout, stripClick);
        strips.bindTitle(this, getString(R.string.string_adding));
        strips.bindTitle(this, getString(R.string.string_spending));

        addSpendContainer.addOnPageChangeListener(pageChangeListener);
        addSpendContainer.setPageTransformer(true, pageTransformer);

        addSpendContainer.addOnLayoutChangeListener(new SimpleLayoutChange(){
            @Override
            public void onLayoutChange(int top) {
                viewCenterYToTop(addFab, addSpendContainer, top);
            }
        });

    }

    PageTitleStrips.StripClick stripClick = new PageTitleStrips.StripClick() {
        @Override
        public void onStripClicked(int position) {

            if(addSpendContainer.getCurrentItem() != position) {
                addSpendContainer.setCurrentItem(position);
                vibrate(100L);
            }

        }
    };

    ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            strips.setPosition(position);
        }
    };



    ViewPager.PageTransformer pageTransformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(View page, float position) {

            if(  !((StickyViewPager)addSpendContainer).isDragging() ) {
                final int color = (Integer) argb.evaluate((position), eClr, sClr);
                colorBackground.setBackgroundColor(color);
            }

            Log.i("POSITION", "D: " + String.valueOf(position));
            //setFabProgress(position);
        }
    };





    public void onCallibrateClicked(final View view){
        vibrator.vibrate(100L);
    }


    private void choreographFabClick(){

        if(addSpendContainer.getCurrentItem() == 0)
            onPlusFabClicked();
        else
            onMinusFabClicked();

    }

    /*
     * Add/Spend Dialog Fab onClick references
     */
    public void onPlusFabClicked(){

        final Bundle style = new Bundle();
        style.putInt(PennyDialog.STYLE_KEY, R.style.StylePennyDialogAdd);

        final SlidePennyDialog pennyDialog = (SlidePennyDialog) SlidePennyDialog.newInstance(cashListener, style);
        launchPennyDialog(pennyDialog, SlidePennyDialog.TAG);

    }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 //hi
    public void onMinusFabClicked(){
        final Bundle style = new Bundle();
        style.putInt(PennyDialog.STYLE_KEY, R.style.StylePennyDialogMinus);
        final PennyDialog dialog = SlidePennyDialog.newInstance(minusListener, style);
        launchPennyDialog(dialog, SlidePennyDialog.TAG);
    }


    /*
     * Navigation Fab onClick references
     */
    public void onStatisticsFabClicked(final View view){
        vibrate(100L);

    }
    public void onHistoryFabClicked(final View view){
        vibrate(100L);
        Toast.makeText(this, "fuck", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, ActivityHistory.class));
    }
    public void onSourceFabClicked(final View view){
        vibrate(100L);
        startActivity(new Intent(this, ActivitySourceList.class));
    }


    private final PennyDialog.CashChangeListener cashListener = new PennyDialog.CashChangeListener() {
        @Override
        public void onPenniesChange(long l) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    final SourceDialog addSourceDialog = AddSourceDialog.newInstance(null);
                    launchPennyDialog(addSourceDialog, AddSourceDialog.TAG);
                    addSourceDialog.registerItemSelectedListener(new SourceDialog.OnSourceItemSelected() {
                        @Override
                        public void onSourceItemSelected(SourceWrapper wrapper) {
                            Toast.makeText(getBaseContext(), wrapper.getTitle(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, 500L);

        }

        @Override
        public void onCashChange(String s) {

        }
    };
    private final PennyDialog.CashChangeListener minusListener = new PennyDialog.CashChangeListener() {
        @Override
        public void onPenniesChange(long l) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SourceDialog dialog = SpendingSourceDialog.newInstance(null);
                    launchPennyDialog(dialog, SpendingSourceDialog.TAG);
                }
            }, 500L);

        }

        @Override
        public void onCashChange(String s) {

        }
    };



    /**
     *
     * @param dialog Dialog to show
     * @param tag the tag to associate with the dialog
     */
    private void launchPennyDialog(final DialogFragment dialog, final String tag){
        dialog.show(getSupportFragmentManager(), tag);
    }


    //Helper method that will position a views center Y to the top of another View.
    private static void viewCenterYToTop(final View subject, final View object, final int top){
        subject.setTranslationY( (top  +  object.getPaddingTop()) //Top + padding
                - (subject.getHeight()  /  2));

    }

    private void vibrate(final long ms){
        if(vibrator.hasVibrator())
            vibrator.vibrate(ms);
    }


}
