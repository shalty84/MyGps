package com.example.administrator.mygps.Utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.administrator.mygps.Interfaces.GpsHlper;

/**
 * Created by Administrator on 11/12/2016.
 */

public class UtlPermissions extends Activity {

    public final static int REQUEST_PERMISSION_23 = 0;
    Context _context;
    GpsHlper _GpsHlper;





    public UtlPermissions(Context context) {
        this._context = context;
    }

    public boolean getGpsPermissions(GpsHlper gpsHlper)
    {
        _GpsHlper = gpsHlper;
        if (Build.VERSION.SDK_INT >= 23
                && ContextCompat.checkSelfPermission(_context, Manifest.permission.ACCESS_COARSE_LOCATION) != _context.getPackageManager().PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(_context, Manifest.permission.ACCESS_FINE_LOCATION) != _context.getPackageManager().PERMISSION_GRANTED) {
            ((Activity)_context).requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_23);
            return false;
        }


        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d("location", "requestCode"+requestCode);

        if(requestCode == REQUEST_PERMISSION_23)
        {
            Log.d("location", "requestCode"+requestCode);
            _GpsHlper.PermissionsResolt(true);
        }
    }
}
