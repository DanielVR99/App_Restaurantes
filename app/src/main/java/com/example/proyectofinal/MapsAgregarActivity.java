package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

public class MapsAgregarActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final int PERMISO_MAP = 0;

    private static final long UPDATE = 120*1000;
    private static final long FASTEST = 120*1000;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    private GoogleMap mMap;

    private Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_agregar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationRequest request = new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(UPDATE);
        request.setFastestInterval(FASTEST);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(request,
                    new LocationCallback(){
                        public void onLocationResult(LocationResult locationResult){
                            lastLocation = locationResult.getLastLocation();
                            Log.wtf("LOCAlization", locationResult.getLastLocation().toString());

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()),14));
                        }
                    },
                    Looper.myLooper());
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng tecTol = new LatLng(19.269046634310097, -99.70542442775053);
        mMap.addMarker(new MarkerOptions().position(tecTol).title("Marker in Tec Tol"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tecTol, 18));
        mMap.setOnMapClickListener(this);
        habilitarMyLocation();
    }

    //Esto es para pedir la ubicacion al usuario
    private void habilitarMyLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permisos, PERMISO_MAP);
        } else {
            mMap.setMyLocationEnabled(true);
        }
    }

    //Click derecho+Generate+Override+onRequestPermissionsResult
    //Es para escuchar lo que el usuario dijo sobre el permiso
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISO_MAP && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                mMap.setMyLocationEnabled(true);
        }else {
            Toast.makeText(this, "Permiso negadoooo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
                .position(latLng).title("Marcador dinamico").alpha(0.8f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        builder = new AlertDialog.Builder(MapsAgregarActivity.this);

        builder.setTitle("Confirmar");
        builder.setMessage("¿Estás seguro de colocar esa ubicación?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MapsAgregarActivity.this, ""+latLng, Toast.LENGTH_SHORT).show();
                regresarPL(latLng.latitude,latLng.longitude);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMap.clear();
                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void clickFab(View view){
        double lat = lastLocation.getLatitude();
        double lon = lastLocation.getLongitude();

        Toast.makeText(MapsAgregarActivity.this, ""+lat+lon, Toast.LENGTH_SHORT).show();

        builder = new AlertDialog.Builder(MapsAgregarActivity.this);

        builder.setTitle("Confirmar");
        builder.setMessage("¿Estás seguro de utilizar tu ubicación actual?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                regresarCL(lat,lon);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMap.clear();
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void regresarCL(double lat, double lon){
        Intent retorno = new Intent();
        retorno.putExtra("lat", lat);
        retorno.putExtra("lon", lon);

        // como lo "enviamos" ?
        setResult(Activity.RESULT_OK, retorno);

        finish();
    }
    public void regresarPL(double lat, double lon){
        Intent retorno = new Intent();
        retorno.putExtra("lat", lat);
        retorno.putExtra("lon", lon);

        // como lo "enviamos" ?
        setResult(Activity.RESULT_OK, retorno);

        finish();
    }
}