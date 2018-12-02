package com.whompum.PennyFlip.Money;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.whompum.PennyFlip.CustomApplication;
import com.whompum.PennyFlip.Dashboard.ActivityDashboard;
import com.whompum.PennyFlip.R;

public class WalletNotificationReceiver extends BroadcastReceiver {

    public static final String BALANCE_KEY = "balance.ky";
    public static final int WALLET_NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        final String newBalance = intent.getStringExtra( BALANCE_KEY );

        if( newBalance == null || newBalance.isEmpty() )
            return;

        launchNotification( context, newBalance );
    }

    private NotificationManagerCompat getNotificationManager(@NonNull final Context context){
        return NotificationManagerCompat.from( context );
    }

    private CharSequence getContentInfo(@NonNull final Context context){
        return context.getString( R.string.string_wallet_content_info );
    }

    private CharSequence getContentTitle(@NonNull final Context context){
        return context.getString( R.string.string_wallet_title );
    }

    private int getSmallIcon(){
        return R.drawable.icon_logo_notification;
    }

    private PendingIntent getContentIntent(@NonNull final Context context){
        return PendingIntent.getActivity( context,
                0,
                new Intent( context, ActivityDashboard.class ),
                0
        );
    }

    private int getNotificationColor(@NonNull final Context context){
        return ContextCompat.getColor( context, R.color.dark_green );
    }

    private Notification getNotification(@NonNull final Context context, @NonNull final String currentBalance){
        
        return new NotificationCompat.Builder( context, CustomApplication.CHANNEL_WALLET )
                .setContentTitle( getContentTitle( context ) )
                .setContentText( getContentInfo( context ) + " " + currentBalance )
                .setSmallIcon( getSmallIcon( ) )
                .setContentIntent( getContentIntent( context ) )
                .setOnlyAlertOnce( true )
                .setShowWhen( false )
                .setColor( getNotificationColor( context ) )
                .setPriority( NotificationCompat.PRIORITY_HIGH)
                .build();

    }

    private void launchNotification(@NonNull final  Context context,  @NonNull final String newBalance){
       getNotificationManager( context )
               .notify( WALLET_NOTIFICATION_ID, getNotification( context, newBalance ) );
    }

}
