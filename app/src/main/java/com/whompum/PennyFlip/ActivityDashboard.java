package com.whompum.PennyFlip;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whompum.PennyFlip.NavMenu.NavMenuAnimator;
import com.whompum.pennydialog.dialog.PennyDialog;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public class ActivityDashboard extends AppCompatActivity {


    private RecyclerView transactionsList;
    private CurrencyEditText dashboardValue;
    private TextView lastTransactionTimestamp;

    private NavMenuAnimator navMenuAnimator;

    private FloatingActionButton statsFab;
    private FloatingActionButton historyFab;
    private FloatingActionButton sourcesFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);


        initViews();
        toggleNullTransactionImage();
    }


    private void initViews(){
        transactionsList = findViewById(R.id.id_dashboard_transactionlist);
        dashboardValue = findViewById(R.id.id_dashboard_value);
        lastTransactionTimestamp = findViewById(R.id.id_dashboard_timestamp);

        findViewById(R.id.id_nav_menu_statistics).setOnClickListener(this.statsListener);
        findViewById(R.id.id_nav_menu_history).setOnClickListener(this.historyListener);
        findViewById(R.id.id_nav_menu_source).setOnClickListener(this.sourcesListener);

        FloatingActionButton anchor = findViewById(R.id.id_nav_menu_anchor);
        anchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navMenuAnimator.animate();
            }
        });
        initNavMenu();
    }

    private void initNavMenu(){
        this.navMenuAnimator = new NavMenuAnimator();
        navMenuAnimator.bindMenu( ((LinearLayout)findViewById(R.id.id_nav_menu_layout)) );

    }

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
        final Bundle style = new Bundle();
        style.putInt(PennyDialog.STYLE_KEY, R.style.AddPennyDialogStyle);
        launchPennyDialog(style);
    }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 //hi

    public void onMinusFabClicked(final View view){
        final Bundle style = new Bundle();
        style.putInt(PennyDialog.STYLE_KEY, R.style.MinusPennyDialogStyle);
        launchPennyDialog(style);
    }


    private void toggleNullTransactionImage(){

        // boolean hasTransactedToday = (transactionsList.getAdapter().getItemCount() > 0); //Un comment when ready

        final ViewGroup nullTransaction = findViewById(R.id.id_dashboard_null_transactionlist);

        if(true)
            nullTransaction.setVisibility(View.VISIBLE);
        else
            nullTransaction.setVisibility(View.INVISIBLE);

    }
    private void launchPennyDialog(@Nullable final Bundle pennyArgs){
        PennyDialog.newInstance(null, pennyArgs)
                .show(getSupportFragmentManager(), PennyDialog.TAG);
    }

}
