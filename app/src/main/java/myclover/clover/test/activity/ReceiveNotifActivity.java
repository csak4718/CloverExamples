package myclover.clover.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import myclover.clover.test.R;


public class ReceiveNotifActivity extends Activity {
    public static final String EXTRA_PAYLOAD = "payload";

    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_notif);

        resultText = (TextView) findViewById(R.id.result_text);
        resultText.setVisibility(View.GONE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        resultText.setVisibility(View.VISIBLE);
        resultText.setText(intent.getStringExtra(EXTRA_PAYLOAD));
//        resultText.setText(getString(R.string.result, intent.getStringExtra(EXTRA_PAYLOAD)));
    }
}
