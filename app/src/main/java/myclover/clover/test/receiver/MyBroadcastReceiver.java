package myclover.clover.test.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.clover.sdk.v1.Intents;

import myclover.clover.test.activity.ReceiveNotifActivity;

/**
 * Created by dewei.kung on 5/26/16.
 */
public class MyBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intents.ACTION_ORDER_CREATED)) {
            Log.d("MyBroadcastReceiver", "RECEIVE");
            String orderId = intent.getStringExtra(Intents.EXTRA_CLOVER_ORDER_ID);
            Toast.makeText(context, orderId, Toast.LENGTH_LONG).show();

            Intent myIntent = new Intent(context, ReceiveNotifActivity.class);
            myIntent.putExtra(ReceiveNotifActivity.EXTRA_PAYLOAD, "My data");
            context.startActivity(myIntent);
        }
    }
}
