package myclover.clover.test.util;

import android.app.Activity;
import android.content.Intent;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.Currency;

import myclover.clover.test.activity.ActivityIntentActivity;
import myclover.clover.test.activity.CreateOrderActivity;
import myclover.clover.test.activity.CustomerFacingTenderActivity;
import myclover.clover.test.activity.GetTokenActivity;
import myclover.clover.test.activity.InventoryActivity;
import myclover.clover.test.activity.MerchantFacingTenderActivity;
import myclover.clover.test.activity.MyOrderUpdateListenerActivity;
import myclover.clover.test.activity.SendNotifActivity;
import myclover.clover.test.activity.TenderInitActivity;
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

    public static void gotoSendNotifActivity(Activity activity) {
        Intent it = new Intent(activity, SendNotifActivity.class);
        activity.startActivity(it);
    }

    public static void gotoActivityIntentActivity(Activity activity) {
        Intent it = new Intent(activity, ActivityIntentActivity.class);
        activity.startActivity(it);
    }

    public static void gotoMyOrderUpdateListenerActivity(Activity activity) {
        Intent it = new Intent(activity, MyOrderUpdateListenerActivity.class);
        activity.startActivity(it);
    }

    public static void gotoTenderInitActivity(Activity activity) {
        Intent it = new Intent(activity, TenderInitActivity.class);
        activity.startActivity(it);
    }

    public static void gotoMerchantFacingTenderActivity(Activity activity) {
        Intent it = new Intent(activity, MerchantFacingTenderActivity.class);
        activity.startActivity(it);
    }

    public static void gotoCustomerFacingTenderActivity(Activity activity) {
        Intent it = new Intent(activity, CustomerFacingTenderActivity.class);
        activity.startActivity(it);
    }

    public static String nextRandomId() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public static String longToAmountString(Currency currency, long amt) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        if (currency != null)
            format.setCurrency(currency);

        double currencyAmount = (double) amt / Math.pow(10.0D, (double) format.getCurrency().getDefaultFractionDigits());

        return format.format(currencyAmount);
    }
}
