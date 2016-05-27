package myclover.clover.test.activity;

import android.accounts.Account;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.order.LineItem;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderConnector;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import myclover.clover.test.R;

public class MyOrderUpdateListenerActivity extends AppCompatActivity implements OrderConnector.OnOrderUpdateListener2 {
    private static final String TAG = MyOrderUpdateListenerActivity.class.getSimpleName();
    private Account account;
    private OrderConnector orderConnector;
    private int orderCount = 0;
    private String s = "";
    @Bind(R.id.txv_result) TextView txvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_update_listener);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Retrieve the Clover account
        if (account == null) {
            account = CloverAccount.getAccount(this);

            if (account == null) {
                Toast.makeText(this, "no_account", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        // Create and Connect to the OrderConnector
        connect();

//        Log.d(TAG, "add Listener");
        orderConnector.addOnOrderChangedListener(this);
    }

    @Override
    protected void onPause() {
//        disconnect();
        super.onPause();
    }

    private void connect() {
        disconnect();
        if (account != null) {
            orderConnector = new OrderConnector(this, account, null);
            orderConnector.connect();
        }
    }

    private void disconnect() {
        if (orderConnector != null) {
            orderConnector.disconnect();
            orderConnector = null;
        }
    }



    @Override
    public void onOrderUpdated(String orderId, boolean selfChange) {
        Log.d(TAG, "onOrderUpdated");
    }

    @Override
    public void onOrderCreated(String orderId) {
        orderCount++;
        s += "==============================\nYou created "+ String.valueOf(orderCount) +"th order. Id is " + orderId + "\n";
        txvResult.setText(s);
    }

    @Override
    public void onOrderDeleted(String orderId) {

    }

    @Override
    public void onOrderDiscountAdded(String orderId, String discountId) {
        Log.d(TAG, "onOrderDiscountAdded");
    }

    @Override
    public void onOrderDiscountsDeleted(String orderId, List<String> discountIds) {
        Log.d(TAG, "onOrderDiscountsDeleted");
    }

    @Override
    public void onLineItemsAdded(String orderId, List<String> lineItemIds) {
        Log.d(TAG, "onLineItemsAdded");
        new AccessDataAsyncTask().execute(orderId);
    }

    @Override
    public void onLineItemsUpdated(String orderId, List<String> lineItemIds) {
        Log.d(TAG, "onLineItemsUpdated");
    }

    @Override
    public void onLineItemsDeleted(String orderId, List<String> lineItemIds) {
        Log.d(TAG, "onLineItemsDeleted");
    }

    @Override
    public void onLineItemModificationsAdded(String orderId, List<String> lineItemIds, List<String> modificationIds) {
        Log.d(TAG, "onLineItemModificationsAdded");
    }

    @Override
    public void onLineItemDiscountsAdded(String orderId, List<String> lineItemIds, List<String> discountIds) {
        Log.d(TAG, "onLineItemDiscountsAdded");
    }

    @Override
    public void onLineItemExchanged(String orderId, String oldLineItemId, String newLineItemId) {
        Log.d(TAG, "onLineItemExchanged");
    }

    @Override
    public void onPaymentProcessed(String orderId, String paymentId) {

    }

    @Override
    public void onRefundProcessed(String orderId, String refundId) {

    }

    @Override
    public void onCreditProcessed(String orderId, String creditId) {

    }

    private class AccessDataAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... orderId) {
            try {
                Order mOrder = orderConnector.getOrder(orderId[0]);
                JSONObject orderJson = mOrder.getJSONObject();
//                Log.d("Order title = ", mOrder.getTitle());
                Log.d("Order JSON = ", orderJson.toString());


                Long total = mOrder.getTotal();
                List<LineItem> lineItems = mOrder.getLineItems();

                String str = "";
                for (LineItem lineItem: lineItems) {
                    str += lineItem.getName() + "\n";
                }
                str += "Total = " + String.valueOf(total) + "\n";
                return str;
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            } catch (BindingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                txvResult.setText(s += result);
            }
        }
    }
}
