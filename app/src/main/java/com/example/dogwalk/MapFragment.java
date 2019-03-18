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

/**
 * My map fragment class, which handles the main feature of my app - generating routes using the Google maps
 * API. I could have split this into smaller classes, but I don't have any performance issues so I focused on
 * other things.
 *
 * This class has methods for getting the location permission, setting up the mapview, as well as all the
 * route generation logic and polyline parsing methods.
 *
 * All of the directionsAPI request methods are based on this Github: https://github.com/gripsack/android
 * This includes: GetRequestURL, requestDirection, taskRequestDirection, and taskParser.
 * These methods are essential in using the Google Maps API, and the gripsack version worked better for me.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener
{
    //Sets up all the variables used by the class.
    private static final String TAG = "MapFragment";

    private GoogleMap mMap; //mMap is a GoogleMap object, which is an object required by the GoogleMaps API.

    private static Button btnGen, btnSave; //Defines buttons within sliding drawer.
    private static EditText textbox;

    private FusedLocationProviderClient mFusedLocationProvider;
    private Boolean mLocationPermissionGranted = false;
    private static final int REQUEST_CODE = 1;
    private LatLng latLngLocation; //Userlocation, which is updated every time the location changes.
    private List<LatLng> listPoints = new ArrayList<>();
    private static final int LOCATION_REQUEST = 500;

    mapInterface snapshotInterface; //Interface instance for passing data to PreviousRoutes.

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //"onCreateView" is the fragment onCreate equivalent, set up in pretty much the same way,
        //however the view is returned.

        View mView = inflater.inflate(R.layout.map_layout, null, false);
        GetLocationPermissions();

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnGen = mView.findViewById(R.id.btnGenerate);
        btnSave = mView.findViewById(R.id.btnSave);


        btnGen.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        btnSave.setEnabled(false); //disables the save route button, as I don't want it used before a route is saved.
        btnSave.setTextColor(Color.parseColor("#808080")); //sets the save route button text colour to grey, as a visual indication  that it is disabled.

        textbox = mView.findViewById(R.id.radius);
        return mView;
    }

    @Override
    public void onAttach(Context context)
    {
        //First method called when fragment is created, used to get current mainactivity instance and
        //assign snapshot interface. differs from onCreateView because the layout and view aren't initialised
        //until onCreateView is called, so that logic does not belong in this method.
        super.onAttach(context);
        snapshotInterface = (mapInterface) getActivity();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        //this method is called when the GoogleMap is set up and nested in its fragment.
        //Assignes mMap to the set up GoogleMap, and finds the users location.
        mMap = googleMap;
        if (mLocationPermissionGranted)
        {
            GetLocation();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true); //If location permission is granted, the user location will be displayed
                                            //and a button to find the users location is defined within the GoogleMap fragment.
        }
    }

    @Override
    public void onClick(View v)
    {
        //Standard onClick using a switch statement which relies on if the generate or save button is clicked.
        String sradius = textbox.getText().toString();
        switch (v.getId()) {
            case R.id.btnGenerate:
                try
                {
                    int radius = Integer.parseInt(sradius); //sRadius is the value the user entered for the distance.
                    mMap.clear(); //Clears map of any previously created polylines and markers, to prevent clutter.
                    listPoints.clear(); //Clears the array that holds the randomly generated points.
                    RandomPoints(latLngLocation, radius); //Calls method that generates random points based on the users location.

                    btnSave.setEnabled(true); //Enables the save route button, because an exception is thrown if an invalid radius input is given.
                    btnSave.setTextColor(Color.parseColor("#FFFFFF")); //Sets the save route button text to white, indicating it's ready to use.
                    break;

                } catch (Exception e)
                {
                    //An exception is thrown if a non integer value is input, and a toast popped.
                    Toast.makeText(getContext(), "Please enter a number.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.btnSave:
                //calls the take snapshot method if the save route button is clicked.
                takeSnapshot();
                Toast.makeText(getContext(), "Route Saved!", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    public void GetLocationPermissions()
    {
        //Gets location permission, method created using Mobile Applications practicals.
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

    private LatLng GetLocation()
    {
        //This method gets the users current location (when it's changed) and moves the camera toward that location.
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
        //Moves the mMap camera to a specific location (centered on a latitude/longitute, with a fixed zoom.)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    public void RandomPoints(LatLng userLocation, int radius)
    {
        //This method is called when the generate route button is clicked. It generates 4 random points,
        //within a circle, where the center is the users location, and the radius is the distance the user entered/3.
        //I divide the radius by 3 because the total route length is what the user enters, not the max distance they want to go away from where they are,
        //and I found dividing by 3 (the number of random points generated) gives a good rough approximation of the distance.

        radius = radius/3;

        int dg = ThreadLocalRandom.current().nextInt(90, 180 + 1);

        //perflat and perflong are generated to be right on the edge of the circle, which means the route isn't cramped to the center of the circle.
        //formula based on what's found here: https://gis.stackexchange.com/questions/37615/how-to-place-markers-on-the-outline-of-a-circle-in-google-maps

        double perflat = Math.sin(dg * Math.PI / 180) * (radius/111000f) + userLocation.latitude;
        double perflng = Math.cos(dg * Math.PI / 180) * (radius/111000f) + userLocation.longitude;

        LatLng perfectLatLng = new LatLng(perflat, perflng);
        listPoints.add(perfectLatLng); //adds perflat and perlong to listpoints, as a latlng rather than two seperate coordinates.

        for (int i = 0; i < 3; i++)
        {
            //This loop generates 3 random points within a circle (at any point within the circle, rather than the outer edges.) and adds them to the
            //listpoints array.
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
            double new_x = x / Math.cos(y0);

            double foundLatitude = new_x + x0;
            double foundLongitude = y + y0;
            LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
            listPoints.add(randomLatLng);
        }

        //Once all of the random points have been found, a waypoint string is generated (the waypoint string is used in my google maps API request.)
        //It starts with "&waypoints=optimize:true" which tells the API to find the most efficient route between the points. After that, each random point LatLng, or waypoint,
        //is added to the string with a "via:" prefix, and a "%2C" suffix. This fits in with the API request formatting, and is then passed to the RequestURL method to
        //finish the API request url.

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
        //Both the origin and destination are the users location, because they want to end up back home after the walk.

        String str_org = "origin=" + userlocation.latitude +","+userlocation.longitude;
        String str_dest = "destination=" + userlocation.latitude+","+userlocation.longitude;

        //Build the full URL
        String param = str_org +"&" + str_dest +"&" +waypointString+"&mode=walking"; //Mode is the mode that the user is taking - It can have the values public transport, running, driving etc, but this is a walking app.
        //output is the format that the API returns the data in - in this case a JSON file - which is passed to my JSON parser later on.
        String output = "json";
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param +"&key="+getString(R.string.directionsApiKey);
        //&key is a Google Maps API key, stored in strings.xml. The Key is used to verify directions requests, and is unique to my app and my Google Account.
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException
    {
        //This method is provided by the Google Developers Documentation, so it was taken in full from there. The API request cannot work without this method,
        //and I did not create it. It was explained to me in a YouTube video linked here - https://www.youtube.com/watch?v=sdinkRanD0I and here
        //https://www.youtube.com/watch?v=xl0GwkLNpNI

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
        //Requests the API in the background, and "onPostExecute" parses the data returned - it's asynced otherwise the app would freeze while
        //waiting for the data.
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

            //Takes the list data and creates a polyline, which is then added to the map.
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
                polylineOptions.color(Color.BLUE);
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

    private void takeSnapshot()
    {
        //This method is provided by the Google Maps API, and provides a way to take a screenshot of the
        //current map as seen on the screen. The screenshot is then passed to the PreviousRoutes fragment,
        //along with date time information and route length information.

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

                String strDateFormat = "dd/MM/yyyy";
                DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                String formattedDate= dateFormat.format(date);
                String sradius = textbox.getText().toString();

                String info = (sradius+"m - "+formattedDate);

                snapshot = resizeBitmap(snapshot);
                Log.d(TAG, "onSnapshotReady: ");
                snapshotInterface.sendSnapshot(snapshot, info);
            }
        };
        mMap.snapshot(callback);
    }

    private Bitmap resizeBitmap(Bitmap snapshot)
    {
        //This method takes the full length screenshot and cuts the top and bottom, forming a square with
        //the route in the centre.

        snapshot = Bitmap.createBitmap(
                    snapshot,
                    0,
                    snapshot.getHeight()/2 - snapshot.getWidth()/2,
                    snapshot.getWidth(),
                    snapshot.getWidth());

        return snapshot;
    }
}