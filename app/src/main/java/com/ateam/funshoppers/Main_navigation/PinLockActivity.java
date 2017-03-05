package com.ateam.funshoppers.Main_navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.ateam.funshoppers.R;


public class PinLockActivity extends AppCompatActivity {
    PinLockView mPinLockView;
    IndicatorDots mIndicatorDots;
    int count;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_lock);
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        Intent i=getIntent();
        if(i!=null &i.hasExtra("count")){
            count=i.getIntExtra("count",3);
        }
    }
    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.d("PinLockActivity", "Pin complete: " + pin);
            if (pin.equals("2301")) {
                Intent i=new Intent(PinLockActivity.this,PaymentMode.class);
                startActivity(i);
            } else {
                if (count > 1) {
                    Toast.makeText(getApplicationContext(), "You entered the wrong pin", Toast.LENGTH_LONG).show();
                    count--;
                } else {
                    Toast.makeText(getApplicationContext(), "You entered wrong pin 3 times", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PinLockActivity.this, MainActivity.class);
                    startActivity(intent);


                }
            }
        }

        @Override
        public void onEmpty() {
            Log.d("PinLockActivity", "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d("PinLockActivity", "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };
}
