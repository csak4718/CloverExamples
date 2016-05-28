package myclover.clover.test.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.clover.sdk.v1.Intents;

import myclover.clover.test.R;
import myclover.clover.test.activity.MainActivity;

/**
 * Created by dewei.kung on 5/26/16.
 */
public class MyBroadcastReceiver extends BroadcastReceiver{

    private void showNotification(Context context, String orderId) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.test_icon)
                        .setContentTitle("My notification")
                        .setContentText(orderId);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intents.ACTION_ORDER_CREATED)) {
            String orderId = intent.getStringExtra(Intents.EXTRA_CLOVER_ORDER_ID);
            showNotification(context, orderId);
        }
    }
}
