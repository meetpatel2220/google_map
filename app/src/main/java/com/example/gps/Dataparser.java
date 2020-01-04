package com.example.gps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dataparser {


    private HashMap<String ,String> getPlace(JSONObject googleplacejson){

        HashMap<String,String> googleplacemap=new HashMap<>();
        String placename="-NA-";
        String vicinity ="-NA-";
        String latitude="";
        String longitude="";
        String reference="";
        try {
        if(!googleplacejson.isNull("name")) {


            placename = googleplacejson.getString("name");
        }
        if(!googleplacejson.isNull("vicinity")){

            vicinity=googleplacejson.getString("vicinity");

        }

        latitude=googleplacejson.getJSONObject("geometry").getJSONObject("location").getString("lat");
        longitude=googleplacejson.getJSONObject("geometry").getJSONObject("location").getString("lng");

        reference=googleplacejson.getString("reference");


        googleplacemap.put("Place_name",placename);
        googleplacemap.put("vicinity",vicinity);
            googleplacemap.put("lat",latitude);
            googleplacemap.put("lng",longitude);




        } catch (JSONException e) {
                e.printStackTrace();
            }

        return googleplacemap;

    }

    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray){

        int count=jsonArray.length();
        List<HashMap<String,String>> placelist=new ArrayList<>();
        HashMap<String,String> placemap=null;
        for(int i=0;i<count;i++){
            try {
                placemap=getPlace((JSONObject) jsonArray.get(i));
                placelist.add(placemap);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return placelist;


    }

    public List<HashMap<String,String>> parse(String Jsondata){

        JSONObject jsonObject;
        JSONArray jsonArray = null;
        try {
            jsonObject=new JSONObject(Jsondata);
            jsonArray=jsonObject.getJSONArray("results");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPlaces(jsonArray);


    }
}
