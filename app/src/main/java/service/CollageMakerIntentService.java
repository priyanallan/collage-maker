package service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import lilacapps.example.com.collagemaker.MainActivity;

/**
 * Created by 1021422 on 2/14/2018.
 */

public class CollageMakerIntentService extends IntentService {

    private static final int COLLAGE_SERVICE_NOTIFICATION_ID = 1221;
    private static final int REQUEST_CODE = 0;

    public CollageMakerIntentService() {
        super("Collage Maker Intent Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mainActivityIntent = new Intent(this,MainActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(),REQUEST_CODE, mainActivityIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                                            .setContentTitle("Service Info")
                                            .setContentText("Service Started")
                                            .setSmallIcon(android.support.v4.R.drawable.notification_icon_background)
                                            .setAutoCancel(true);
        mBuilder.setContentIntent(mPendingIntent);
        notificationManager.notify(COLLAGE_SERVICE_NOTIFICATION_ID,mBuilder.build());

    }
}