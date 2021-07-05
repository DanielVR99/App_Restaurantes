package com.example.proyectofinal;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.StringTokenizer;

public class VerMapsLocalActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_maps_local);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent = getIntent();
        String ubiStr = intent.getStringExtra("ubicate");
        StringTokenizer st = new StringTokenizer(ubiStr, "$");
        double lat = Double.parseDouble(st.nextToken());
        double lon = Double.parseDouble(st.nextToken());
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Local"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14));
    }
}