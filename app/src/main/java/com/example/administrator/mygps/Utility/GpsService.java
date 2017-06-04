package com.example.administrator.mygps.Utility;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by Administrator on 30/11/2016.
 */

public class GpsService extends Service {

    private static LocationManager locationManager;
    private static LocationListener listener;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("gps", "onCreate servic: ");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d("gps", "onLocationChanged servic: ");

                Intent intent = new Intent("Location_update");


                intent.putExtra("coordinate",location);
                sendBroadcast(intent);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        };


            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 3 * 1000,3, listener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null)
        {
            locationManager.removeUpdates(listener);
        }
     }
    public static void stopService() {

        if(locationManager != null)
        {
            locationManager.removeUpdates(listener);
        }
    }
}
