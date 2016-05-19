package www.icebd.com.suzukibangladesh.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

import www.icebd.com.suzukibangladesh.R;

public class GcmMessageHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Toast.makeText(GcmMessageHandler.this,"Notification Received from server",Toast.LENGTH_LONG).show();
       //String message = data.getString("message");
       //Toast.makeText(GcmMessageHandler.this,"Notification Received from server",Toast.LENGTH_LONG).show();

        createNotification(from, data);
    }

    // Creates notification based on title and body received
    private void createNotification(String title, Bundle body) {
       Toast.makeText(this,"Notification Received",Toast.LENGTH_LONG).show();
      /* Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title)
                .setContentText(body);
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());



/*


        Context context = getBaseContext();
        String title1 = body.getString("title");
        Boolean isBackground = Boolean.valueOf(body.getString("is_background"));
        String flag = body.getString("flag");
        String data = body.getString("data");


        Intent intent = new Intent(context,MoreInfoNotification.class);
        intent.putExtra("value",title);
        intent.putExtra("body",data);
        PendingIntent notificIntent = PendingIntent.getActivity(context, 0,
                intent, 0);





        // Builds a notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title1)
                        .setTicker(title1)
                        .setContentText(data);

        // Defines the Intent to fire when the notification is clicked
        mBuilder.setContentIntent(notificIntent);

        // Set the default notification option
        // DEFAULT_SOUND : Make sound
        // DEFAULT_VIBRATE : Vibrate
        // DEFAULT_LIGHTS : Use the default light notification
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);

        // Auto cancels the notification when clicked on in the task bar
        mBuilder.setAutoCancel(true);

        // Gets a NotificationManager which is used to notify the user of the background event
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Post the notification
        mNotificationManager.notify(1, mBuilder.build());



*/




      /*  String title1 = body.getString("title");
        Boolean isBackground = Boolean.valueOf(body.getString("is_background"));
        String flag = body.getString("flag");
        String data = body.getString("data");



        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.drawable.ic_notification_small)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID, notification);*/




    }

}