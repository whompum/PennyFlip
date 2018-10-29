package com.whompum.PennyFlip.Money.Writes;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.whompum.PennyFlip.Money.MoneyThread;

/**
 *
 * Writes data to an implementation of {@link MoneyWriter}
 *
 * @param <T> The type of data to write
 */
public class RoomMoneyWriter<T> implements MoneyWriter<T> {

    private MoneyDaoWriter<T> writer;

    public RoomMoneyWriter(@NonNull final MoneyDaoWriter writer){
        this.writer = writer;
    }

    @MainThread
    @Override
    public void save(@NonNull final T o) {
        new MoneyThread().doInBackground(new MoneyThread.MoneyThreadOperation() {
            @Override
            public void doOperation() {
                internalSave(o);
            }
        });
    }

    @WorkerThread
    protected void internalSave(@NonNull final T t){
        writer.insert(t);
    }

}
