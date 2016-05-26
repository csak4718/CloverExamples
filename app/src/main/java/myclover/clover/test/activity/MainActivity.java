package myclover.clover.test.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.accounts.Account;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.widget.TextView;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.inventory.InventoryConnector;
import com.clover.sdk.v3.inventory.Item;

import myclover.clover.test.R;
import myclover.clover.test.util.Utils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Account mAccount;
    private InventoryConnector mInventoryConnector;
    private TextView mTextView;
    @OnClick(myclover.clover.test.R.id.btn_to_inventory) void toInventory() {
        Utils.gotoInventoryActivity(MainActivity.this);
    }
    @OnClick(myclover.clover.test.R.id.btn_to_create_order) void toCreateOrder() {
        Utils.gotoCreateOrderActivity(MainActivity.this);
    }
    @OnClick(myclover.clover.test.R.id.btn_to_web_service) void toWebService() {
        Utils.gotoWebServiceActivity(MainActivity.this);
    }
    @OnClick(myclover.clover.test.R.id.btn_to_get_token) void toGetToken() {
        Utils.gotoGetTokenActivity(MainActivity.this);
    }
    @OnClick(R.id.btn_to_send_notif) void toSendNotif() {
        Utils.gotoSendNotifActivity(MainActivity.this);
    }
    @OnClick(R.id.btn_to_activity_intent) void toActivityIntentActivity() {
        Utils.gotoActivityIntentActivity(MainActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(myclover.clover.test.R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTextView = (TextView) findViewById(myclover.clover.test.R.id.txv);

        // Retrieve the Clover account
        if (mAccount == null) {
            mAccount = CloverAccount.getAccount(this);

            if (mAccount == null) {
                return;
            }
        }

        // Connect InventoryConnector
        connect();

        // Get Item
        new InventoryAsyncTask().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect();
    }

    private void connect() {
        disconnect();
        if (mAccount != null) {
            mInventoryConnector = new InventoryConnector(this, mAccount, null);
            mInventoryConnector.connect();
        }
    }

    private void disconnect() {
        if (mInventoryConnector != null) {
            mInventoryConnector.disconnect();
            mInventoryConnector = null;
        }
    }

    private class InventoryAsyncTask extends AsyncTask<Void, Void, List<Item>> {

        @Override
        protected final List<Item> doInBackground(Void... params) {
            try {
                //Get all the items in inventory
                return mInventoryConnector.getItems();

            } catch (RemoteException | ClientException | ServiceException | BindingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected final void onPostExecute(List<Item> itemList) {
            if (itemList != null) {
                StringBuilder sb = new StringBuilder();
                for (Item item: itemList) {
                    sb.append(item.getName()).append(", ");
                }
                mTextView.setText(sb.toString());
            }
        }
    }
}
