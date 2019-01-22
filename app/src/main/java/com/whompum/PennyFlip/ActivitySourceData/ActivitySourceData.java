package com.whompum.PennyFlip.ActivitySourceData;

import android.arch.lifecycle.LifecycleOwner;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.whompum.PennyFlip.ListUtils.ListFragment;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Wallet.Wallet;
import com.whompum.PennyFlip.PennyListener;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.SlidePennyDialog;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.Transactions.OnTitleListener;
import com.whompum.PennyFlip.Transactions.TransactionFragment;
import com.whompum.PennyFlip.Transactions.TransactionTitleDialog;
import com.whompum.PennyFlip.WalletNotificationManager;
import com.whompum.pennydialog.dialog.PennyDialog;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;
import currencyedittext.whompum.com.currencyedittext.CurrencyFormatter;

public class ActivitySourceData extends AppCompatActivity implements SourceDataClient{

    public static final String DATA = "source.ky";

    private Source data;
    private ActivitySourceDataController server;

    private PennyDialog pennyDialog;

    private WalletNotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.source );

        ButterKnife.bind( this );

        notificationManager = new WalletNotificationManager( this );

        this.data = (Source) getIntent().getSerializableExtra( DATA );

        initializeUi( data );

        if( savedInstanceState == null )
            getSupportFragmentManager()
                    .beginTransaction()
                    .add( R.id.id_global_container, getFragment( resolveNoDataLayout() ), "TAG" )
                    .commit();

        server = new ActivitySourceDataController( this, this, data.getTitle() );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificationManager.unRegister();
    }

    @Nullable
    private Integer resolveNoDataLayout(){

        if( data.getTransactionType() == TransactionType.INCOME)
            return R.layout.layout_source_data_no_data_add;

        return null;
    }

    @Override
    public void onTransactionChanged(@NonNull List<Transaction> data) {

        final ListFragment frag = getFragmentByTag();

        if( frag == null )
            return;

        if( data.size() == 0 )
            getFragmentByTag().onNoData();

        else
            getFragmentByTag().display( data );
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
        return this;
    }

    @Override
    public void onSourceChanged(@NonNull Source source) {
        this.data = source; //Will cause temporary data inconsistency issue on configuration change.
        initializeUi( data );
    }

    @Override
    public void onWalletChanged(@NonNull Wallet wallet) {

        final long value = wallet.getValue();

        notificationManager.onNewWallet(
                value,
                CurrencyFormatter.getInstance().convert( value )
        );

    }

    //Initializes the core UI (title displays, value display, lastUpdate)
    private void initializeUi(@NonNull final Source data){

        int newTransactionImageRes = -1;

        int headerColor = -1;

        if( data.getTransactionType() == TransactionType.INCOME) {
            headerColor = R.color.dark_green;
            newTransactionImageRes = R.drawable.graphic_plus_green;
        }

        else if( data.getTransactionType() == TransactionType.EXPENSE) {
            headerColor = R.color.dark_red;
            newTransactionImageRes = R.drawable.graphic_minus_red;
        }

        findViewById( R.id.id_local_source_header )
                .setBackgroundColor( getResources().getColor( headerColor ) );

        ((FloatingActionButton)findViewById( R.id.id_global_fab ))
                .setImageResource( newTransactionImageRes );

        ((TextView)findViewById( R.id.id_global_title ))
                .setText( data.getTitle() );

        ((CurrencyEditText)findViewById( R.id.id_global_total_display ))
                .setText( String.valueOf( data.getPennies() ) );

        final String lastUpdate = getString(R.string.string_last_update) + " " + Timestamp.from( data.getLastUpdate() ).getPreferentialDate();

        ((TextView)findViewById( R.id.id_global_timestamp )).setText( lastUpdate );

    }

    private ListFragment<Transaction> getFragment(@Nullable final Integer noDataLayout){
        final Bundle bundle = new Bundle();
        bundle.putString(TransactionFragment.SOURCE_KEY, data.getTitle());

     return TransactionFragment.newInstance( data, noDataLayout );
    }

    private boolean fragmentExists(){
        return getFragmentByTag() != null;
    }

    @Nullable
    private TransactionFragment getFragmentByTag(){
        return (TransactionFragment) getSupportFragmentManager().findFragmentByTag( "TAG" );
    }

    @OnClick(R.id.id_global_nav)
    public void navigate(){
        NavUtils.navigateUpFromSameTask( this );
    }

    @OnClick(R.id.ic_delete_source)
    public void delete() {
        launchDeleteConfDialog();
    }

    @OnClick(R.id.id_global_fab)
    public void onFabClicked(){
        if( pennyDialog != null )  //Done to block double tapping; Avoids creating multiple instances.
            if( pennyDialog.isAdded() ) return;

        //vibrate(500L);
        int styleRes;

        if( data.getTransactionType() == TransactionType.INCOME)  //Is adding transaction
            styleRes = R.style.StylePennyDialogAdd;
        else //Is probably a spending transaction
            styleRes = R.style.StylePennyDialogMinus;
        Log.w("CALLIBRATE", "Callibraion issue?");

        final Bundle args = new Bundle();
        args.putInt( PennyDialog.STYLE_KEY, styleRes );

        this.pennyDialog = SlidePennyDialog.newInstance( pennyListener, args );

        pennyDialog.show( getSupportFragmentManager(), SlidePennyDialog.TAG );
    }

    @OnClick(R.id.id_local_statistic_fab)
    public void onStatisticsClicked(){

    }

    private void launchDeleteConfDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder( this, R.style.Theme_AppCompat_Light_Dialog );

        final AlertDialog dialog = builder.setTitle( R.string.string_delete_conf_title )
               .setMessage( R.string.string_delete_conf_message )
               .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton( android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteSource();
            }
        }).create();

        Window w;

        if( (w = dialog.getWindow()) != null )
            w.getAttributes().windowAnimations = R.style.StyleDialogAnimate;

        dialog.show();
    }


    private void deleteSource(){
        server.deleteSource( data.getTitle() );
        server.unObserverSource( this );
        server.unObserveTransactions( this );
        finish();
    }


    private void launchTransactionNameDialog(@NonNull final long pennies){

        new TransactionTitleDialog( this, new OnTitleListener() {
            @Override
            public void onTitleSet(@NonNull String title) {
                final Transaction transaction = new Transaction( data.getTitle() ,data.getTransactionType(), pennies );
                transaction.setTitle( title );
                server.saveTransaction( transaction );
            }
        });

    }

    private PennyListener pennyListener = new PennyListener() {
        @Override
        public void onPenniesChange(final long l) {
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            launchTransactionNameDialog( l );
                        }
                    }
                    , 500L );
        }
    };

}
