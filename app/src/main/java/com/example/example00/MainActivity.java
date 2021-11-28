package com.example.example00;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.example00.databinding.ActivityMainBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    SupportMapFragment mapFragment;
    GoogleMap map;
    MarkerOptions myLocationMarker;
    MarkerOptions myLocationMarker2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;
                /*********************************This part is for the setMyLocationEnabled********
                 try{
                 map.setMyLocationEnabled(true);
                 }catch(SecurityException e){
                 e.printStackTrace();
                 }
                 ********/
            }
        });

    }



    /*********************************This part is for the setMyLocationEnabled********
     @Override
     protected void onResume() {
     super.onResume();

     if (map != null) {

     try{
     map.setMyLocationEnabled(true);
     }catch(SecurityException e){
     e.printStackTrace();
     }
     }
     }

     @Override
     protected void onPause() {
     super.onPause();

     if (map != null) {
     try{
     map.setMyLocationEnabled(false);
     }catch(SecurityException e){
     e.printStackTrace();
     }
     }
     }
     **************************************/



    public void myLocation(View view){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try{

            GPSListener gpsListener = new GPSListener();
            long minTime = 1000;
            float minDistance = 0;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);

        }catch(SecurityException e){
            e.printStackTrace();
        }
    }


    class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            showCurrentLocation(latitude, longitude);
            showCurrentLocation2(37.511716875127455, 127.07843285782081);

        }
    }

    private void showCurrentLocation(double latitude, double longitude){
        LatLng curPoint = new LatLng(latitude, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 13));
        showMyLocationMarker(curPoint);

    }
    private void showCurrentLocation2(double latitude, double longitude){
        LatLng curPoint2 = new LatLng(latitude, longitude);
        showMyLocationMarker2(curPoint2);

    }


    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };

    private void showMyLocationMarker(LatLng curPoint){
        if(myLocationMarker == null){
            myLocationMarker = new MarkerOptions();
            myLocationMarker.position(curPoint).title("puppy\n").snippet("Obtained from GPS\n")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.loc1));
            map.addMarker(myLocationMarker);
        }else{
            myLocationMarker.position(curPoint);
        }
    }

    private void showMyLocationMarker2(LatLng curPoint2){
        if(myLocationMarker2 == null){
            myLocationMarker2 = new MarkerOptions();
            myLocationMarker2.position(curPoint2).title("hello\n").snippet("Obtained from GPS\n")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.loc2));
            map.addMarker(myLocationMarker2);
        }else{
            myLocationMarker2.position(curPoint2);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "Satellite Map");
        menu.add(0, 2, 0, "Normal Map");
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case 1:
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case 2:
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
        }
        return false;
    }



}