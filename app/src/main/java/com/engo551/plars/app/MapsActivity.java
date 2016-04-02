package com.engo551.plars.app;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
        map.setInfoWindowAdapter(new MyInfoWindowAdapter());

        final LatLng CALGARY = new LatLng(51.0486, -114.0708);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(CALGARY, 10));

        String[] markers = intent.getStringArrayExtra("markers");
        String markerStyle = intent.getStringExtra("markerStyle");

        drawMarkers(markers);

        //TODO: Center on current user location
        //TODO: Draw marker according to type
        //TODO: Add info window click event***add to class? popup thing
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.restaurant_info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            boolean imageLoaded = false;

            String snippet = marker.getSnippet();
            String[] metadata = snippet.split(",");
            String rating = metadata[0];
            String type = metadata[1];
            String picture_url = metadata[2];

            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.type));
            tvSnippet.setText(type);

            ImageView image = ((ImageView)myContentsView.findViewById(R.id.image));
            if (!imageLoaded) {
                Picasso.with(getApplicationContext())
                        .load("http:" + picture_url)
                        .placeholder(R.drawable.star_empty)
                        .into(image, new MarkerCallback(marker));
                imageLoaded = true;
            }



            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    public class MarkerCallback implements Callback {
        Marker marker=null;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }
    }

    private boolean drawMarkers(String[] markers){
        for (int i = 0; i < markers.length; i++) {
            String[] marker = markers[i].split(",");
            drawMarker(marker[RestaurantDatabaseHelper.NAME_COL],
                    marker[RestaurantDatabaseHelper.RATING_COL] +
                            "," + marker[RestaurantDatabaseHelper.TYPE_COL] +
                            "," + marker[RestaurantDatabaseHelper.PICTURE_COL],
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
