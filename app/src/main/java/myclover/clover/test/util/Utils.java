package myclover.clover.test.util;

import android.app.Activity;
import android.content.Intent;

import myclover.clover.test.activity.CreateOrderActivity;
import myclover.clover.test.activity.GetTokenActivity;
import myclover.clover.test.activity.InventoryActivity;
import myclover.clover.test.activity.WebServiceActivity;

/**
 * Created by dewei.kung on 5/23/16.
 */
public class Utils {
    public static void gotoInventoryActivity(Activity activity) {
        Intent it = new Intent(activity, InventoryActivity.class);
        activity.startActivity(it);
    }

    public static void gotoCreateOrderActivity(Activity activity) {
        Intent it = new Intent(activity, CreateOrderActivity.class);
        activity.startActivity(it);
    }

    public static void gotoWebServiceActivity(Activity activity) {
        Intent it = new Intent(activity, WebServiceActivity.class);
        activity.startActivity(it);
    }

    public static void gotoGetTokenActivity(Activity activity) {
        Intent it = new Intent(activity, GetTokenActivity.class);
        activity.startActivity(it);
    }
}
