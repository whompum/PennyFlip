package com.whompum.PennyFlip;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whompum.PennyFlip.NavMenu.NavMenuAnimator;
import com.whompum.PennyFlip.Source.SourceListActivity.ActivitySourceList;
import com.whompum.PennyFlip.Source.SourceDialog.AddSourceDialog;
import com.whompum.PennyFlip.Source.SourceDialog.SourceDialog;
import com.whompum.PennyFlip.Source.SourceDialog.SourceWrapper;
import com.whompum.PennyFlip.Source.SourceDialog.SpendingSourceDialog;
import com.whompum.pennydialog.dialog.PennyDialog;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public class ActivityDashboard extends AppCompatActivity {


    private CurrencyEditText value;

    private ViewGroup nullTransactionImage;

    private TextView todayValueLabel;
    private CurrencyEditText todayValue;

    private TextView todayValueSecondaryLabel;
    private CurrencyEditText todaySecondaryValue;

    private NavMenuAnimator navMenuAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);


        value = findViewById(R.id.id_dashboard_value);
        nullTransactionImage = findViewById(R.id.id_dashboard_transactions_null_image);
        nullTransactionImage.setVisibility(View.INVISIBLE);

        todayValueLabel = findViewById(R.id.id_dashboard_today_total_label);
        todayValue = findViewById(R.id.id_dashboard_today_value);

        todayValueSecondaryLabel = findViewById(R.id.id_dashboard_today_total_label_secondary);
        todaySecondaryValue = findViewById(R.id.id_dashboard_today_value_secondary);


        this.navMenuAnimator = new NavMenuAnimator();
        navMenuAnimator.bindMenu( ((LinearLayout)findViewById(R.id.id_nav_menu_layout)) ); //Binds the views to the animator

    }




    /*
     * Goal/Callibrate imageButton onClick references
     */
    public void onGoalClicked(final View view){
    }
    public void onCallibrateClicked(final View view){
    }


    /*
     * Add/Spend Dialog Fab onClick references
     */
    public void onPlusFabClicked(final View view){
        final Bundle style = new Bundle();
        style.putInt(PennyDialog.STYLE_KEY, R.style.StylePennyDialogAdd);

        final SlidePennyDialog pennyDialog = (SlidePennyDialog) SlidePennyDialog.newInstance(cashListener, style);
        launchPennyDialog(pennyDialog, SlidePennyDialog.TAG);

    }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 //hi
    public void onMinusFabClicked(final View view){
        final Bundle style = new Bundle();
        style.putInt(PennyDialog.STYLE_KEY, R.style.StylePennyDialogMinus);
        final PennyDialog dialog = SlidePennyDialog.newInstance(minusListener, style);
        launchPennyDialog(dialog, SlidePennyDialog.TAG);
    }


    /*
     * Navigation Fab onClick references
     */
    public void onStatisticsFabClicked(final View view){

    }
    public void onHistoryFabClicked(final View view){

    }
    public void onSourceFabClicked(final View view){
        startActivity(new Intent(this, ActivitySourceList.class));
    }
    public void onAnchorFabClicked(final View view) {
            navMenuAnimator.animate();
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

    private class StateAdjust{

        /**
         * TODO add logic for determining what the state of the users transactions is
         * Then change things as needed. 
         */

        public StateAdjust(){

        }



    }



}
