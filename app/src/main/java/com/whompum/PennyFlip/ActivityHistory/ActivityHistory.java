package com.whompum.PennyFlip.ActivityHistory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.badoualy.datepicker.DatePickerTimeline;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Transaction.Transactions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryan on 1/4/2018.
 */

public class ActivityHistory extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        final RecyclerView transactionList = findViewById(R.id.id_history_recycler);
        transactionList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        final TransactionsAdapter adapter = new TransactionsAdapter(this, temp());

        transactionList.setAdapter(adapter);

    }



    List<Transactions> temp(){

        final List<Transactions> transactions = new ArrayList<>();

        for(int i =0 ; i < 100; i++){
            if(i % 2 == 0)
                transactions.add(new Transactions(Transactions.ADD, 8462, 8642, "Car Wash"));
            if(i % 3 == 0)
                transactions.add(new Transactions(Transactions.SPEND, 9753, 9753, "Cocina"));
        }

    return transactions;
    }

}

















