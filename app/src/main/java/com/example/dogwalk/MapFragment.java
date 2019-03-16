package com.example.dogwalk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private static final String TAG = "MapFragment";
    private GoogleMap mMap;
    private static Button b1, b2;
    private static EditText textbox;
    private FusedLocationProviderClient mFusedLocationProvider;
    private Boolean mLocationPermissionGranted = false;
    private static final int REQUEST_CODE = 1;
    private LatLng latLngLocation;
    private List<LatLng> listPoints = new ArrayList<>();
    private static final int LOCATION_REQUEST = 500;
    mapInterface snapshotInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.map_layout, null, false);
        GetLocationPermissions();

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        b1 = mView.findViewById(R.id.btnGenerate);
        b2 = mView.findViewById(R.id.btnSave);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        textbox = mView.findViewById(R.id.radius);
        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            LatLng userLocation = GetLocation();

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        String sradius = textbox.getText().toString();
        switch (v.getId()) {
            case R.id.btnGenerate:
                try
                {
                    int radius = Integer.parseInt(sradius);
                    Log.d(TAG, "onClick: User location is " + latLngLocation);
                    mMap.clear();
                    listPoints.clear();
                    RandomPoints(latLngLocation, radius);
                    break;
                } catch (Exception e)
                {
                    Log.i("",sradius+" is not a number");
                    Toast.makeText(getContext(), "Please enter a number.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.btnSave:
                takeSnapshot();
                Toast.makeText(getContext(), "Feature not currently working.", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    public void GetLocationPermissions()
    {
        String fineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
        String courseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;

        String[] permissions = {fineLocation, courseLocation};

        if(ContextCompat.checkSelfPermission(getContext().getApplicationContext(), fineLocation) ==
                PackageManager.PERMISSION_GRANTED)
        {

            if(ContextCompat.checkSelfPermission(getContext().getApplicationContext(), courseLocation) ==
                    PackageManager.PERMISSION_GRANTED)
            {
                    mLocationPermissionGranted = true;
            }else
            {
                ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE);
            }
        }else
        {
            ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE);
        }
    }

    public void onRequestPermission(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        mLocationPermissionGranted = false;

        switch(requestCode)
        {
            case REQUEST_CODE:
            {
                if(grantResults.length > 0)
                {
                    for (int i = 0; i < grantResults.length; i++)
                    {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                }
            }
        }
    }
    private LatLng GetLocation()
    {
        mFusedLocationProvider = LocationServices.getFusedLocationProviderClient(getActivity());
        try
        {
            if(mLocationPermissionGranted)
            {
                final Task location = mFusedLocationProvider.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful())
                        {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            latLngLocation = (new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15);
                        }
                        else{
                            Log.d(TAG, "onComplete: location not found");
                            Toast.makeText(getContext(),"Unable to access location.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch(SecurityException e)
        {
            Log.e(TAG,"Get device location security exception "+ e.getMessage() );
        }
        return latLngLocation;
    }

    private void moveCamera(LatLng latLng, float zoom)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    public void RandomPoints(LatLng userLocation, int radius)
    {
        radius = radius/3;

        int dg = ThreadLocalRandom.current().nextInt(90, 180 + 1);
        //int dg = 90;
        double perflat = Math.sin(dg * Math.PI / 180) * (radius/111000f) + userLocation.latitude;
        double perflng = Math.cos(dg * Math.PI / 180) * (radius/111000f) + userLocation.longitude;

        LatLng perfectLatLng = new LatLng(perflat, perflng);
        listPoints.add(perfectLatLng);

        for (int i = 0; i < 3; i++)
        {
            double x0 = userLocation.latitude;
            double y0 = userLocation.longitude;

            Random random = new Random();

            // Convert radius from meters to degrees
            double radiusInDegrees = radius / 111000f;

            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            // Adjust the x-coordinate for the shrinking of the east-west distances
            double new_x = x / Math.cos(y0);

            double foundLatitude = new_x + x0;
            double foundLongitude = y + y0;
            LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
            listPoints.add(randomLatLng);
            Location l1 = new Location("");
            l1.setLatitude(randomLatLng.latitude);
            l1.setLongitude(randomLatLng.longitude);
        }

        String waypointString = "&waypoints=optimize:true";

        for(int i = 0; i < 4; i++)
        {
            String lat = "via:" + Double.toString(listPoints.get(i).latitude);
            String lng = "%2C" + Double.toString(listPoints.get(i).longitude);
            waypointString = waypointString + "|" + lat + lng;
        }
        Log.d(TAG, "RandomPoints:" + waypointString);

        String url = getRequestUrl(userLocation, waypointString);
        MapFragment.TaskRequestDirections taskRequestDirections = new MapFragment.TaskRequestDirections();
        taskRequestDirections.execute(url);


    }

    private String getRequestUrl(LatLng userlocation, String waypointString)
    {
        //Value of origin
        String str_org = "origin=" + userlocation.latitude +","+userlocation.longitude;
        //Value of destination
        String str_dest = "destination=" + userlocation.latitude+","+userlocation.longitude;
        //Set value enable the sensor
        //Mode for find direction
        String mode = "mode=walking";
        //Build the full param
        String param = str_org +"&" + str_dest +"&" +waypointString+"&" + mode;
        //Output format
        String output = "json";
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param +"&key="+getString(R.string.directionsApiKey);
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException
    {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            MapFragment.TaskParser taskParser = new MapFragment.TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> >
    {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsJSONParser directionsParser = new DirectionsJSONParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists)
            {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path)
                {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLACK);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions!=null)
            {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getContext().getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    ///NON WORKING CODE, COULDN'T GET ROUTE SAVING WORKING///


    private void takeSnapshot()
    {
        if (mMap == null)
        {
            return;
        }

        final GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback()
        {
            @Override
            public void onSnapshotReady(Bitmap snapshot)
            {
                Date date = new Date();

                String strDateFormat = "hh:mm:ss a";

                DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

                String formattedDate= dateFormat.format(date);
                String sradius = textbox.getText().toString();

                Log.d(TAG, "onSnapshotReady: ");
                snapshotInterface.sendSnapshot(snapshot, sradius);
            }
        };
        mMap.snapshot(callback);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        snapshotInterface = (mapInterface) getActivity();
    }

}