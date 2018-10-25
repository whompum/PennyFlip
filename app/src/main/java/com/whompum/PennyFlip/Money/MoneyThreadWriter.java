package com.whompum.PennyFlip.Money;

import android.support.annotation.NonNull;

public class MoneyThreadWriter extends Thread {

    private ThreadWriterOperation threadWriterOperation;

    @Override
    public void run() {

        if(threadWriterOperation != null)
            threadWriterOperation.doOperation();

    }

    public void doInBackground(@NonNull final ThreadWriterOperation threadWriterOperation){
        this.threadWriterOperation = threadWriterOperation;
        this.start();
    }

    public static abstract class ThreadWriterOperation {
        abstract void doOperation();
    }

}
