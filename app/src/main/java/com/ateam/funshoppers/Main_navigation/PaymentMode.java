package com.ateam.funshoppers.Main_navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ateam.funshoppers.LoginPref;
import com.ateam.funshoppers.R;

public class PaymentMode extends AppCompatActivity implements View.OnClickListener {
    Button online,offline;
    String token="";
    LoginPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref=new LoginPref(this);
        setContentView(R.layout.activity_payment_mode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        online=(Button)findViewById(R.id.online);
        offline=(Button)findViewById(R.id.offline);
        online.setOnClickListener(this);
        offline.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.online:
                token=sharedPref.getToken();
                String url="http://www.suvojitkar365.esy.es/apptite/fire.php?reg_id="+token;
                Log.e("token",url);
                Intent intent = new Intent(PaymentMode.this, Featured.class);
                intent.putExtra("url", url);
                intent.putExtra("title", "Payment Portal");
                startActivity(intent);
                return;
            case R.id.offline:
                Intent i7=new Intent(PaymentMode.this,Featured.class);
                i7.putExtra("url","http://suvojitkar365.esy.es/apptite/qr.php?amount=10");
                i7.putExtra("title","Payment");
                startActivity(i7);
                return;
        }

    }
}
