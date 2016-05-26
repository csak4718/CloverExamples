package myclover.clover.test.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v3.order.Discount;
import com.clover.sdk.v3.order.OrderConnector;

import butterknife.ButterKnife;
import butterknife.OnClick;
import myclover.clover.test.R;

//3rd Intent Example: Action Intents
public class ModifyOrderActivity extends AppCompatActivity {
    @OnClick(R.id.btn_add_discount) void submit() {
        String orderId = getIntent().getStringExtra(Intents.EXTRA_ORDER_ID);
        addDiscount(orderId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_order);
        ButterKnife.bind(this);
    }

    private void addDiscount(final String orderId) {
        Log.w("AddDiscount", orderId);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OrderConnector orderConnector = new OrderConnector(ModifyOrderActivity.this, CloverAccount.getAccount(ModifyOrderActivity.this), null);
                    orderConnector.connect();

                    final Discount discount = new Discount();
                    discount.setPercentage(10l);
                    discount.setName("Example2 10% Discount");

                    orderConnector.addDiscount(orderId, discount);
                    orderConnector.disconnect();
                } catch (Exception e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                finish();
            }
        }.execute();

    }
}
