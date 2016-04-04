package com.engo551.plars.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MapsActivity extends FragmentActivity implements
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback {

    private GoogleMap map;
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

        //TODO: Get user location as intent extra
        final LatLng MY_POSITION = new LatLng(51.080056, -114.129239);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(MY_POSITION, 12));

        //Draw current location
        drawMarker("My Position",
                ",,,myposition",
                "myposition",
                MY_POSITION.latitude,
                MY_POSITION.longitude);

        //Draw restaurant markers
        String[] markers = intent.getStringArrayExtra("markers");
        String markerStyle = intent.getStringExtra("markerStyle");

        drawMarkers(markers);

        map.setOnInfoWindowClickListener(
                new GoogleMap.OnInfoWindowClickListener() {
                    public void onInfoWindowClick(Marker marker) {
                        //TODO: Add type to ClickRecord database on click
                        String title = marker.getTitle();
                        String snippet = marker.getSnippet();
                        String[] metadata = snippet.split(",");
                        String address = metadata[2];

                        //The following opens google maps
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=+" + title + ", " + address + "\n");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                }
        );
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //This needs to be here for the OnInfoWindowClickListener to work....
    }


    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.restaurant_info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            boolean imageLoaded = false;

            //TODO: Snippet parser function?
            String snippet = marker.getSnippet();
            String[] metadata = snippet.split(",");
            String rating = metadata[0];
            String picture_url = metadata[1];
            String address = metadata[2];
            String type = metadata[3];

            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.info));
            if (type.equals("myposition"))
                tvSnippet.setText("");
            else
                tvSnippet.setText(address + "\n" + rating + " stars   " + type);

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
        Marker marker = null;

        MarkerCallback(Marker marker) {
            this.marker = marker;
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
                            "," + marker[RestaurantDatabaseHelper.PICTURE_COL] +
                            "," + marker[RestaurantDatabaseHelper.ADDRESS_COL] +
                            "," + marker[RestaurantDatabaseHelper.TYPE_COL],
                    marker[RestaurantDatabaseHelper.TYPE_COL],
                    Double.parseDouble(marker[RestaurantDatabaseHelper.LATITUDE_COL]),
                    Double.parseDouble(marker[RestaurantDatabaseHelper.LONGITUDE_COL]));
        }
        return true;
    }

    /**
     *
     * @param title     Restaurant name
     * @param snippet   String format: "rating,pictureUrl,address,type"
     * @param type      Restaurant type
     * @param latitude
     * @param longitude
     * @return
     */
    private boolean drawMarker(String title, String snippet, String type, double latitude, double longitude)
    {
        LatLng latLng = new LatLng(latitude, longitude);

        BitmapDescriptor bitmap;
        switch(type.toLowerCase()) {
            case "asian":
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.green);
                break;
            case "cafe":
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.pink);
                break;
            case "canadian":
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.red);
                break;
            case "pub&bar":
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.black);
                break;
            case "italian":
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.green);
                break;
            case "myposition":
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.myposition);
                break;
            default:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.grey);
                break;
        }

        map.addMarker(new MarkerOptions().position(latLng)
                .title(title)
                .snippet(snippet)
                .icon(bitmap));
        return true;
    }
}
