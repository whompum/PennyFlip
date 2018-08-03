package com.whompum.PennyFlip.Dashboard;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.PennyFlip.ActivityHistory.ActivityHistory;
import com.whompum.PennyFlip.Animations.PageTitleStrips;
import com.whompum.PennyFlip.DialogSourceChooser.OnSourceItemSelected;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.SlidePennyDialog;
import com.whompum.PennyFlip.ActivitySourceList.ActivitySourceList;
import com.whompum.PennyFlip.DialogSourceChooser.AddSourceDialog;
import com.whompum.PennyFlip.DialogSourceChooser.SourceDialog;
import com.whompum.PennyFlip.DialogSourceChooser.SourceWrapper;
import com.whompum.PennyFlip.DialogSourceChooser.SpendingSourceDialog;
import com.whompum.PennyFlip.Time.Ts;
import com.whompum.PennyFlip.Widgets.StickyViewPager;
import com.whompum.pennydialog.dialog.PennyDialog;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;
import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public class ActivityDashboard extends AppCompatActivity implements DashboardClient{

    private ActivityDashboardConsumer consumer;
    private ArgbEvaluator argb = new ArgbEvaluator();
    private PageTitleStrips strips;

    private Vibrator vibrator;

    private PennyDialog pennyDialog; //Reference is stored so we can prevent accidental instantiation overload

    @BindColor(R.color.light_green)
    protected int sClr;

    @BindColor(R.color.light_red)
    protected int eClr;

    @BindView(R.id.dashboard_colored_background)
    protected View colorBackground;

    @BindView(R.id.id_global_total_display)
    protected CurrencyEditText value;

    @BindView(R.id.id_global_pager)
    protected ViewPager addSpendContainer;

    @BindView(R.id.id_global_strips_indicator)
    protected ViewGroup stripsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        ButterKnife.bind(this);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        consumer = DashboardController.create(this).bindClient(this);

        initTodayFragments();

        setTodayTimeLabel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        consumer.bindWalletObserver(this);
    }


    private void initTodayFragments(){
        addSpendContainer.setAdapter(new TodayFragmentAdapter(getSupportFragmentManager()));

        strips = new PageTitleStrips(stripsLayout, stripClick);
        strips.bindTitle(this, getString(R.string.string_adding));
        strips.bindTitle(this, getString(R.string.string_spending));

        addSpendContainer.setPageTransformer(true, pageTransformer);
    }

    private void setTodayTimeLabel(){
        ((TextView)findViewById(R.id.id_global_timestamp))
                .setText(Ts.now().simpleDate());
    }

    PageTitleStrips.StripClick stripClick = new PageTitleStrips.StripClick() {
        @Override
        public void onStripClicked(int position) {

            if(addSpendContainer.getCurrentItem() != position) {
                addSpendContainer.setCurrentItem(position);
                vibrate(500L);
            }

        }
    };

    @OnPageChange(R.id.id_global_pager)
    public void onPageSelected(final int p){
        strips.setPosition(p);

        if(p == 0)
            ((FloatingActionButton)findViewById(R.id.id_global_fab)).setImageResource(R.drawable.graphic_plus_green);

        else if(p == 1)
            ((FloatingActionButton)findViewById(R.id.id_global_fab)).setImageResource(R.drawable.graphic_minus_red);

    }


    ViewPager.PageTransformer pageTransformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(View page, float position) {
            if(  !((StickyViewPager)addSpendContainer).isDragging() ) {
                final int color = (Integer) argb.evaluate((position), eClr, sClr);
                colorBackground.setBackgroundColor(color);
            }
        }
    };

    @Override
    public void onWalletChanged(long pennies) {
        Log.i("WALLET_FIX", "onWalletChanged(long)#ActivityDashboard new wallet value: " + pennies);
        value.setText(String.valueOf(pennies));
    }

    @OnClick(R.id.dashboard_local_calibrate)
    public void callibrate(){
        vibrate(500L);
    }

    @OnClick(R.id.id_global_fab)
    void onFabClicked() {

        if(pennyDialog != null)  //Done to block double tapping; Avoids creating multiple instances.
            if(pennyDialog.isAdded()) return;


        vibrate(500L);
        int styleRes;

        PennyDialog.CashChangeListener ccL;

        if (addSpendContainer.getCurrentItem() == 0) { //Is adding transaction
            styleRes = R.style.StylePennyDialogAdd;
            ccL = cashListener;
        } else { //Is probably a spending transaction
            styleRes = R.style.StylePennyDialogMinus;
            ccL = minusListener;
        }

        final Bundle args = new Bundle();
        args.putInt(PennyDialog.STYLE_KEY, styleRes);

        this.pennyDialog = SlidePennyDialog.newInstance(ccL, args);

        launchPennyDialog(pennyDialog, SlidePennyDialog.TAG);
    }

    @OnClick(R.id.id_nav_statistics)
    public void onStatisticsFabClicked(){
        vibrate(500L);
    }

    @OnClick(R.id.id_nav_menu_history)
    public void onHistoryFabClicked(){
        vibrate(500L);
        startActivity(new Intent(this, ActivityHistory.class));
    }

    @OnClick(R.id.id_nav_source)
    public void onSourceFabClicked(){
        vibrate(500L);
        startActivity(new Intent(this, ActivitySourceList.class));
    }

    private final PennyDialog.CashChangeListener cashListener = new PennyDialog.CashChangeListener() {
        @Override
        public void onPenniesChange(final long pennies) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    final SourceDialog addSourceDialog = AddSourceDialog.newInstance(generateTransactionDialogArgs(pennies));
                    launchPennyDialog( addSourceDialog, AddSourceDialog.TAG);

                    addSourceDialog.registerItemSelectedListener(new OnSourceItemSelected() {
                        @Override
                        public void onSourceItemSelected(@NonNull SourceWrapper wrapper, @NonNull Transaction transaction) {
                            consumer.saveTransaction(wrapper, transaction);
                        }
                    });
                }
            }, 500L); //Delayed so the PennyDialog can finish its exit animation.

        }

        @Override
        public void onCashChange(String s) {

        }
    };

    private final PennyDialog.CashChangeListener minusListener = new PennyDialog.CashChangeListener() {
        @Override
        public void onPenniesChange(final long pennies) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    SourceDialog dialog = SpendingSourceDialog.newInstance(generateTransactionDialogArgs(pennies));
                    launchPennyDialog(dialog, SpendingSourceDialog.TAG);

                    dialog.registerItemSelectedListener(new OnSourceItemSelected() {
                        @Override
                        public void onSourceItemSelected(@NonNull SourceWrapper wrapper, @NonNull Transaction transaction) {
                            consumer.saveTransaction(wrapper, transaction);
                        }
                    });
                }
            }, 500L);

        }

        @Override
        public void onCashChange(String s) {

        }
    };

    private Bundle generateTransactionDialogArgs(final long pennies){
        final Bundle bundle = new Bundle();
        bundle.putLong(SourceDialog.TOTAL_KEY, pennies);
        return bundle;
    }

    /**
     * @param dialog Dialog to show
     * @param tag the tag to associate with the dialog
     */
    private void launchPennyDialog(final DialogFragment dialog, final String tag){
        dialog.show(getSupportFragmentManager(), tag);
    }


    private void vibrate(final long millis){

        if(!vibrator.hasVibrator())
            return;

        if(Build.VERSION.SDK_INT >= 26)
            vibrator.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE));

        else vibrator.vibrate(millis);
    }

}
