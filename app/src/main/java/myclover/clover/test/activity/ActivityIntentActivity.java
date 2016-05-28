package myclover.clover.test.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.clover.sdk.v1.Intents;
import com.clover.sdk.v3.apps.AppsIntent;

import butterknife.ButterKnife;
import butterknife.OnClick;
import myclover.clover.test.R;


public class ActivityIntentActivity extends AppCompatActivity {

    @OnClick(R.id.btn_to_register) void toRegister() {
//        1: works
//        Intent intent = new Intent(Intents.ACTION_START_REGISTER);
//        intent.putExtra(Intents.EXTRA_ORDER_ID, "23P9X00NGHFWT");
//        startActivity(intent);

        
//        2: doesn't work
        Intent intent = new Intent(Intents.ACTION_START_REGISTER);
//        String customerJson = "{'id': '5QM8KCB2Y0E5G', 'emailAddresses': [{'id': '5QM8KCB2Y0E5G','emailAddress': 'csak4718@gmail.com'}]}";
        String customerJson = "{'id': '5QM8KCB2Y0E5G'}";

//        intent.putExtra(Intents.EXTRA_CUSTOMER_V3, customerJson);
        intent.putExtra("com.clover.intent.extra.CUSTOMER", customerJson);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_intent);
        ButterKnife.bind(this);
    }
}
