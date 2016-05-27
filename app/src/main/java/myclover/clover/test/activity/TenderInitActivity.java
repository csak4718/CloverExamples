package myclover.clover.test.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.tender.Tender;
import com.clover.sdk.v1.tender.TenderConnector;

import java.util.List;

import myclover.clover.test.R;

public class TenderInitActivity extends Activity {
    public static final String TAG = "TenderInitActivity";
    private TenderConnector tenderConnector;
    private Button initializeButton;
    private TextView customTenderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender_init);
        customTenderText = (TextView) findViewById(R.id.txv_custom_tender);

        initializeButton = (Button) findViewById(R.id.btn_initialize);
        initializeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTenderType();
            }
        });

        tenderConnector = new TenderConnector(this, CloverAccount.getAccount(this), null);
        tenderConnector.connect();

        getTenderTypes();
    }

    private void getTenderTypes() {
        new AsyncTask<Void, Void, List<Tender>>() {
            @Override
            protected List<Tender> doInBackground(Void... params) {
                try {
                    return tenderConnector.getTenders();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e.getCause());
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Tender> tenders) {
                getTenderTypesFinished(tenders);
            }
        }.execute();
    }

    public void getTenderTypesFinished(List<Tender> tenders) {
        String text;
        if (tenders != null) {
            boolean tenderExists = false;
            for (Tender tender : tenders) {
                if (getString(R.string.tender_name).equals(tender.getLabel())) {
                    tenderExists = true;
                    break;
                }
            }

            if (tenderExists)
                text = getString(R.string.custom_tender_initialized);
            else {
                text = "Custom tender not found. Tap button to initialize it for your merchant account.";
                initializeButton.setVisibility(View.VISIBLE);
            }
        } else {
            text = "Custom tender initialization failure: Unable to get tenders for merchant account";
            initializeButton.setVisibility(View.VISIBLE);
        }
        customTenderText.setText(text);
    }

    private void createTenderType() {
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    tenderConnector.checkAndCreateTender(getString(R.string.tender_name), getPackageName(), true, false);
                } catch (Exception exception) {
                    Log.e(TAG, exception.getMessage(), exception.getCause());
                    return exception;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception exception) {
                createTenderTypeFinished(exception);
            }
        }.execute();
    }

    private void createTenderTypeFinished(Exception exception) {
        String text;
        if (exception == null) {
            text = getString(R.string.custom_tender_initialized);
            initializeButton.setVisibility(View.GONE);
        } else {
            text = "Custom tender initialization failure: " + exception.getMessage();
            initializeButton.setVisibility(View.VISIBLE);
        }
        customTenderText.setText(text);
        Toast.makeText(TenderInitActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        if (tenderConnector != null) {
            tenderConnector.disconnect();
            tenderConnector = null;
        }
        super.onDestroy();
    }
}
