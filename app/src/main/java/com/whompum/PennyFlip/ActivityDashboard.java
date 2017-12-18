package com.whompum.PennyFlip;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whompum.pennydialog.dialog.PennyDialog;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public class ActivityDashboard extends AppCompatActivity {


    private RecyclerView transactionsList;
    private CurrencyEditText dashboardValue;
    private TextView lastTransactionTimestamp;


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
    }




    public void onGoalClicked(final View view){
    }

    public void onCallibrateClicked(final View view){
    }

    public void onNavFabClicked(final View view){

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
