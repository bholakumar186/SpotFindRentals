package com.example.spotfindrentals;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.FirebaseMessaging;

import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyFirebaseInstanceid extends FirebaseMessagingService {

    // This method is called when a new token is generated
//    @Override
//    public void onNewToken(String token) {
//        super.onNewToken(token);
//        // Log the new token or send it to your server
//        Log.d("FCM Token", "New FCM Token: " + token);
//    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a notification payload
        if (remoteMessage.getNotification() != null) {
            // Show the notification
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String messageBody) {
        // Check if we are on Android 13 (API level 33) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if permission is granted to post notifications
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, handle this appropriately (e.g., request permission)
                return;
            }
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default")
                //.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Booking Confirmation")
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
