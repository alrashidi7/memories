package com.alrshididev.memories;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.location.LocationResult;


public class MyService extends BroadcastReceiver {
    public static final String ACTION_PROCESS="com.example.demomaps.UPDATE_LOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null){
            final String action =intent.getAction();
            if (ACTION_PROCESS.equals(action)){
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null)
                {
                    Location location = result.getLastLocation();
                }
            }


        }

    }
}