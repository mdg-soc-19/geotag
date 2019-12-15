package com.example.android.geotag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class map extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, LocationSource.OnLocationChangedListener {

    private GoogleMap mMap;
    LocationManager locationManager;

    //widget
    private EditText mSearchText;

    //marker
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);

        //editText searchbar
        mSearchText = (EditText) findViewById(R.id.input_search);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // mMap.setMyLocationEnabled(true);

        //position the myLocation button
        View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 300);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        //check network provider
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //get latitude
                    double latitude = location.getLatitude();
                    //get longitude
                    double longitude = location.getLongitude();
                    //instantiate LatLng class
                    LatLng latLng = new LatLng(latitude, longitude);
                    //instantiate Geocoder class
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude,1);
                        String str = addressList.get(0).getLocality()+", ";
                        str += addressList.get(0).getCountryName();
                        //setInfoText
                        //infoTextview
                        TextView infoTextview = findViewById(R.id.locationInfoText);
                        infoTextview.setText(str);

                        //add marker
                        MarkerOptions a = new MarkerOptions().position(latLng);
                        marker = mMap.addMarker(a);
                        marker.setDraggable(true);

                        //mMap.addMarker(new MarkerOptions().position(latLng).title(str)).setDraggable(true);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

        //check gps provider
        else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //get latitude
                    double latitude = location.getLatitude();
                    //get longitude
                    double longitude = location.getLongitude();
                    //instantiate LatLng class
                    LatLng latLng = new LatLng(latitude, longitude);
                    //instantiate Geocoder class
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude,1);
                        String str = addressList.get(0).getLocality()+", ";
                        str += addressList.get(0).getCountryName();
                        //setInfoText
                        //infoTextview
                        TextView infoTextview = findViewById(R.id.locationInfoText);
                        infoTextview.setText(str);


                        //add marker
                        MarkerOptions a = new MarkerOptions().position(latLng);
                        marker = mMap.addMarker(a);
                        marker.setDraggable(true);

                        //mMap.addMarker(new MarkerOptions().position(latLng).title(str)).setDraggable(true);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

    }

    private void init(){

        mSearchText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // execute method for searching
                    geoLocate();

                    return true;
                }
                return false;
            }
        });
    }

    private void geoLocate(){

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(map.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            //something
        }

        if(list.size() > 0){
            Address address = list.get(0);
            double latitude = address.getLatitude();
            double longitude = address.getLongitude();

            //instantiate LatLng class
            LatLng latLng = new LatLng(latitude, longitude);
            //instantiate Geocoder class
            Geocoder newgeocoder = new Geocoder(getApplicationContext());
            try {
                List<Address> addressList = newgeocoder.getFromLocation(latitude, longitude,1);
                String str = new String();
                str += addressList.get(0).getLocality()+", ";
                str += addressList.get(0).getCountryName();
                //setInfoText
                //infoTextview
                TextView infoTextview = findViewById(R.id.locationInfoText);
                infoTextview.setText(str);

                //add marker
                MarkerOptions a = new MarkerOptions().position(latLng);
                marker.setPosition(latLng);
                marker.setDraggable(true);

                //mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        init();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //TODO update infoText
        //TODO update marker

        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        //instantiate LatLng class
        LatLng latLng = new LatLng(latitude, longitude);

        //update marker
        MarkerOptions a = new MarkerOptions().position(latLng);
        marker = mMap.addMarker(a);
        marker.setDraggable(true);
    }
}