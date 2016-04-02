package com.engo551.plars.app;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private RestaurantDatabaseHelper rDb;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        intent = getIntent();
        if (!intent.hasExtra("markerStyle"))
            throw new AssertionError();
        if (!intent.hasExtra("markers"))
            throw new AssertionError();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        final LatLng CALGARY = new LatLng(51.0486, -114.0708);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(CALGARY, 10));

        String[] markers = intent.getStringArrayExtra("markers");
        String markerStyle = intent.getStringExtra("markerStyle");

        drawMarkers(markers);

        //TODO: Center on current user location
        //TODO: Draw marker according to type
        //TODO: Add info window click event***add to class? popup thing
    }

    private boolean drawMarkers(String[] markers){
        for (int i = 0; i < markers.length; i++) {
            String[] marker = markers[i].split(",");
            drawMarker(marker[RestaurantDatabaseHelper.NAME_COL],
                    marker[RestaurantDatabaseHelper.TYPE_COL],
                    Double.parseDouble(marker[RestaurantDatabaseHelper.LATITUDE_COL]),
                    Double.parseDouble(marker[RestaurantDatabaseHelper.LONGITUDE_COL]));
        }
        return true;
    }

    private boolean drawMarker(String title, String snippet, double latitude, double longitude)
    {
        LatLng latLng = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(latLng)
                .title(title)
                .snippet(snippet));
        return true;
    }
}
