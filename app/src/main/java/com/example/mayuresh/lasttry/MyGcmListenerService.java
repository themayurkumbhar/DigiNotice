package com.example.mayuresh.lasttry;

import com.google.android.gms.gcm.GcmListenerService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.io.FileOutputStream;

public class MyGcmListenerService extends GcmListenerService {

    public static final int MESSAGE_NOTIFICATION_ID = 435345;
    private String titles;
    private NotificationManager mNotificationManager;
    private String msg;
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String []a=message.split("#");
        titles=a[0];
        msg=a[1];
        DatabaseHandler db=new DatabaseHandler(this);
        db.addNotice(new Notices(((int)Math.random()*1000)+8,titles,msg));
        createNotification(a[1]);
    }

    // Creates notification based on title and body received
    private void createNotification(String body) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = {500};

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.lancherbl)
                .setContentTitle("DigiNotice: "+titles).setVibrate(pattern)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentText(body)
                .setAutoCancel(true).setSound(sound);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());

    }
}