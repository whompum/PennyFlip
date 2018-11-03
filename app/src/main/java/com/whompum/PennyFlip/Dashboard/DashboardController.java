package com.whompum.PennyFlip.Dashboard;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.whompum.PennyFlip.Money.DatabaseUtils;
import com.whompum.PennyFlip.Money.MoneyDatabase;
import com.whompum.PennyFlip.Money.Queries.Deliverable;
import com.whompum.PennyFlip.Money.Queries.LoggerResponder;
import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;
import com.whompum.PennyFlip.Money.Queries.Responder;
import com.whompum.PennyFlip.Money.Queries.WalletQueries;
import com.whompum.PennyFlip.Money.Queries.WalletRequestBuilder;
import com.whompum.PennyFlip.Money.Writes.MoneyWriter;
import com.whompum.PennyFlip.Money.Writes.RoomMoneyWriter;
import com.whompum.PennyFlip.Money.Source.NewSourceTotalConstraintException;
import com.whompum.PennyFlip.Time.UserStartDate;
import com.whompum.PennyFlip.DialogSourceChooser.SourceWrapper;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Wallet.Wallet;

public class DashboardController implements ActivityDashboardConsumer, Observer<Wallet>{

    //private LocalMoneyProvider repo;

    private DashboardClient client;

    private MoneyWriter writer;
    private MoneyDatabase database;

    public DashboardController(@NonNull final Context context){
        this(context, null);
    }

    /**
     *
     * @param context used to instantiate a Repo object
     * @param o Used to track changes occuring to the Wallet
     */
    public DashboardController(@NonNull final Context context, @Nullable LifecycleOwner o){
        UserStartDate.set(context); //Sets the user start date. If already set then it will skip
        //repo = LocalMoneyProvider.obtain(context);

        this.database = DatabaseUtils.getMoneyDatabase( context );

        this.writer = new RoomMoneyWriter( database );

        if( o != null ) {
            bindWalletObserver( o );
        }
    }

    @Override
    public void bindClient(@NonNull DashboardClient c) {
        this.client = c;
    }

    @Override
    public void bindWalletObserver(@NonNull final LifecycleOwner o){
        //Fetch an observable wallet, and register the lifecycle owner on it.

        final Deliverable<LiveData<Wallet>> deliverable =
                new WalletQueries().queryObservable( new WalletRequestBuilder().getQuery(), database );

        deliverable.attachResponder(new Responder<LiveData<Wallet>>() {
            @Override
            public void onActionResponse(@NonNull LiveData<Wallet> data) {
                data.observe( o, DashboardController.this );
            }
        });

        deliverable.attachCancelledResponder( new LoggerResponder( DashboardController.class ) );

    }

    public void saveTransaction(@NonNull final SourceWrapper w, @NonNull final Transaction t){
        //Save the source first, if its new
        if( ( w.getTag().equals( SourceWrapper.TAG.NEW ) ) ) {

            try{
                writer.saveSource( w.getSource() );
            }catch ( NewSourceTotalConstraintException e ){
                w.getSource().setPennies( 0L );
                writer.saveSource( w.getSource() );
            }

        }
        writer.saveTransaction( t );
    }

    @Override
    public void onChanged(@Nullable Wallet wallet) {

        if( wallet != null )
            if( client != null )
                client.onWalletChanged( wallet.getValue() );
    }
}
