package com.varsitycollege.navig8;
//App creators: Mohamed Rajab-ST10116167, Reeselin Pillay-ST10117187,Terell Rangasamy-ST10117009, Fransua Somers-ST10117162, Lungelo Zungu-ST10116993
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Code attribution
//Links: https://www.youtube.com/watch?v=NtnK1UmVAnY
//Author: Tech Academy (2017)
//Accessed: 18 Oct. 2022
public class DataParser {

    public String latitude = "";
    public String longitude = "";

    private HashMap<String,String> getDurations(JSONArray googleDurationsJson){
        HashMap<String,String> googleDirectionsMap = new HashMap<>();
        String duration = "";
        String distance  = "";

        Log.d("json response", googleDurationsJson.toString());
        try{
            duration = googleDurationsJson.getJSONObject(0).getJSONObject("duration").getString("text");
            distance = googleDurationsJson.getJSONObject(0).getJSONObject("distance").getString("text");

            googleDirectionsMap.put("duration", duration);
            googleDirectionsMap.put("distance", distance);
        } catch (JSONException e){
            e.printStackTrace();
        }

        return  googleDirectionsMap;

    }

    private HashMap<String,String> getPlace(JSONObject googlePlaceJson){
        HashMap<String,String> googlePlaceMap = new HashMap<>();
        String Place_Name = "-NA-";
        String vicinity = "-NA-";
//        public String latitude = "";
//        public String longitude = "";
        String reference = "";

        try{
            if(!googlePlaceJson.isNull("name")){
                Place_Name = googlePlaceJson.getString("name");
        }
            if(!googlePlaceJson.isNull("vicinity")){
                vicinity = googlePlaceJson.getString("vicinity");

            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference = googlePlaceJson.getString("reference");

            googlePlaceMap.put("place_name", Place_Name);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng",longitude);
            googlePlaceMap.put("reference",reference);

    } catch (JSONException e){
            e.printStackTrace();
        }
         return  googlePlaceMap;
    }
    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray){
        int count = jsonArray.length();
        List<HashMap<String,String>> placeList = new ArrayList<>();
        HashMap<String,String> placeMap = null;
        for(int i = 0 ; i<count ;i++ ){
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placeList.add(placeMap);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        return  placeList;

    }

    public List<HashMap<String, String>> parse(String jsonData){
        JSONArray jsonArray = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }
     return  getPlaces(jsonArray);
    }

//    private String getDirections(){
//        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
//    }

    public String[] parseDirections(String s) {

        return  null;
    }
}
