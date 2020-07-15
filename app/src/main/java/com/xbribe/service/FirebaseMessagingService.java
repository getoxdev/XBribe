package com.xbribe.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.RemoteMessage;
import com.xbribe.R;
import com.xbribe.ui.main.MainActivity;

import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public FirebaseMessagingService() {
    }

    @Override
    public void onNewToken(@NonNull String s) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(!task.isSuccessful())
                {
                    Log.w("TAG","Get InstanceId failed",task.getException());
                    return;
                }

                String token = task.getResult().getToken();
                Log.e("My Token",token);
            }
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        //int type=getSharedPreferences("login_info",MODE_PRIVATE).getInt("usertype",-1);

        Map<String, String> data = remoteMessage.getData();
        String body = data.get("body");
        String title = data.get("title");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        /*if(type==2){
            intent = new Intent(getApplicationContext(), ViewResponses.class);
        }
        else
            intent = new Intent(getApplicationContext(), OldQuotation.class);*/

        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 101, intent, 0);

        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            channel = new NotificationChannel("222", "my_channel", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        getApplicationContext(), "222")
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setLargeIcon(((BitmapDrawable)getDrawable(R.drawable.icon_main)).getBitmap())
                        .setSmallIcon(R.drawable.icon_main_2)
                        .setContentText(body)
                        .setSmallIcon(R.drawable.icon_main_2)
                        .setContentIntent(pi)
                ;

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        nm.notify(101, builder.build());
    }
}
