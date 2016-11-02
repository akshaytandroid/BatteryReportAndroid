package com.akshay.batterystatusreport;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by akshaythalakoti on 11/2/16.
 */

public class ChargeDetector extends AppCompatActivity {
    IntentFilter filter;
    private BroadcastReceiver battery_broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int plugged = intent.getIntExtra("plugged", -1);
            String pluggedType = getPluggedType(plugged);
            Toast.makeText(ChargeDetector.this, pluggedType, Toast.LENGTH_SHORT).show();

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ChargeDetector.this, getString(R.string.str_connect_to_charger_msg), Toast.LENGTH_SHORT).show();
                }
            };
            handler.postDelayed(runnable, 10000);    //10secs
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(battery_broadcast);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(battery_broadcast);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(battery_broadcast, filter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(battery_broadcast, filter);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private String getPluggedType(int plugged) {
        String plugType = getString(R.string.str_not_connected);

        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                plugType = getString(R.string.str_ac_plugged);
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                plugType = getString(R.string.str_usb_plugged);
                break;
        }
        return plugType;
    }
}
