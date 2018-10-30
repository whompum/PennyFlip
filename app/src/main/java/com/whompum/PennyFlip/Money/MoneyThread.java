package com.whompum.PennyFlip.Money;

import android.support.annotation.NonNull;

public class MoneyThread extends Thread {

    private MoneyThreadOperation moneyThreadOperation;

    @Override
    public void run() {

        if(moneyThreadOperation != null)
            moneyThreadOperation.doOperation();

    }

    public void doInBackground(@NonNull final MoneyThreadOperation moneyThreadOperation){
        this.moneyThreadOperation = moneyThreadOperation;
        this.start();
    }

    public static abstract class MoneyThreadOperation {
        public abstract void doOperation();
    }

}
