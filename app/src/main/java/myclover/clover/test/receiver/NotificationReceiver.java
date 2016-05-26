package myclover.clover.test.receiver;

/**
 * Created by dewei.kung on 5/25/16.
 */
import android.content.Context;
import android.content.Intent;

import com.clover.sdk.v1.app.AppNotification;
import com.clover.sdk.v1.app.AppNotificationReceiver;
import myclover.clover.test.activity.ReceiveNotifActivity;

public class NotificationReceiver extends AppNotificationReceiver {
    public final static String TEST_NOTIFICATION_ACTION = "test_notification";

    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, AppNotification notification) {
        if (notification.appEvent.equals(TEST_NOTIFICATION_ACTION)) {
            Intent intent = new Intent(context, ReceiveNotifActivity.class);
//            intent.putExtra(ReceiveNotifActivity.EXTRA_PAYLOAD, notification.payload);
            intent.putExtra(ReceiveNotifActivity.EXTRA_PAYLOAD, "My data");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}