package com.varsitycollege.navig8;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class FetchData extends AsyncTask<Object,String,String> {
    String googleNearbyPlace;
    GoogleMap googleMap;
    String url;

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearbyPlaceList=null;
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);
        showNearbyPlace(nearbyPlaceList);

    }

    @Override
    protected String doInBackground(Object... objects) {
        googleMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        DownloadUrl downloadUrl = new DownloadUrl();
        try{
            googleNearbyPlace = downloadUrl.retrieveUrl(url);
        } catch (IOException e){
            e.printStackTrace();
        }
        return  googleNearbyPlace;
    }

    private  void showNearbyPlace(List<HashMap<String,String>> nearbyPlaceList){
        for(int i = 0;i<nearbyPlaceList.size();i++){
            MarkerOptions markerOptions;
            HashMap<String,String> googlePlace = nearbyPlaceList.get(i);
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            LatLng latLng = new LatLng(lat,lng);
            googleMap.addMarker(new MarkerOptions().position(latLng).title(placeName+":"+vicinity)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }


}