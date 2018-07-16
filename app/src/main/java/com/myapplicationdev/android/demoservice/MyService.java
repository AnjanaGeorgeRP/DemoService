package com.myapplicationdev.android.demoservice;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MyService extends Service {
    public MyService() {
    }

    boolean started;
    FusedLocationProviderClient client;
    LocationCallback mLocationCallback;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("Service", "Service created");
        super.onCreate();
        client = LocationServices.getFusedLocationProviderClient(MyService.this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location data = locationResult.getLastLocation();
                    double lat = data.getLatitude();
                    double lng = data.getLongitude();
                    String msg = "New location detected\nLat: " + lat + "Lng: " + lng;
                    Toast.makeText(MyService.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (started == false) {
            started = true;
            Log.d("Service", "Service started");
        } else {
            Log.d("Service", "Service is still running");
        }

        //requestLocationUpdate
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(100);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //removeLocationUpdate
        client.removeLocationUpdates(mLocationCallback);
        Log.d("Service", "Service exited");
        super.onDestroy();
    }
}
