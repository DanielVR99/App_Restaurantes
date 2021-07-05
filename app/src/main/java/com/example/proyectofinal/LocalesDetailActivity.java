package com.example.proyectofinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class LocalesDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FirebaseAuth auth;
    private DatabaseReference ref;

    private TextView nombre, tipo, ciudad, calificacion, disca;
    private ArrayList<ResenasDP> listaLocales;
    private ResenasDP  resenasDP;
    private double lat, lon;
    private String ubi, uID,calif;
    private int pos;
    private double reseCount;
    private MapView mapV;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private String nombrePMapa = "Marker";

    private Handler handler;
    private  ResenasFragment listaResenas;

    protected void onCreate(Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase db =FirebaseDatabase.getInstance();
        FirebaseUser user = auth.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_locales);
        Intent intent = getIntent();
        nombre = findViewById(R.id.nombreLocalTV);
        tipo = findViewById(R.id.tipoLocalTV);
        ciudad = findViewById(R.id.ciudadLocalTV);
        calificacion = findViewById(R.id.calificacionTV);
        disca = findViewById(R.id.discaTV);


        nombrePMapa = intent.getStringExtra("nombre");


        calif = intent.getStringExtra("calif");
        nombre.setText(intent.getStringExtra("nombre"));
        tipo.setText(intent.getStringExtra("tipo"));
        ciudad.setText(intent.getStringExtra("ciudad"));
        calificacion.setText(intent.getStringExtra("calif"));
        disca.setText(intent.getStringExtra("disca"));
        ubi = intent.getStringExtra("ubi");
        uID =  intent.getStringExtra("uid");
        //Toast.makeText(this, uID,  Toast.LENGTH_SHORT).show();
        buscaResenas();

        Bundle mVBundle = null;
        if (savedInstanceState != null) {
            mVBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapV = (MapView) findViewById(R.id.mapView);
        mapV.onCreate(mVBundle);
        mapV.getMapAsync(this);


    }

    public void verMapa(View v){
        Intent i = new Intent(this, VerMapsLocalActivity.class);
        if(ubi.equals("coordenadas")){
            Snackbar.make(v, "Este local aún no cuenta con ubicación",  Snackbar.LENGTH_SHORT).show();
        }else{
            i.putExtra("ubicate", ubi);
            startActivity(i);
        }
    }

    public void verComoLlegar(View v){

        if(ubi.equals("coordenadas")){
            Snackbar.make(v, "Este local aún no cuenta con ubicación",  Snackbar.LENGTH_SHORT).show();
        }else{
            StringTokenizer st = new StringTokenizer(ubi, "$");
            double lat = Double.parseDouble(st.nextToken());
            double lon = Double.parseDouble(st.nextToken());
            Uri navigationIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    public void verRese(){
        //handler = new Handler(Looper.getMainLooper(), this);
        listaResenas = new ResenasFragment();
        listaResenas.setArray(listaLocales);
        FragmentManager manager = getSupportFragmentManager();
        Fragment f = manager.findFragmentByTag("Fragmento");
        FragmentTransaction transaction =  manager.beginTransaction();
        if(listaResenas==f)
            return;

        if(f!=null){
            transaction.remove(f);
        }
        transaction.add(R.id.resenaContainer, listaResenas, "Fragmento");
        transaction.commit();
    }

    public void buscaResenas(){
        FirebaseDatabase db =FirebaseDatabase.getInstance();
        ref = db.getReference();
        listaLocales = new ArrayList<>();
        FirebaseUser user = auth.getInstance().getCurrentUser();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                verRese();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("lugares").orderByKey().equalTo(uID).addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                //Log.wtf("Resenas", ""+dataSnapshot.child("resena").getChildrenCount());
                reseCount = dataSnapshot.child("resena").getChildrenCount();

                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.child("resena").getValue();
                Object[] hola = td.values().toArray();//Para obtener solo los valores de las reseñas

                for(int i = 0;i<=reseCount-1;i++){
                    resenasDP = new ResenasDP();
                    String reseStr = (String) hola[i];
                    StringTokenizer st2 = new StringTokenizer(reseStr, "$");

                    resenasDP.setCalif(st2.nextToken());
                    resenasDP.setText(st2.nextToken());

                    listaLocales.add(resenasDP);
                    Log.wtf("Ojala",listaLocales.get(i).toString());
                }

            }
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}


            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName){}

            public void onCancelled(@NonNull DatabaseError error) {
                Log.wtf("TAG","murio");
            }
        });
    }

    public void creaRese(View v){
        Intent i = new Intent(this, ReseniaActivity.class);
        i.putExtra("id",uID);
        i.putExtra("count",reseCount);
        i.putExtra("calif", calif);
        startActivityForResult(i, 1);
        // finish();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // A contact was picked.  Here we will just display it
                // to the user.
                buscaResenas();
                verRese();
                //Intent intent=new Intent();
              //  intent.setClass(this, this.getClass());
               // startActivity(intent);
               // finish();
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapV.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapV.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapV.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapV.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        StringTokenizer st = new StringTokenizer(ubi, "$");
        double lat, lon;
        lat = 0;
        lon = 0;
        if (!ubi.equals("coordenadas")) {
            lat = Double.parseDouble(st.nextToken());
            lon = Double.parseDouble(st.nextToken());
            map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(nombrePMapa));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lon), 10));
        }
        else {
            View namebar = findViewById(R.id.mapView);
            ((ViewGroup) namebar.getParent()).removeView(namebar);
        }
    }

    @Override
    protected void onPause() {
        mapV.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapV.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapV.onLowMemory();
    }
}