package com.ateam.funshoppers.Main_navigation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransitMode;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.ateam.funshoppers.R;
import com.ateam.funshoppers.Utility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 1/25/2017.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,DirectionCallback,View.OnClickListener {

    private GoogleMap mMap;

    double lat=-34, lng=151, cLat=-34, cLng=151;
    ArrayList<LatLng> markerPoints;
    String  api;
    LatLng cloc,sydney;
    private String[] colors = {"#7fff7272", "#7f31c7c5", "#7fff8a00"};
    FloatingActionButton walk,car,bus,navigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Initializing
      walk=(FloatingActionButton)findViewById(R.id.walk);
        car=(FloatingActionButton)findViewById(R.id.car);
        bus=(FloatingActionButton)findViewById(R.id.bus);
        navigate=(FloatingActionButton)findViewById(R.id.navigate);
        navigate.setOnClickListener(this);
        walk.setOnClickListener(this);
        car.setOnClickListener(this);
        bus.setOnClickListener(this);
        markerPoints = new ArrayList<LatLng>();

        Intent i = getIntent();
        if (i != null) {
            if (i.hasExtra("lat")) {
                lat = i.getDoubleExtra("lat", -34);

            }
            if (i.hasExtra("lng")) {
                lng = i.getDoubleExtra("lng", 151);
            }

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        api=getString(R.string.google_map_key);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        sydney = new LatLng(lat, lng);

        GPSTracker gps = new GPSTracker(MapsActivity.this);
            cLat = gps.getLatitude();
            cLng = gps.getLongitude();
        cloc = new LatLng(cLat, cLng);


            Log.e("latlng", cloc.toString() + sydney.toString());




        if (Utility.checkLocationPermission(this)) {
            mMap.setMyLocationEnabled(true);
        }



        if (gps.canGetLocation()) {
            GoogleDirection.withServerKey(api).from(cloc).to(sydney).alternativeRoute(true).execute(this);

        }


    }


    //-------------------------------//


    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        /// Snackbar.make(btnRequestDirection, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        Log.e("success", "with status" + direction.getStatus());
        if (direction.isOK()) {
            //mMap.addMarker(new MarkerOptions().position(cloc));
            mMap.addMarker(new MarkerOptions().position(sydney).title("Destination"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

            for (int i = 0; i < direction.getRouteList().size(); i++) {
                Route route = direction.getRouteList().get(i);
                String color = colors[i % colors.length];
                ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.parseColor(color)));
            }

            //  btnRequestDirection.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDirectionFailure(Throwable t) {
        Log.e("failed",t.getMessage()+t.getLocalizedMessage()+" "+t.toString());
    }



    //for location permissio
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 12:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"permission granted",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this,"permission not granted",Toast.LENGTH_LONG).show();
                    onBackPressed();

//code for deny
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.walk:
                mMap.clear();
                GoogleDirection.withServerKey(api).from(cloc).to(sydney).alternativeRoute(true).transportMode(TransportMode.WALKING).execute(this);
                return;
            case R.id.bus:
                mMap.clear();
                GoogleDirection.withServerKey(api).from(cloc).to(sydney).alternativeRoute(true).transitMode(TransitMode.BUS).execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            ArrayList<LatLng> sectionPositionList = direction.getRouteList().get(0).getLegList().get(0).getSectionPoint();
                            for (LatLng position : sectionPositionList) {
                                mMap.addMarker(new MarkerOptions().position(position));
                            }

                            List<Step> stepList = direction.getRouteList().get(0).getLegList().get(0).getStepList();
                            ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(MapsActivity.this, stepList, 5, Color.RED, 3, Color.BLUE);
                            for (PolylineOptions polylineOption : polylineOptionList) {
                                mMap.addPolyline(polylineOption);
                            }
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Log.e("failed",t.getMessage()+t.getLocalizedMessage()+" "+t.toString());

                    }
                });
                return;
            case R.id.car:
                mMap.clear();
                GoogleDirection.withServerKey(api).from(cloc).to(sydney).alternativeRoute(true).transportMode(TransportMode.DRIVING).execute(this);
                return;
            case R.id.navigate:
                final Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?" + "saddr="
                                + cLat + "," + cLng + "&daddr="
                                + lat + "," + lng));
                intent.setClassName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity");
                startActivity(intent);
                return;

        }

    }
    private  void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Yout GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}


