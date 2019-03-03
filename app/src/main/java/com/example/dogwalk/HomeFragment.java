package com.example.dogwalk;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener
{
    private GoogleMap mMap;
    private static Button b1, b2;
    private static ImageButton slideButton;
    private static EditText textbox;
    private static SlidingDrawer slidingDrawer;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int ACCESS_REQUEST_LOCATION = 0;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.homelayout, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        slideButton = mView.findViewById(R.id.slideButton);
        slidingDrawer = mView.findViewById(R.id.SlidingDrawer);
        b1 = mView.findViewById(R.id.btnGenerate);
        b2 = mView.findViewById(R.id.btnSave);
        textbox = mView.findViewById(R.id.radius);
        setListeners();
        padDrawer();

        locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
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

        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                padDrawer();
            }
        });

        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                padDrawer(); }
        });
        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnGenerate:
                RouteGeneration routeGeneration = new RouteGeneration();
                routeGeneration.LocationManager();
                break;

            case R.id.btnSave:
                // do your code
                break;


            default:
                break;
        }
    }

    void padDrawer()
    {
        final View parent = (View) slideButton.getParent();  // button: the view you want to enlarge hit area
        parent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                slideButton.getHitRect(rect);
                rect.top -= 300;    // increase top hit area
                rect.left -= 300;   // increase left hit area
                rect.bottom += 300; // increase bottom hit area
                rect.right += 300;  // increase right hit area
                parent.setTouchDelegate(new TouchDelegate(rect, slideButton));
            }
        });
    }

    void setListeners() {
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
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