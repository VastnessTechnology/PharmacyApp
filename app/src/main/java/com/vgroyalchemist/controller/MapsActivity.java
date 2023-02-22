package com.vgroyalchemist.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.vgroyalchemist.DirectionsJSONParser;
import com.vgroyalchemist.R;
import com.vgroyalchemist.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final static int MY_PERMISSIONS_REQUEST = 32;

    private GoogleMap mMap;
    private LatLng mOrigin;
    private LatLng mDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//        mOrigin = new LatLng(,);
//        mDestination = new LatLng(,);
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * This method will manipulate the Google Map on the main screen
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Google map setup
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Show marker on the screen and adjust the zoom level
        mMap.addMarker(new MarkerOptions().position(mOrigin).title("Origin"));
        mMap.addMarker(new MarkerOptions().position(mDestination).title("Destination"));
        drawRoute();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin,8f));




    }


    private void drawRoute(){

        // Getting URL to the Google Directions API
        String url = buildRequestUrl(mOrigin, mDestination);

        TaskDirectionRequest downloadTask = new TaskDirectionRequest();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
//        new TaskDirectionRequest().execute(buildRequestUrl(mOrigin,mDestination));
    }


    private String buildRequestUrl(LatLng origin, LatLng destination) {
//        String strOrigin = origin.latitude + "," + origin.longitude;
//        String strDestination =  destination.latitude + "," + destination.longitude;
//        String sensor = "sensor=false";
//        String mode = "mode=driving";
//
//        String param = strOrigin + "&daddr=" + strDestination ;
//        String output = "json";
//        String APIKEY = getResources().getString(R.string.google_map_key);
//
//        String url = "https://maps.google.com/maps?saddr=" + param + "&key="+APIKEY;;
////        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param +"&key="+APIKEY;
//
//        Log.d("TAG", url);
//        return url;

        // Origin of route
        String str_origin = "origin=" + origin.latitude + ","
                + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";
        String APIKEY = getResources().getString(R.string.google_map_key);

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters +"&key="+ APIKEY ;
        Utility.debugger("vt url..." + url);

        return url;
    }

    /**
     * Request direction from Google Direction API
     *
     * @param requestedUrl see {@link #buildRequestUrl(LatLng, LatLng)}
     * @return JSON data routes/direction
     */
    private String requestDirection(String requestedUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(requestedUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            responseString = stringBuffer.toString();
            bufferedReader.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                    httpURLConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return responseString;
    }

    //Get JSON data from Google Direction
    @SuppressLint("StaticFieldLeak")
    public class TaskDirectionRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String responseString) {
            super.onPostExecute(responseString);
            //Json object parsing

                TaskParseDirection parseResult = new TaskParseDirection();
                parseResult.execute(responseString);


        }
    }


    //Parse JSON Object from Google Direction API & display it on Map
    @SuppressLint("StaticFieldLeak")
    public class TaskParseDirection extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonString) {
            List<List<HashMap<String, String>>> routes = null;
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(jsonString[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);
            ArrayList<LatLng>  points = null;
            PolylineOptions lineOptions = null;


            if (lists != null && lists.size() > 0) {

                for (int i = 0; i < lists.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = lists.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }
                }
            }

            // Drawing polyline in the Google Map for the i-th route
            System.out.println("LAT LONG IN MAP" + mOrigin
                    + " " + mDestination);


            if (points != null && points.size() > 0) {
                lineOptions = new PolylineOptions();
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.visible(true);
                lineOptions.color(Color.BLUE).width(5);
                lineOptions.geodesic(true);
                mMap.addPolyline(lineOptions);

            }
        }

    }

    /**
     * Request app permission for API 23/ Android 6.0
     *
     * @param permission
     */
    private void requestPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    MY_PERMISSIONS_REQUEST);
        }
    }


}