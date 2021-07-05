package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class InicioActivity extends AppCompatActivity implements Handler.Callback, ListaLocalesFragment.Callback{

    private FirebaseAuth auth;
    private DatabaseReference ref;
    private LocalesDP localesDP;
    private ArrayList<LocalesDP> listaLocales;
    private ListaLocalesFragment lista;
    private LocalesDetailActivity local;
    private String cityUser, uID;
    private Handler handler;
    private TextView saludo, infoCiudad;
    //private DBHelper db;
    private TextView locales;
    private String cityStr = "";
    private String current = "";
    private String nameUser = "";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //Get that instance saved in the previous activity
        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);


        auth = FirebaseAuth.getInstance();
        //Log.wtf("HOLA", "holaaa");
        FirebaseUser user = auth.getInstance().getCurrentUser();

        saludo = findViewById(R.id.saludo);
        infoCiudad = findViewById(R.id.infoCiudad);
        String resultadoLocales = "";
       /* saludo.setText("Hola, "+ user.getDisplayName().substring(0,1).toUpperCase() +
                user.getDisplayName().substring(1,user.getDisplayName().length())); */



        FirebaseDatabase db =FirebaseDatabase.getInstance();
        ref = db.getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                buscaLocales();
                saludo.setText("Hola, "+ nameUser.substring(0,1).toUpperCase() +
                        nameUser.substring(1,nameUser.length()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.child("users").orderByChild("uID").equalTo(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key=childSnapshot.getKey();
                    cityUser = dataSnapshot.child("city").getValue(String.class);
                    nameUser = dataSnapshot.child("uname").getValue(String.class);
                    //Log.wtf("CIUDADFUNC",cityUser);
                }
                infoCiudad.setText("Mostrando lugares en " + cityUser);
                current = "cityUser";
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName){}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.wtf("TAG","murio");
            }
        });
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        FragmentManager manager = getSupportFragmentManager();
        Fragment f = manager.findFragmentByTag("Fragmento");
        FragmentTransaction transaction =  manager.beginTransaction();
        transaction.remove(f);
        transaction.commit();
        buscaLocales();
    }*/

    private void buscaLocales(){
        listaLocales = new ArrayList<>();
        FirebaseUser user = auth.getInstance().getCurrentUser();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.wtf("buscaLocales()", listaLocales.toString());
                ponRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("lugares").orderByChild("ciudad").equalTo(cityUser).addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                localesDP = new LocalesDP();
                localesDP.setuID(dataSnapshot.getKey());
                localesDP.setCalif(dataSnapshot.child("calif").getValue(String.class));
                localesDP.setCreador(dataSnapshot.child("creador").getValue(String.class));
                localesDP.setDisca(dataSnapshot.child("disca").getValue(String.class));
                localesDP.setNombre(dataSnapshot.child("nombre").getValue(String.class));
                localesDP.setCiudad(dataSnapshot.child("ciudad").getValue(String.class));
                localesDP.setTipo(dataSnapshot.child("tipo").getValue(String.class));
                localesDP.setUbi(dataSnapshot.child("ubi").getValue(String.class));
                listaLocales.add(localesDP);
            }
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}


            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName){}

            public void onCancelled(@NonNull DatabaseError error) {
                Log.wtf("TAG","murio");
            }
        });
    }

    private void buscaLocalesCiudad(String ciudad){
        listaLocales = new ArrayList<>();
        Log.wtf("buscarLocales()",ciudad);
        FirebaseUser user = auth.getInstance().getCurrentUser();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.wtf("buscaLocales()", listaLocales.toString());
                ponRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (ciudad != "All") {
            if (ciudad != cityUser) {
                infoCiudad.setText("Mostrando lugares en " + ciudad);
            }
            ref.child("lugares").orderByChild("ciudad").
                    equalTo(ciudad).addChildEventListener(new ChildEventListener() {

                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    localesDP = new LocalesDP();
                    localesDP.setuID(dataSnapshot.getKey());
                    localesDP.setCalif(dataSnapshot.child("calif").getValue(String.class));
                    localesDP.setCreador(dataSnapshot.child("creador").getValue(String.class));
                    localesDP.setDisca(dataSnapshot.child("disca").getValue(String.class));
                    localesDP.setNombre(dataSnapshot.child("nombre").getValue(String.class));
                    localesDP.setCiudad(dataSnapshot.child("ciudad").getValue(String.class));
                    localesDP.setTipo(dataSnapshot.child("tipo").getValue(String.class));
                    localesDP.setUbi(dataSnapshot.child("ubi").getValue(String.class));
                    listaLocales.add(localesDP);
                }

                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                }


                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                public void onCancelled(@NonNull DatabaseError error) {
                    Log.wtf("TAG", "murio");
                }
            });
        }
        else{
            infoCiudad.setText("Mostrando todos los lugares");
            ref.child("lugares").orderByChild("ciudad").addChildEventListener(new ChildEventListener() {

                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    localesDP = new LocalesDP();
                    localesDP.setuID(dataSnapshot.getKey());
                    localesDP.setCalif(dataSnapshot.child("calif").getValue(String.class));
                    localesDP.setCreador(dataSnapshot.child("creador").getValue(String.class));
                    localesDP.setDisca(dataSnapshot.child("disca").getValue(String.class));
                    localesDP.setNombre(dataSnapshot.child("nombre").getValue(String.class));
                    localesDP.setCiudad(dataSnapshot.child("ciudad").getValue(String.class));
                    localesDP.setTipo(dataSnapshot.child("tipo").getValue(String.class));
                    localesDP.setUbi(dataSnapshot.child("ubi").getValue(String.class));
                    listaLocales.add(localesDP);
                }

                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                }


                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                public void onCancelled(@NonNull DatabaseError error) {
                    Log.wtf("TAG", "murio");
                }
            });
        }
    }

    private void ponRecycler(){
        //Log.wtf("ponRecycler()","Poniendo ese");
        handler = new Handler(Looper.getMainLooper(), this);
        lista = new ListaLocalesFragment();
        lista.setArray(listaLocales);
        FragmentManager manager = getSupportFragmentManager();
        Fragment f = manager.findFragmentByTag("Fragmento");
        FragmentTransaction transaction =  manager.beginTransaction();
        if(lista==f)
            return;

        if(f!=null){
            //transaction.remove(f);
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        transaction.add(R.id.contenedorMain, lista, "Fragmento");
        transaction.commit();
    }

    public void misLocales(View v){
        Intent i = new Intent(this, UserpointsActivity.class);
        startActivity(i);
        finish();
    }

    public void refresh(View v){
        if (current == "cityUser"){
            buscaLocales();

            Snackbar.make(v,"Refrescando locales", Snackbar.LENGTH_SHORT).show();
        }
        else if (current == "CDMX"){
            buscaLocalesCiudad("CDMX");
            Snackbar.make(v,"Refrescando locales", Snackbar.LENGTH_SHORT).show();
        }
        else if (current == "Monterrey"){
            buscaLocalesCiudad("Monterrey");
            Snackbar.make(v,"Refrescando locales", Snackbar.LENGTH_SHORT).show();
        }
        else if (current == "Guadalajara"){
            buscaLocalesCiudad("Guadalajara");
            Snackbar.make(v,"Refrescando locales", Snackbar.LENGTH_SHORT).show();
        }
        else if (current == "Toluca"){
            buscaLocalesCiudad("Toluca");
            Snackbar.make(v,"Refrescando locales", Snackbar.LENGTH_SHORT).show();
        }
        else if (current == "All"){
           // buscaLocales();
            buscaLocalesCiudad("All");
        }
    }

    public void abrirCDMX(View v){
        buscaLocalesCiudad("CDMX");
        current = "CDMX";
    }

    public void abrirGDL(View v){
        buscaLocalesCiudad("Guadalajara");
        current = "Guadalajara";

    }

    public void abrirTOL(View v){
        buscaLocalesCiudad("Toluca");
        current = "Toluca";

    }

    public void abrirMTY(View v){
        buscaLocalesCiudad("Monterrey");
        current = "Monterrey";

    }

    public void abrirALL(View v){
        buscaLocalesCiudad("All");
        current = "All";
    }

    public void logout(View v){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("key", 0);
        editor.apply();
        Intent b = new Intent(this,MainActivity.class);
        b.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(b);
        this.finish();
    }


    @Override
    public boolean handleMessage(@NonNull Message msg) {
        return false;
    }

    @Override
    public void saludoEnActividad(int pos) {
        local = new LocalesDetailActivity();
        Intent i = new Intent(this, LocalesDetailActivity.class);
        LocalesDP local = listaLocales.get(pos);
        //Toast.makeText(this, "Local: "+listaLocales.get(pos).toString(), Toast.LENGTH_LONG).show();
        i.putExtra("uid", local.getuID());
        i.putExtra("nombre", local.getNombre());
        i.putExtra("tipo", local.getTipo());
        i.putExtra("ciudad", local.getCiudad());
        i.putExtra("calif", local.getCalif());
        i.putExtra("disca", local.getDisca());
        i.putExtra("ubi", local.getUbi());
        startActivity(i);
        //Toast.makeText(this, "Local: "+listaLocales.get(pos).toString(), Toast.LENGTH_LONG).show();
    }
}