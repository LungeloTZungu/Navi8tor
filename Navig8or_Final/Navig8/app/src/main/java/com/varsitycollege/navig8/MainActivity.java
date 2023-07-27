package com.varsitycollege.navig8;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.SphericalUtil;

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

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{

    //Variable declaration
    GoogleMap map;
    Location currentLocation;
    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    SearchView searchView;
    FloatingActionButton B_food, B_bank, B_hosp, B_mall, B_hotel, B_meseum;
    LatLng latLng, desLatLng;
    double latitude, longitude;
    Button clear, btnSettings, btnReview;
    Double distance;
    TextView tvDistance;
    String directionMode = "driving";

    SwitchCompat switchMode;
    SharedPreferences sharedPref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialising variables
        B_food = findViewById(R.id.Food);
        B_bank = findViewById(R.id.Bank);
        B_hosp = findViewById(R.id.Hospital);
        B_mall = findViewById(R.id.Mall);
        B_hotel = findViewById(R.id.Hotel);
        B_meseum = findViewById(R.id.Museum);
        clear = findViewById(R.id.Clear_All);
        switchMode = findViewById(R.id.switchMode);
        tvDistance = findViewById(R.id.tvDistance);
        btnSettings = findViewById(R.id.btnSettings);
        btnReview = findViewById(R.id.btnReview);
        //Initializing fused location

        client = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        searchView = findViewById(R.id.search_location);
        getCurrentLocation();

        //button intent to take user to settings page
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, Favourite.class));
            }
        });

        //button intent to take user to review page
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, ReviewsPage.class));
            }
        });

        //this button will clear the map
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.clear();
                tvDistance.setText("");

                getCurrentLocation();
            }
        });

        //this search view allows users to serach for the destination
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                String location = searchView.getQuery().toString();

                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address address = addressList.get(0);
                    desLatLng = new LatLng(address.getLatitude(), address.getLongitude());//this will set the desLatLng

                    distance = (SphericalUtil.computeDistanceBetween(latLng,desLatLng)/1000); //this will display the distance between the two locations
//                    Toast.makeText(MainActivity.this, "Distance between: " + distance + "km", Toast.LENGTH_SHORT).show();
                    tvDistance.setText("Distance between: " + distance + "km"); //this will dsiplay the disnace that was calculated
                    map.clear();
                    //this will add the markers to the map
                    map.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                    map.addMarker(new MarkerOptions().position(desLatLng).title("Destination"));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(desLatLng, 18));

                    //this will display the polyline
                    String url = getDirectionsUrl(latLng, desLatLng);
                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(url);
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

//this will dsiplay the marker for the seleted landmark
        B_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.clear();
                tvDistance.setText("");
                String bank = "bank";
                getCurrentLocation();
                String url = getUrl(latitude, longitude, bank);
                if (url != null) {
                    Object dataTransfer[] = new Object[2];

                    FetchData fetchData = new FetchData();
                    dataTransfer[0] = map;
                    dataTransfer[1] = url;


                    fetchData.execute(dataTransfer);
                    Toast.makeText(MainActivity.this, "Showing banks nearby", Toast.LENGTH_SHORT).show();


                }


            }
        });

        //this will dsiplay the marker for the seleted landmark
        B_mall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.clear();
                tvDistance.setText("");
                String mall = "mall";
                getCurrentLocation();
                String url = getUrl(latitude, longitude, mall);
                Object dataTransfer[] = new Object[2];

                FetchData fetchData = new FetchData();
                dataTransfer[0] = map;
                dataTransfer[1] = url;
                fetchData.execute(dataTransfer);
                Toast.makeText(MainActivity.this, "Showing malls nearby", Toast.LENGTH_SHORT).show();

            }

        });
        //this will dsiplay the marker for the seleted landmark
        B_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.clear();
                tvDistance.setText("");
                String food = "restaurant";
                getCurrentLocation();
                String url = getUrl(latitude, longitude, food);
                Object dataTransfer[] = new Object[2];

                FetchData fetchData = new FetchData();
                dataTransfer[0] = map;
                dataTransfer[1] = url;

                fetchData.execute(dataTransfer);
                Toast.makeText(MainActivity.this, "Showing restaurants nearby", Toast.LENGTH_SHORT).show();
            }
        });
        //this will dsiplay the marker for the seleted landmark
        B_hosp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.clear();
                tvDistance.setText("");
                String hospital = "hospital";
                getCurrentLocation();
                String url = getUrl(latitude, longitude, hospital);
                Object dataTransfer[] = new Object[2];

                FetchData fetchData = new FetchData();
                dataTransfer[0] = map;
                dataTransfer[1] = url;
                fetchData.execute(dataTransfer);
                Toast.makeText(MainActivity.this, "Showing hospitals nearby", Toast.LENGTH_SHORT).show();
            }
        });
        //this will dsiplay the marker for the seleted landmark
        B_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.clear();
                tvDistance.setText("");
                String hospital = "hotel";
                getCurrentLocation();
                String url = getUrl(latitude, longitude, hospital);
                Object dataTransfer[] = new Object[2];

                FetchData fetchData = new FetchData();
                dataTransfer[0] = map;
                dataTransfer[1] = url;
                fetchData.execute(dataTransfer);
                Toast.makeText(MainActivity.this, "Showing hotels nearby", Toast.LENGTH_SHORT).show();
            }
        });
