package com.clover.test.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.clover.test.activity.CreateOrderActivity;
import com.clover.test.activity.InventoryActivity;

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
}
