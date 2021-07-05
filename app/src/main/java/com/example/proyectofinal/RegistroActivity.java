package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference ref;
    private EditText correo, psw1, psw2, uname;
    private Spinner cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        auth = FirebaseAuth.getInstance();
        cities = findViewById(R.id.spinnerCity);
        correo = findViewById(R.id.correoTV);
        psw1 = findViewById(R.id.pass1);
        psw2 = findViewById(R.id.pass2);
        uname = findViewById(R.id.unameTV);
        String[] citiesStr = new String[] {"CDMX", "Monterrey", "Guadalajara", "Toluca"};
        ArrayAdapter<String> adapterCity = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, citiesStr);
        cities.setAdapter(adapterCity);
    }

    public void registrarseBien(View view){
        String cityStr = cities.getSelectedItem().toString();
        String correoStr = correo.getText().toString();
        String pass1 = psw1.getText().toString();
        String pass2 = psw2.getText().toString();
        String unameStr = uname.getText().toString();

        if(correoStr.isEmpty()||pass1.isEmpty()||pass2.isEmpty()||unameStr.isEmpty()){
            Toast.makeText(this, "Registro fallido, algún campo vacío", Toast.LENGTH_SHORT).show();
        }else if(!pass1.equals(pass2)){
            Toast.makeText(this, "Registro fallido, las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }else{
            auth.createUserWithEmailAndPassword(correoStr, pass1)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //Si se pudo hacer registro
                                Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                registrarUsuarioDB(correoStr,pass1,unameStr,cityStr);

                            }else{
                                //No se registro
                                Toast.makeText(RegistroActivity.this,
                                        "Registro fallido: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void registrarUsuarioDB(String correo, String psw,String unameString, String cityString){
        auth.signInWithEmailAndPassword(correo, psw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegistroActivity.this, "LogIn exitoso", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(RegistroActivity.this,
                                    "LogIn fallido: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(unameString).build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.wtf("TAG", "User profile updated.");
                        } else{
                            Log.wtf("TAG", "No se actualizo");
                        }
                    }
                });

        user.sendEmailVerification();
        FirebaseDatabase db =FirebaseDatabase.getInstance();
        ref = db.getReference();
        String key = ref.child("users").push().getKey();
        ref.child("users").child(key).child("city").setValue(cityString);
        ref.child("users").child(key).child("uname").setValue(unameString);
        ref.child("users").child(key).child("uID").setValue(user.getUid());
        Intent i = new Intent(this, InicioActivity.class);
        startActivity(i);
    }

}