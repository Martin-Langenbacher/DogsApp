package de.telekom.dogsapp.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import de.telekom.dogsapp.R;
import de.telekom.dogsapp.view.MainActivity;

public class NotificationsHelper {

    private static final String CHANNEL_ID = "Dogs channel id"; // anything can be the name...
    private static final int NOTIFICATION_ID = 123;             // can be any number!

    // we want to have it as a singleton! --> single-Object.
    private static NotificationsHelper instance;
    private Context context;

    private NotificationsHelper(Context context){
        this.context = context;
    }

    public static NotificationsHelper getInstance(Context context) {
        if(instance == null) {
            instance = new NotificationsHelper(context);
        }
        return instance;
        // Now we have our sigelton!
    }

    public void createNotification(){
        createNotificationChannel();

        // 3) you creat the action related to that notification
        Intent intent = new Intent(context, MainActivity.class);  // the intent is that what will happen, when the user is tapping on the notification! --> we pass the contextd and we go to the MainActivity.class!
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // it will open an new activation; then, with multiple activities close the others...

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0); // package our intent...

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.dog);


        // 1) This is how to create a notification
        // 4) And you create the content
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)              // first, we have a builder and we can chain with dots...
                .setSmallIcon(R.drawable.dog_icon)                                                   // smallIcon is assigned to the notification on the drawer
                .setLargeIcon(icon)                                                                  // will come, when we open the drawer...
                .setContentTitle("Dog retrieved")                                                    // title of notification and below the Content...
                .setContentText("This is a notification to let you know that the dog information has been retrieved.")
                .setStyle(
                        new NotificationCompat.BigPictureStyle()                                     // setStyle --> what happens when the user clicks and drags (e.g. show the big.Picture...
                        .bigPicture(icon)
                        .bigLargeIcon(null)                                                          // when we expand, we don't show the icon, we only see the big pictures...
                )
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        // now that we have our notification, we can just use it:
        // 5) and then you display it.
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification);
    }


    // 2) you create the channel
    private void createNotificationChannel(){
        // needs Android version größer 26!!!  --> Version "O"
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String name = CHANNEL_ID;
            String description = "Dogs retrieved notifications channel - is in NotificationHelper.jave";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            // ==> we have a name, a description, an importance AND THEN, we create that channel
        }

    }



}
