package com.example.gps;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GetNeatbyPlacedata extends AsyncTask<Object, String, String> {


    String Googlepacecdata;
    GoogleMap mmap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {
        mmap = (GoogleMap) objects[0];
        url = (String) objects[1];
        Downloaduri downloaduri = new Downloaduri();
        try {
            Googlepacecdata = downloaduri.readurl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Googlepacecdata;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyplacelist = null;
       Dataparser parser=new Dataparser();
       nearbyplacelist=parser.parse(s);
       shownearbyplaces(nearbyplacelist);

    }


    private void shownearbyplaces(List<HashMap<String, String>> nearbyplaceslist) {

        for (int i = 0; i < nearbyplaceslist.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googleplaces = nearbyplaceslist.get(i);

            String Placename = googleplaces.get("Placename");
            String vicinity = googleplaces.get("vicinity");
            double lat = Double.parseDouble(Objects.requireNonNull(googleplaces.get("lat")));
            double lng = Double.parseDouble(Objects.requireNonNull(googleplaces.get("lng")));

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(Placename + " : " + vicinity);

            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.p));


            mmap.addMarker(markerOptions);
            mmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mmap.animateCamera(CameraUpdateFactory.zoomBy(10));


        }

    }


}
