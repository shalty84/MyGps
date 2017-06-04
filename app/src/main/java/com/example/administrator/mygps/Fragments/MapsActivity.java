package com.example.administrator.mygps.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.mygps.Interfaces.GpsHlper;
import com.example.administrator.mygps.R;
import com.example.administrator.mygps.Type.Track;
import com.example.administrator.mygps.Type.User;
import com.example.administrator.mygps.Utility.UtlPermissions;
import com.example.administrator.mygps.Utility.UtilFirebase;
import com.example.administrator.mygps.Utility.UtilGps;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends Fragment implements  View.OnClickListener,GpsHlper {

    private static GoogleMap _mMap;
    private static MapView mMapView;
    private static LatLng corentLocation;
    private BroadcastReceiver broadcastReceiver;
    private TextView TextView;
    private Button start, stop;
    private Intent _intent;
    private MarkerOptions marker;
    private View V;
    private CameraPosition cameraPosition;
    private ArrayList<LatLng> singalTrak;
    private static Track _track;
    private Bundle _savedInstanceState;
    UtlPermissions utlPermissions;

    public static void setTrak(Track trak)
    {
        _track = trak;
    }



    public MapsActivity() {
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("gps", "onResume:MapsActivity ");
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent)
                {
                    _intent = intent;
                    Location location = (Location) intent.getExtras().get("coordinate");

                    Log.d("gps", "onResume -> onReceive: ");
                    if(_track==null)
                    {
                        _track = new Track(singalTrak = new ArrayList<>(), User.getUserInstance().getEmail());
                        if(location!=null)
                        {
                            corentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                            _track.addCoordinate(corentLocation);
                        }
                    }
                    else if (checkdistanc(location))
                    {
                            Log.d("gps", "distanceTo>0.5 ");
                           // TextView.setText(_track.getTrack().get(_track.getTrack().size()-1).distanceTo(location)+"");

                            corentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                            _track.addCoordinate(corentLocation);

                            marker.position(corentLocation);
                                // For zooming automatically to the location of the marker
                            addPolyline(_track.getTrack());
                                 cameraPosition = new CameraPosition.Builder().target(corentLocation).zoom(18).build();
                                 _mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                    }
                }

            };
        }
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("Location_update"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            getContext().unregisterReceiver(broadcastReceiver);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.activity_maps, container, false);

        _savedInstanceState = savedInstanceState;
        TextView = (TextView) V.findViewById(R.id.gpsPoint);
        start = (Button) V.findViewById(R.id.start);
        stop = (Button) V.findViewById(R.id.stop);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        mMapView = (MapView) V.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        utlPermissions = new UtlPermissions(this.getContext());
        if(!utlPermissions.getGpsPermissions(this))
            utlPermissions.getGpsPermissions(this);
        else{

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("gps", "initialsMap oncreate");
            initialsMap();

        }
        //

        return V;
    }

    private void initialsMap() {


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                Log.d("gps", "initialsMap onMapReady");


                _mMap = mMap;

                    if(corentLocation!=null) {
                         marker = new MarkerOptions().position(corentLocation).title("Marker Title").visible(true).snippet("Marker Description");
                        _mMap.addMarker(marker);
                       cameraPosition = new CameraPosition.Builder().target(corentLocation).zoom(15).build();
                        _mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }else
                        UtilGps.getCurrentLocation(getContext(),MapsActivity.this);
            }
        });
    }
    private void addPolyline(ArrayList<LatLng> track)
    {
        PolylineOptions polylineOptions = new PolylineOptions();

        LatLng startLocation,endLocation;

       // for (int i=0 ;i<track.size()-1;i++) {
            startLocation = new LatLng(track.get(track.size()-2).latitude, track.get(track.size()-2).longitude);
            endLocation = new LatLng(track.get(track.size()-1).latitude, track.get(track.size()-1).longitude);
            _mMap.addPolyline(polylineOptions.add(startLocation, endLocation)
                    .width(5)
                    .color(Color.BLUE));
        //}
    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.start:

                UtilGps.StartRecording(this.getActivity());

                break;

            case R.id.stop:
                UtilGps.stopRecording(this.getActivity());


               // Track singalTrak = new Track(_track,LogInFragment.userName);
                UtilFirebase.SaveUserTrack(_track, User.getUserInstance().getUid());
                break;
        }
    }

    @Override
    public void getLocation(Location location) {


       corentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d("gps", "getLocation");
       initialsMap();
    }

    @Override
    public void PermissionsResolt(boolean PermissioResolt) {
        Log.d("gps", corentLocation+"");
        UtilGps.getCurrentLocation(getContext(),MapsActivity.this);


    }
    private boolean checkdistanc(Location location)
    {
        float[] results = new float[1];
        Location.distanceBetween(_track.getTrack().get(_track.getTrack().size()-1).latitude,
                _track.getTrack().get(_track.getTrack().size()-1).longitude,
                location.getLatitude(),
                location.getLongitude(),
                results);
        float distanceInMeters = results[0];
        Log.d("gps", "distanceInMeters = "+distanceInMeters);

        return 3.00000000 <= distanceInMeters;

    }
}
