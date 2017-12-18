package com.whompum.PennyFlip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.whompum.pennydialog.dialog.PennyDialog;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public class ActivityDashboard extends AppCompatActivity {


    private RecyclerView transactions;
    private CurrencyEditText value;
    private TextView timestamp;

    private PennyDialog pennyDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        pennyDialog = PennyDialog.newInstance(null,null);
        toggleTransactions();
    }

    private void toggleTransactions(){

       // boolean hasTransactedToday = (transactions.getAdapter().getItemCount() > 0); //Un comment when ready

        final ViewGroup nullTransaction = findViewById(R.id.null_transaction_layout);

        if(false)
            nullTransaction.setVisibility(View.VISIBLE);
        else
            nullTransaction.setVisibility(View.INVISIBLE);

    }

    public void onGoalClicked(final View view){
    }

    public void onCallibrateClicked(final View view){
    }

    public void onNavigationFabClicked(final View view){
    }

    public void onPlusFabClicked(final View view){
        launchPennyDialog();
    }

    public void onMinusFabClicked(final View view){
        launchPennyDialog();
    }

    private void launchPennyDialog(){
        if(pennyDialog!=null)
            pennyDialog.show(getSupportFragmentManager(), PennyDialog.TAG);
    }

}
