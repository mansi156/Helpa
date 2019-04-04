package com.helpa.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.helpa.activities.MainActivity;
import com.helpa.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebasePushService";
    private int uniqueId = 0;
    private String message,imageUrl;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sendNotification(remoteMessage.getData());

    }

    //This method is only generating push notification
    private void sendNotification(Map<String, String> data) {
        try {
            message = data.get("message");
            imageUrl = data.get("image");
            generateUniqueId();
            Bitmap myBitmap = null;
            if (imageUrl != null && imageUrl.trim().length() > 0) {
                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueId /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder;
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String CHANNEL_ID = "my_channel_01";// The id of the channel.
                CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                notificationManager.createNotificationChannel(mChannel);

                Notification notification = new Notification.Builder(this)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(message)
                        .setSmallIcon(getNotificationIcon())
                        .setChannelId(CHANNEL_ID)
                        .setColor(getColor())
                        .setSound(defaultSoundUri)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .build();

                notificationManager.notify((int) System.currentTimeMillis(), notification);
            } else {
                notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(getNotificationIcon())
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setContentTitle(getString(R.string.app_name))
                        .setColor(Color.rgb(109, 2, 92))
                        .setSound(defaultSoundUri)
                        .setContentText(message)
                        .setContentIntent(pendingIntent);

                notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(uniqueId, notificationBuilder.build());
            }
        }
        catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    private void generateUniqueId() {
        uniqueId = (int) System.currentTimeMillis();
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }

    private int getColor() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getColor(R.color.Red);
        }
        return 0;
    }
}