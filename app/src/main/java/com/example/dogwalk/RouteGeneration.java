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

public class RouteGeneration extends HomeFragment
{
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int ACCESS_REQUEST_LOCATION = 0;

    public void LocationManager()
    {
    }
}