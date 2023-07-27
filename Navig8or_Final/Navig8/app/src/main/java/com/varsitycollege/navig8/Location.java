package com.varsitycollege.navig8;

import com.google.android.gms.maps.model.LatLng;

public class Location {

    //

    String marker_title;
    LatLng latLng;

    public Location(String marker_title, LatLng latLng) {
        this.marker_title = marker_title;
        this.latLng = latLng;
    }

    public String getMarker_title() {
        return marker_title;
    }

    public void setMarker_title(String marker_title) {
        this.marker_title = marker_title;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
