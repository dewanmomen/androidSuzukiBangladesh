package www.icebd.com.suzukibangladesh.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import www.icebd.com.suzukibangladesh.FirstActivity;
import www.icebd.com.suzukibangladesh.R;
import www.icebd.com.suzukibangladesh.menu.NewsEvents;
import www.icebd.com.suzukibangladesh.request.RFSNotificationFragment;
import www.icebd.com.suzukibangladesh.splash.Splash;

public class GcmMessageHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;
    Context context;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Toast.makeText(GcmMessageHandler.this,"Notification Received from server",Toast.LENGTH_LONG).show();
        Log.d("data : ",data.toString());
        String title = data.getString("title");
        Log.d("title : ",title);
        String message = data.getString("message");
        String NotificationType = data.getString("NotificationType");
        Log.d("NotificationType : ",NotificationType);
        String picture = data.getString("picture");
       //Toast.makeText(GcmMessageHandler.this,"Notification Received from server",Toast.LENGTH_LONG).show();

        context = getBaseContext();
        Bitmap remote_picture = null;
        long when = System.currentTimeMillis();
        int icon = R.drawable.ic_launcher;
        //Bundle gcmData = intent.getExtras();
        NotificationManager notificationManager;
        int count = 1;
        //if message and image url
        if(message != null) {
            try
            {
                Log.v("TAG_IMAGE", "" + data.getString("message"));
                Log.v("TAG_IMAGE", "" + data.getString("picture"));

                NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
                notiStyle.setSummaryText(data.getString("message"));

                try
                {
                    if(picture != null)
                    {
                        remote_picture = BitmapFactory.decodeStream((InputStream) new URL(picture).getContent());
                        notiStyle.bigPicture(remote_picture);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                notificationManager = (NotificationManager)context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                PendingIntent contentIntent = null;

                //Intent gotoIntent = new Intent();
                //Intent gotoIntent = getIntent();
                //Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                Intent launchIntent = new Intent(context, FirstActivity.class);
                //Bundle bundle = new Bundle();
                if(NotificationType != null && launchIntent != null){
                    launchIntent.setAction(NotificationType);

                    launchIntent.putExtra("title",title);
                    launchIntent.putExtra("message",message);
                    launchIntent.putExtra("picture",picture);
                }

                //gotoIntent.setClassName(context, "www.icebd.com.suzukibangladesh.splash.Splash");//Start activity when user taps on notification.
                contentIntent = PendingIntent.getActivity(context,
                        (int) (Math.random() * 100), launchIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                /*NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Suzuki Bangladesh")
                        .setContentText(data.getString("message"));
                mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon));
                //NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());*/


                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        context);

                mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker("Suzuki Bangladesh").setWhen(0);
                mBuilder.setAutoCancel(true);
                mBuilder.setContentTitle("Suzuki Bangladesh");
                mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(data.getString("message")));
                mBuilder.setContentIntent(contentIntent);
                        //mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                mBuilder.setContentText(data.getString("message"));
                if(picture != null)
                {
                    mBuilder.setStyle(notiStyle);
                }

                //notification.flags = Notification.FLAG_AUTO_CANCEL;
                count++;
                notificationManager.notify(count, mBuilder.build());//This will generate seperate notification each time server sends.

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        //createNotification(from, message);
    }


    // Creates notification based on title and body received
    private void createNotification(String from, String body) {
        Log.d("from : ",from);
        Log.d("title : ",body);
       //Toast.makeText(this,"Notification Received",Toast.LENGTH_LONG).show();
      Context context = getBaseContext();

        /*
        // first way
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Suzuki Bangladesh")
                .setContentText(body);
        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon));
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());*/




       /*
        // second way
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

    /*************



    @SuppressWarnings("deprecation")
    private void handleMessage(Context mContext, Intent intent) {
        Bitmap remote_picture = null;
        long when = System.currentTimeMillis();
        int icon = R.drawable.reload_logo;
        Bundle gcmData = intent.getExtras();
        //if message and image url
        if(intent.getExtras().getString("message")!=null && intent.getExtras().getString("imageurl")!=null) {
            try {


                Log.v("TAG_IMAGE", "" + intent.getExtras().getString("message"));
                Log.v("TAG_IMAGE", "" + intent.getExtras().getString("imageurl"));


                NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
                notiStyle.setSummaryText(intent.getExtras().getString("message"));

                try {
                    remote_picture = BitmapFactory.decodeStream((InputStream) new URL(intent.getExtras().getString("imageurl")).getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                notiStyle.bigPicture(remote_picture);
                notificationManager = (NotificationManager) mContext
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                PendingIntent contentIntent = null;

                Intent gotoIntent = new Intent();
                gotoIntent.setClassName(mContext, "com.reloadapp.reload.fragments.MainActivity");//Start activity when user taps on notification.
                contentIntent = PendingIntent.getActivity(mContext,
                        (int) (Math.random() * 100), gotoIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        mContext);
                Notification notification = mBuilder.setSmallIcon(icon).setTicker("Reload.in").setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle("Reload.in")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(intent.getExtras().getString("message")))
                        .setContentIntent(contentIntent)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

                        .setContentText(intent.getExtras().getString("message"))
                        .setStyle(notiStyle).build();


                notification.flags = Notification.FLAG_AUTO_CANCEL;
                count++;
                notificationManager.notify(count, notification);//This will generate seperate notification each time server sends.

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }*/
}