//this will dsiplay the marker for the seleted landmark
        B_meseum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.clear();
                tvDistance.setText("");
                String meseum = "museum";
                getCurrentLocation();
                Object dataTransfer[] = new Object[2];

                FetchData fetchData = new FetchData();
                dataTransfer.equals(null);


                String url = getUrl(latitude, longitude, meseum);

                dataTransfer[0] = map;
                dataTransfer[1] = url;
                fetchData.execute(dataTransfer);
                Toast.makeText(MainActivity.this, "Showing museums nearby", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //this will get the directions url, google api to get the directions
    private String getDirectionsUrl(LatLng latLng, LatLng destLatLng) {

        // Origin of route
        String route_current = "origin=" + latLng.latitude + "," + latLng.longitude;

        // Destination of route
        String route_destination = "destination=" + destLatLng.latitude + "," + destLatLng.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = route_current + "&" + route_destination + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" +"AIzaSyAqldjAnPrYOd91eTsl5IyjNfyxwTN5fBg";


        return url;
    }

//this will call the places api that is used for the selected landmarks
    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        stringBuilder.append("location=" + latitude + "," + longitude);
        stringBuilder.append("&radius=" + 10000);
        stringBuilder.append("&type=" + nearbyPlace);
        stringBuilder.append("&sensor=true");
        stringBuilder.append("&key=" + "AIzaSyB8zhy2S511JxNQpl6uKtSa-F756QpDHB4");

        return stringBuilder.toString();
    }

    //this will get the current location of the user
    private void getCurrentLocation() {
        // Assigning variable
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            return;
        } else {
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLocation = location;
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Toast.makeText(MainActivity.this, (int) currentLocation.getLatitude() + "," + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                        assert mapFragment != null;
                        mapFragment.getMapAsync(MainActivity.this);


                    }
                }
            });
        }

    }

    //this will get the permissions needed for the current location
    @Override
    public void onRequestPermissionsResult(int reqCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(reqCode, permissions, grantResults);
        if (reqCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        //Code attribution
//Links: https://www.youtube.com/watch?v=_hqHA-YSF98 ; https://www.youtube.com/watch?v=-qsHE3TpJqw ; https://www.youtube.com/watch?v=9G1ErQo6dBU
//Authors: Aws Rh ; Coding with Dev ; Android Coding

        sharedPref = getSharedPreferences("light", 0); //This is the  default
        Boolean bool = sharedPref.getBoolean("light_mode", false);
        if (bool) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            switchMode.setChecked(false);
        }

        //this will enable the user to change the map between night and dark mode
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton comBtn, boolean isChecked) {
                if (isChecked) { //if button is checked dark mode will be enabled and light mode will be disabled
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchMode.setChecked(true);
                    SharedPreferences.Editor ed = sharedPref.edit(); //this will edit the shared preference
                    ed.putBoolean("dark_mode", true); //this will set the shared preference as true
                    ed.commit(); //this will commit the edit/change

                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    MainActivity.this, R.raw.mapstyle_dark));
                    if (!success) {
                        Log.e("MainActivity", "Style parsing failed.");
                    }
                } else { //if button is not checked light mode will be enabled and dark mode will be disabled
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchMode.setChecked(false);
                    SharedPreferences.Editor ed = sharedPref.edit(); //this will edit the shared preference
                    ed.putBoolean("dark_mode", false); //this will set the shared preference as false
                    ed.commit(); //this will commit the edit/change

                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    MainActivity.this, R.raw.mapstyle_normal));
                    if (!success) {
                        Log.e("MainActivity", "Style parsing failed.");
                    }

                }
            }
        });

        latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        map.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 0f));
    }

//    ---Code attribution---
    //Author:The Code City
//Title:Draw route between two locations in Android - Google Maps Directions API
//Date:14/11/2022
//Link:https://www.youtube.com/watch?v=wRDLjUK8nyU
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        /**
         * A method to download json data from url
         */
        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.connect();

                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();

                br.close();

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask(directionMode);


            parserTask.execute(result);

        }
    }
//this declares the type of transport and creates an instance of the Direction class, that is used to draw the polyline
    public  class ParserTask extends  AsyncTask<String,Void,List<List<HashMap<String,String>>>>{


        public ParserTask(String directionMode) {
            this.directionMode = directionMode;
        }

        String directionMode = "driving";

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(@NonNull String... strings) {

            List<List<HashMap<String,String>>> route = null;
            try {

                JSONObject jOb  = new JSONObject(strings[0]);//creates an instance of the JSONObject
                Direction direct = new Direction();//creates an instance of the Directions class
                route = direct.parse(jOb);
            } catch (JSONException e){
                e.printStackTrace();
            }
            return route;
        }
//This will create the polyline between the two locations
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            ArrayList<LatLng> points;
            PolylineOptions poly = null;
            for (int i = 0; i < lists.size(); i++) {
                points = new ArrayList<>();
                poly = new PolylineOptions();
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
                // Adding all the points in the route to LineOptions
                poly.addAll(points);
                if (directionMode.equalsIgnoreCase("walking")) {
                    poly.width(10);
                    poly.color(Color.MAGENTA);
                } else {
                    poly.width(20);
                    poly.color(Color.BLUE);
                }
                Log.d("mylog", "onPostExecute lineoptions decoded");
            }
                if(poly!= null){
                    map.addPolyline(poly);
                } else {
                    Toast.makeText(MainActivity.this, "Directions not found", Toast.LENGTH_SHORT).show();
                }
            }
        }
}


