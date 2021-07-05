package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// se implementa el fragmento en el que se da la opcion de editar y borrar
public class UserpointsActivity extends AppCompatActivity implements Handler.Callback, ListaLocalesFragmentUser.Callback {

    private FirebaseAuth auth;
    private DatabaseReference ref;
    private LocalesDP localesDP;
    private ListaLocalesFragmentUser lista;
    ArrayList<LocalesDP> objList;
    private Handler handler;
    private Integer selectedPosition;
    private LocalesDetailActivity local;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpoints);



        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        FirebaseDatabase db =FirebaseDatabase.getInstance();
        ref = db.getReference();
        objList = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.wtf("El numero magico", ""+objList.size());
                Log.wtf("El gran obj", objList.toString());
                tostito();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("lugares").orderByChild("creador").equalTo(userID).addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    localesDP = new LocalesDP();
                    localesDP.setCalif(dataSnapshot.child("calif").getValue(String.class));
                    localesDP.setCreador(userID);
                    localesDP.setuID(dataSnapshot.getKey());
                    localesDP.setDisca(dataSnapshot.child("disca").getValue(String.class));
                    localesDP.setNombre(dataSnapshot.child("nombre").getValue(String.class));
                    localesDP.setCiudad(dataSnapshot.child("ciudad").getValue(String.class));
                    localesDP.setTipo(dataSnapshot.child("tipo").getValue(String.class));
                    localesDP.setUbi(dataSnapshot.child("ubi").getValue(String.class));
                    localesDP.setId(dataSnapshot.child("id").getValue(String.class));
                    objList.add(localesDP);
            }
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}


            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName){}

            public void onCancelled(@NonNull DatabaseError error) {
                Log.wtf("TAG","murio");
            }
        });

    }


    public void agregarLocal(View v){
        //finish();
        Intent i = new Intent(this, AgregarActivity.class);
        startActivityForResult(i, 1);
    }

    public void editarLocal(View v){
        //finish();
        Intent i = new Intent(this, EditActivity.class);

        startActivityForResult(i, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // A contact was picked.  Here we will just display it
                // to the user.
                onBackPressed();

            }
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(UserpointsActivity.this, InicioActivity.class));
        finish();
    }

/*
    @Override
    public void saludoEnActividad(int pos) {
        LocalesDP local = listaLocales.get(pos);
        Toast.makeText(this, "Local: "+listaLocales.get(pos).toString(), Toast.LENGTH_LONG).show();
    }

 */
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Your desired class
                startActivity(new Intent(UserpointsActivity.this, InicioActivity.class));
                break;
        }
        return true;
    }

 */


    private void tostito(){
        handler = new Handler(Looper.getMainLooper(), this);
        lista = new ListaLocalesFragmentUser();
        lista.setArray(objList);
        FragmentManager manager = getSupportFragmentManager();
        Fragment f = manager.findFragmentByTag("Fragmento");
        FragmentTransaction transaction =  manager.beginTransaction();
        if(lista==f)
            return;

        if(f!=null){
            transaction.remove(f);
        }
        transaction.add(R.id.contenedor, lista, "Fragmento");
        transaction.commit();
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        return false;
    }

    @Override
    public void saludoEnActividad(int pos) {
        local = new LocalesDetailActivity();
        Intent i = new Intent(this, LocalesDetailActivity.class);
        LocalesDP local = objList.get(pos);
        i.putExtra("uid", local.getuID());
        i.putExtra("nombre", local.getNombre());
        i.putExtra("tipo", local.getTipo());
        i.putExtra("ciudad", local.getCiudad());
        i.putExtra("calif", local.getCalif());
        i.putExtra("disca", local.getDisca());
        i.putExtra("ubi", local.getUbi());
        startActivity(i);
    }
}