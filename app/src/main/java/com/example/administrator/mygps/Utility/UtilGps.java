package com.example.administrator.mygps.Utility;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.administrator.mygps.Interfaces.GpsHlper;

import static android.content.Context.LOCATION_SERVICE;


/**
 * Created by Administrator on 29/11/2016.
 */



public class UtilGps {

    public final static int REQUEST_PERMISSION_23 = 0;
    private static Context _context;
    private static Location _location;


    UtilGps(){}

    public static void setContext(Context context) {
        _context = context;
    }

    public static boolean CheckPermissions(Context context) {
        if (Build.VERSION.SDK_INT >= 23
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != context.getPackageManager().PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != context.getPackageManager().PERMISSION_GRANTED) {
            ((Activity) _context).requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_23);
            return false;
        }


        return true;
    }

    public static void StartRecording(Context context) {
        Log.d("gps", "StartRecording: ");

        Intent i = new Intent(context, GpsService.class);
        context.startService(i);
    }

    public static void stopRecording(Context context) {
        Log.d("gps", "stopRecording: ");
        GpsService.stopService();
        //Intent i = new Intent(context, GpsService.class);
        //context.stopService(i);
    }

    public static void getCurrentLocation(final Context context,final GpsHlper hlper) {

        final ProgressDialog pd = new ProgressDialog(context);

        final LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //pd.setTitle("Title of progress dialog.");
        pd.setMessage("Loading.........");
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));

        pd.setIndeterminate(false);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            UtlPermissions utlPermissions = new UtlPermissions(context);

                utlPermissions.getGpsPermissions(hlper);
            return;
        }
        LocationListener mLocationListener = new LocationListener()
        {


            @Override
            public void onLocationChanged(final Location location) {

                pd.cancel();
                hlper.getLocation(location);
                mLocationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        pd.show();


    }



}
