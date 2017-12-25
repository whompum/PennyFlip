package com.whompum.PennyFlip;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whompum.PennyFlip.NavMenu.NavMenuAnimator;
import com.whompum.pennydialog.dialog.PennyDialog;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public class ActivityDashboard extends AppCompatActivity {


    private CurrencyEditText value;
    private ImageButton goal;
    private ImageButton callibrate;

    private ViewGroup nullTransactionImage;

    private TextView valueSecondaryLabel;
    private CurrencyEditText valueSecondary;

    private TextView valueTertiaryLabel;
    private CurrencyEditText valueTertiary;

    private NavMenuAnimator navMenuAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);


        findViewById(R.id.id_dashboard_value);

        findViewById(R.id.id_nav_menu_statistics).setOnClickListener(this.statsListener);
        findViewById(R.id.id_nav_menu_history).setOnClickListener(this.historyListener);
        findViewById(R.id.id_nav_menu_source).setOnClickListener(this.sourcesListener);
        findViewById(R.id.id_nav_menu_anchor).setOnClickListener(this.anchorListner);

        this.navMenuAnimator = new NavMenuAnimator();
        navMenuAnimator.bindMenu( ((LinearLayout)findViewById(R.id.id_nav_menu_layout)) ); //Binds the views to the animator

        findViewById(R.id.id_dashboard_transactions_null_image).setVisibility(View.INVISIBLE);

    }



    private final View.OnClickListener anchorListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navMenuAnimator.animate();
        }
    };

    private final View.OnClickListener statsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private final View.OnClickListener historyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private final View.OnClickListener sourcesListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };



    public void onGoalClicked(final View view){
    }
    public void onCallibrateClicked(final View view){
    }


    public void onPlusFabClicked(final View view){
    }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 //hi



    public void onMinusFabClicked(final View view){
        final Bundle style = new Bundle();
        style.putInt(PennyDialog.STYLE_KEY, R.style.MinusPennyDialogStyle);
        launchPennyDialog(style, null);
    }



    /**
     * Convenience method that launches the PennyDialog Dialog window.
     *
     *
     * @param pennyArgs Styling bundle
     * @param cashChangeListener Listener
     */
    private void launchPennyDialog(@Nullable final Bundle pennyArgs, @Nullable PennyDialog.CashChangeListener cashChangeListener){
        PennyDialog.newInstance(cashChangeListener, pennyArgs)
                .show(getSupportFragmentManager(), PennyDialog.TAG);
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
