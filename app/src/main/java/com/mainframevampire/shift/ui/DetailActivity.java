package com.mainframevampire.shift.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mainframevampire.shift.Adapter;
import com.mainframevampire.shift.R;
import com.mainframevampire.shift.data.model.Shift;

import java.util.Arrays;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String SHIFT_DETAIL = "SHIFT_DETAIL" ;
    private String mStartTime = "";
    private String mEndTime = "";
    private String mStartLatitude = "";
    private String mStartLongtitude = "";
    private String mEndLatitude = "";
    private String mEndLongtitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Parcelable parcelable = intent.getParcelableExtra(SHIFT_DETAIL);
        Shift shift = (Shift) parcelable;
        mStartTime = shift.getStart();
        mEndTime = shift.getEnd();
        mStartLatitude = shift.getStartLatitude();
        mStartLongtitude = shift.getStartLongitude();
        mEndLatitude = shift.getEndLatitude();
        mEndLongtitude = shift.getEndLongitude();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker for shift start location
        String startTitle = "Shift started on " + mStartTime.substring(0, 10) + " " + mStartTime.substring(11, 19);
        LatLng startLocation = new LatLng(Double.parseDouble(mStartLatitude), Double.parseDouble(mStartLongtitude));
        googleMap.addMarker(new MarkerOptions().position(startLocation)
                .title(startTitle));

        // Add a marker for shift start location
        String endTitle = "Shift ended on " + mEndTime.substring(0, 10) + " " + mEndTime.substring(11, 19);
        LatLng endLocation = new LatLng(Double.parseDouble(mEndLatitude), Double.parseDouble(mEndLongtitude));
        googleMap.addMarker(new MarkerOptions().position(endLocation)
                .title(endTitle));

        //zome two marker as close as possible
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(startLocation);
        builder.include(endLocation);
        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 30, 25, 5);
        googleMap.animateCamera(cu);

    }


}
