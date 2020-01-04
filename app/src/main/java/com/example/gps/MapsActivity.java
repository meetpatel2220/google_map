package com.example.gps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    int PROXIMITY_RADIUS=10000;
    Double latitude,longitude;
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location lastlocation;
    private Marker currentlocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    private EditText e1;
    private Button b1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checklocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void onclick(View v) {
        if (v.getId() == R.id.search) {
            mMap.clear();
            e1 = findViewById(R.id.editText);
            String location = e1.getText().toString();
            List<Address> addressList = null;
            MarkerOptions mo = new MarkerOptions();
            if (!location.equals("")) {
                Geocoder geocoder = new Geocoder(this);

                try {
                    addressList = geocoder.getFromLocationName(location, 5);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < addressList.size(); i++) {
                    Address myAddress = addressList.get(i);
                    LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                    mo.position(latLng);
                    mo.title(location);
                    mMap.addMarker(mo);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }

            }
        }
        Object datatransfer[] =new Object[2];

        if (v.getId() == R.id.hospital) {

            mMap.clear();
            String hospital="hospital";
            String url=getUrl(latitude,longitude,hospital);
         datatransfer[0]=mMap;
            datatransfer[1]=url;
            GetNeatbyPlacedata getNeatbyPlacedata=new GetNeatbyPlacedata();

            getNeatbyPlacedata.execute(datatransfer);
            Toast.makeText(this, "Showing nearby Hospitals", Toast.LENGTH_SHORT).show();


        }
        if (v.getId() == R.id.School) {

            mMap.clear();
            String school="school";
            String url=getUrl(latitude,longitude,school);
            datatransfer[0]=mMap;
            datatransfer[1]=url;
            GetNeatbyPlacedata getNeatbyPlacedata=new GetNeatbyPlacedata();

            getNeatbyPlacedata.execute(datatransfer);
            Toast.makeText(this, "Showing nearby schools", Toast.LENGTH_SHORT).show();

        }
        if (v.getId() == R.id.restaurant) {

            mMap.clear();
            String restaurant="restaurant";
            String url=getUrl(latitude,longitude,restaurant);
            datatransfer[0]=mMap;
            datatransfer[1]=url;
            GetNeatbyPlacedata getNeatbyPlacedata=new GetNeatbyPlacedata();

            getNeatbyPlacedata.execute(datatransfer);
            Toast.makeText(this, "Showing nearby restaurants", Toast.LENGTH_SHORT).show();


        }

    }

    private String getUrl(Double latitude,Double longitude,String nearbyplace){

        StringBuilder googleplacesapi=new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleplacesapi.append("location"+latitude+" ,"+longitude);
        googleplacesapi.append("&radius"+PROXIMITY_RADIUS);
        googleplacesapi.append("&type="+nearbyplace);
        googleplacesapi.append("&sensor=true");
        googleplacesapi.append("&key"+PROXIMITY_RADIUS);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            buildapiclient();
            mMap.setMyLocationEnabled(true);
        }

    }

    protected synchronized void buildapiclient() {


        client = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastlocation = location;
        if (currentlocationMarker != null) {
            currentlocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.p));
        currentlocationMarker = mMap.addMarker(markerOptions);

//        Bitmap.Config conf=Bitmap.Config.ARGB_8888;
//        Bitmap bmp=Bitmap.createBitmap(80,80,conf);
//        Canvas can=new Canvas(bmp);
//        Paint color=new Paint();
//        color.setTextSize(35);
//        color.setColor(Color.BLACK);
//        can.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.p),0,0,color);
//         can.drawText("Username!",30,40,color);
//        currentlocationMarker=mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(bmp)).anchor(0.5f,1));
//


        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if (client != null) {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);

        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //  LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);

        }


    }

    public boolean checklocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);

            }
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            buildapiclient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                }

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
