package myclover.clover.test.activity;

import android.accounts.Account;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.util.CloverAuth;


public class GetTokenActivity extends Activity {
    private static String TAG = "MainActivity";

    private Account mAccount;
    private CloverAuth.AuthResult mCloverAuth;

    private TextView mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(myclover.clover.test.R.layout.activity_get_token);

        // Find all the TextView objects
        mToken = (TextView) findViewById(myclover.clover.test.R.id.txv_token);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Retrieve the Clover account
        if (mAccount == null) {
            mAccount = CloverAccount.getAccount(this);

            if (mAccount == null) {
                Toast.makeText(this, getString(myclover.clover.test.R.string.no_account), Toast.LENGTH_SHORT).show();
                finish();
            }

            // Use this account to get the access token (and other Clover authentication data)
            getCloverAuth();
        }
    }

    private void getCloverAuth() {
        // This needs to be done on a background thread
        new AsyncTask<Void, Void, CloverAuth.AuthResult>() {
            @Override
            protected CloverAuth.AuthResult doInBackground(Void... params) {
                try {
                    return CloverAuth.authenticate(GetTokenActivity.this, mAccount);
                } catch (OperationCanceledException e) {
                    Log.e(TAG, "Authentication cancelled", e);
                } catch (Exception e) {
                    Log.e(TAG, "Error retrieving authentication", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(CloverAuth.AuthResult result) {
                mCloverAuth = result;

                // To get a valid auth result you need to have installed the app from the App Market. The Clover servers
                // only creates the token once installed the first time.
                if (mCloverAuth != null && mCloverAuth.authToken !=null) {
                    mToken.setText(getString(myclover.clover.test.R.string.token) + mCloverAuth.authToken);
                } else {
                    mToken.setText(getString(myclover.clover.test.R.string.auth_error));
                }
            }
        }.execute();
    }
}