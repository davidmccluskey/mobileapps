package com.example.dogwalk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Route;

public class RouteGeneration extends HomeFragment implements Runnable {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int ACCESS_REQUEST_LOCATION = 0;

    @Nullable
    public void run()
    {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener()
        {
            public void onLocationChanged(Location location) {
                if (location != null) {
                    String locationString =
                            "Location changed: Lat: " + location.getLatitude() +
                                    " Lng: " + location.getLongitude();

                    Toast.makeText(getActivity().getBaseContext(), locationString, Toast.LENGTH_LONG).show();
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) { }
            public void onProviderEnabled(String provider) { }
            public void onProviderDisabled(String provider) { }
        };

        // Handle the case we don't have the necessary permission
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(getResources().getString(R.string.app_name),
                    "No Location Permission. Asking for Permission");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},ACCESS_REQUEST_LOCATION);
        } else {
            Log.i(getResources().getString(R.string.app_name),
                    "Location is allowed.");
            setLocationUpdateFunction();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted.
                    Log.i(getResources().getString(R.string.app_name),
                            "Location Permission granted by user.");
                    setLocationUpdateFunction();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.e(getResources().getString(R.string.app_name),
                            "No Location Permission granted by user.");
                }
                return;
            }
        }
    }
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 second

    @SuppressLint("MissingPermission")
    private void setLocationUpdateFunction() {
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
    }

}