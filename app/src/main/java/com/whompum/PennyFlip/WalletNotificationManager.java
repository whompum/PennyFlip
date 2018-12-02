package com.whompum.PennyFlip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.whompum.PennyFlip.Money.WalletNotificationReceiver;

public class WalletNotificationManager {

    public static final String NOTIF_CONTENT_KEY= "notifContent.ky";

    public static final String ACTION = "com.whompum.PennyFlip.WALLET_CHANGE";

    private SharedPreferences preferences;

    private LocalBroadcastManager broadcastManager;

    private BroadcastReceiver receiver = new WalletNotificationReceiver();

    public WalletNotificationManager(@NonNull final Context context){
        preferences = getPreferences( context );
        broadcastManager = LocalBroadcastManager.getInstance( context );

        register();
    }

    public void onNewWallet(final long amount, @NonNull final String currentBalance){

        final long lastKnownNotificationContentInfo = getLastKnownNotificationContentInfo();

        if( amount == lastKnownNotificationContentInfo )
            return;

        deliverBroadcast( amount, currentBalance );
    }

    private void deliverBroadcast(final long amount, @NonNull final String currentBalance){

        final Intent intent = new Intent( ACTION );

        intent.putExtra( WalletNotificationReceiver.BALANCE_KEY, currentBalance );

        broadcastManager.sendBroadcast( intent );

        setLastKnownNotificationContentInfo( amount );

    }

    public void register(){
        broadcastManager.registerReceiver( receiver, getWalletBroadcastFilter() );
    }

    public void unRegister(){
        broadcastManager.unregisterReceiver( receiver );
    }

    private IntentFilter getWalletBroadcastFilter(){
        return new IntentFilter( ACTION );
    }

    private SharedPreferences getPreferences(@NonNull final Context context){
        return context.getSharedPreferences( CustomApplication.CHANNEL_WALLET, Context.MODE_PRIVATE );
    }

    private long getLastKnownNotificationContentInfo(){
        return preferences.getLong( NOTIF_CONTENT_KEY, Long.MAX_VALUE );
    }

    private void setLastKnownNotificationContentInfo(final long newBalance){
        preferences.edit().putLong( NOTIF_CONTENT_KEY, newBalance ).apply();
    }

}
