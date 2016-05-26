package myclover.clover.test.activity;

import android.accounts.Account;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.inventory.Item;
import com.clover.sdk.v3.inventory.Modifier;
import com.clover.sdk.v3.inventory.ModifierGroup;
import com.clover.sdk.v3.inventory.PriceType;
import com.clover.sdk.v3.order.Discount;
import com.clover.sdk.v3.order.LineItem;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderConnector;
import com.clover.sdk.v3.order.OrderContract;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import android.support.v7.app.AppCompatActivity;
import com.clover.sdk.v3.inventory.InventoryConnector;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateOrderActivity extends AppCompatActivity {
    private static final String TAG = CreateOrderActivity.class.getSimpleName();
    private Account account;
    private OrderConnector orderConnector;

    private InventoryConnector inventoryConnector;

    @Bind(myclover.clover.test.R.id.order_id) TextView orderId;
    @Bind(myclover.clover.test.R.id.line_item_count) TextView lineItemCount;
    @Bind(myclover.clover.test.R.id.line_items) TextView txvLineItems;
    @Bind(myclover.clover.test.R.id.total) TextView total;
    @Bind(myclover.clover.test.R.id.create_time) TextView createTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(myclover.clover.test.R.layout.activity_create_order);
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

        // Load the last order or create a new order
        loadLastOrder();
    }

    @Override
    protected void onPause() {
        disconnect();
        super.onPause();
    }

    private void connect() {
        disconnect();
        if (account != null) {
            orderConnector = new OrderConnector(this, account, null);
            orderConnector.connect();
            inventoryConnector = new InventoryConnector(this, account, null);
            inventoryConnector.connect();
        }
    }

    private void disconnect() {
        if (orderConnector != null) {
            orderConnector.disconnect();
            orderConnector = null;
        }
        if (inventoryConnector != null) {
            inventoryConnector.disconnect();
            inventoryConnector = null;
        }
    }

    private void loadLastOrder() {
        new OrderAsyncTask().execute();
    }

    private class OrderAsyncTask extends AsyncTask<Void, Void, Order> {

        @Override
        protected final Order doInBackground(Void... params) {
            String orderId = null;
            Cursor cursor = null;
            Order mOrder = null;
            try {
                // Query the last order
                cursor = CreateOrderActivity.this.getContentResolver().query(OrderContract.Summaries.contentUriWithAccount(account), new String[]{OrderContract.Summaries.ID}, null, null, OrderContract.Summaries.LAST_MODIFIED + " DESC LIMIT 1");
                if (cursor != null && cursor.moveToFirst()) {
                    orderId = cursor.getString(cursor.getColumnIndex(OrderContract.Summaries.ID));
                }

                if (orderId == null) {
                    mOrder = orderConnector.createOrder(new Order());
                    return mOrder;
                } else {
                    mOrder = orderConnector.getOrder(orderId);

                    List<Item> merchantItems = inventoryConnector.getItems();

                    Item mItem2 = merchantItems.get(2); // coffee red medium
                    Item mItem3 = merchantItems.get(3); // coffee red small

                    LineItem mLineItem2;
                    LineItem mLineItem3;
                    if (mItem2.getPriceType() == PriceType.FIXED) {
                        mLineItem2 = orderConnector.addFixedPriceLineItem(mOrder.getId(), mItem2.getId(), null, null);
                    } else if (mItem2.getPriceType() == PriceType.PER_UNIT) {
                        mLineItem2 = orderConnector.addPerUnitLineItem(mOrder.getId(), mItem2.getId(), 1, null, null);
                    } else { // The item must be of a VARIABLE PriceType
                        mLineItem2 = orderConnector.addVariablePriceLineItem(mOrder.getId(), mItem2.getId(), 5, null, null);
                    }
                    if (mItem3.getPriceType() == PriceType.FIXED) {
                        mLineItem3 = orderConnector.addFixedPriceLineItem(mOrder.getId(), mItem3.getId(), null, null);
                    } else if (mItem3.getPriceType() == PriceType.PER_UNIT) {
                        mLineItem3 = orderConnector.addPerUnitLineItem(mOrder.getId(), mItem3.getId(), 1, null, null);
                    } else { // The item must be of a VARIABLE PriceType
                        mLineItem3 = orderConnector.addVariablePriceLineItem(mOrder.getId(), mItem3.getId(), 5, null, null);
                    }


                    //Percentage discount
                    final Discount discount1 = new Discount();
                    discount1.setPercentage(10l);
                    discount1.setName("Example 10% Discount");

                    //Static discount
                    final Discount discount2 = new Discount();
                    discount2.setAmount(-100l);
                    discount2.setName("Example 1 base unit of currency Discount");

                    //Apply first discount to whole order
                    orderConnector.addDiscount(mOrder.getId(), discount1);

                    //Apply second discount only to the first line item
                    orderConnector.addLineItemDiscount(mOrder.getId(), mLineItem2.getId(), discount2);



                    List<ModifierGroup> modifierGroups = inventoryConnector.getModifierGroupsForItem(mItem3.getId());

                    //Check if any modifier is available to the item
                    if (modifierGroups.size() > 0){
                        List<Modifier> modifiers = modifierGroups.get(0).getModifiers();
                        if (modifiers.size() > 0){
                            //If so, apply the first modifier to the line item
                            Log.d(TAG, "APPLY modifier");
                            orderConnector.addLineItemModification(mOrder.getId(), mLineItem3.getId(), modifiers.get(0));
                        }
                    }

                    return orderConnector.getOrder(orderId);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            } catch (BindingException e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return null;
        }

        @Override
        protected final void onPostExecute(Order order) {
            // Populate the UI
            orderId.setText("order id = " + order.getId());

            int lineItemSize = 0;
            List<LineItem> lineItems;
            if (order.getLineItems() != null) {
                lineItemSize = order.getLineItems().size();
                lineItems = order.getLineItems();
                StringBuilder sb = new StringBuilder("line items = ");
                for (LineItem lineItem: lineItems) {
                    sb.append(lineItem.getName()).append(", ");
                }
                txvLineItems.setText(sb.toString());
            }

            lineItemCount.setText("line item count = " + Integer.toString(lineItemSize));
            total.setText("total = " + BigDecimal.valueOf(order.getTotal()).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            createTime.setText("create time = " + new Date(order.getCreatedTime()).toString());
        }
    }
}
