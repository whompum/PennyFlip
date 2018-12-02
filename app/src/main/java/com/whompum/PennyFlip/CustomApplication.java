package com.whompum.PennyFlip;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class CustomApplication extends Application {

    public static final String CHANNEL_WALLET = "com.whompum.PennyFlip.Wallet_Channel";

    public static final int OREO = Build.VERSION_CODES.O;
    public static final int DEVICE_VERSION = Build.VERSION.SDK_INT;

    @Override
    public void onCreate() {
        super.onCreate();

        if( DEVICE_VERSION >= OREO )
            getNotificationManager().createNotificationChannel(
                    getWalletChannel()
            );

    }

    private NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService( NOTIFICATION_SERVICE );
    }

    @TargetApi(OREO)
    private NotificationChannel getWalletChannel(){
        return new NotificationChannel( CHANNEL_WALLET, getUserSidedChannelName(), NotificationManager.IMPORTANCE_HIGH );
    }

    public CharSequence getUserSidedChannelName(){
        return getString( R.string.string_wallet_channel_name );
    }

}
