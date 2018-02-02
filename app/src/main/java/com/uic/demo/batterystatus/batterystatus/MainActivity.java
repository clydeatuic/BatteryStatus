package com.uic.demo.batterystatus.batterystatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class MainActivity extends AppCompatActivity {

    TextView textView_batteryPercentage;
    TextView textView_batteryPercentageRing;
    TextView textView_batteryPlugged;
    ImageView imageView_batteryPlug;
    RingProgressBar progressBar_batteryPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_batteryPercentage = (TextView) findViewById(R.id.textView_batteryPercentage);
        textView_batteryPercentageRing = (TextView) findViewById(R.id.textView_batteryPercentageRing);
        textView_batteryPlugged = (TextView) findViewById(R.id.textView_batteryPlugged);
        progressBar_batteryPercentage = (RingProgressBar) findViewById(R.id.progressBar_batteryPercentage);
        imageView_batteryPlug = (ImageView) findViewById(R.id.imageView_batteryPlug);

        loadBatteryInfo();
    }

    private void loadBatteryInfo(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(batteryInfoReceiver, intentFilter);
    }

    private void updateBatteryData(Intent intent){
        boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
        if(present){
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            if(level!= -1 && scale!= -1){
                int batteryPercentage =  (int) ((level / (float) scale) * 100f);
                textView_batteryPercentage.setText(batteryPercentage+"%");
                textView_batteryPercentageRing.setText(batteryPercentage+"");
                progressBar_batteryPercentage.setProgress(batteryPercentage);
            }

            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            if(plugged != 0) {
                textView_batteryPlugged.setText(plugged + "");
                imageView_batteryPlug.setBackgroundResource(R.drawable.ic_battery_charging_50_black_24dp);
            }else{
                textView_batteryPlugged.setText(plugged + "");
                imageView_batteryPlug.setBackgroundResource(R.drawable.ic_battery_50_black_24dp);
            }

        }else{
            Toast.makeText(MainActivity.this, "No Battery detected", Toast.LENGTH_SHORT).show();
        }
    }

    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateBatteryData(intent);
        }
    };
}
