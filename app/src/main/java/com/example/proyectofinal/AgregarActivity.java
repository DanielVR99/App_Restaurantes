package com.example.proyectofinal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AgregarActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference ref;

    AlertDialog dialog;
    AlertDialog.Builder builder;

    private final static int ACTIVITY_MAPS = 0;

    private EditText nombre, rese;
    private RatingBar calif;
    private Spinner dropdownCity, dropdownCategory;
    private CheckBox chBox;
    private double latitudMap=0, longitudMap=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        Intent desintent = getIntent();
        String cityStr = desintent.getStringExtra("city");

        dropdownCity = findViewById(R.id.spinnerCiudad);
        nombre = findViewById(R.id.ETnombre);
        dropdownCategory = findViewById(R.id.spinnerTipo);
        chBox = findViewById(R.id.checkBox);
        calif = findViewById(R.id.ratingBar);
        rese = findViewById(R.id.resET);

        String[] itemsName = new String[] {"-Selecciona lugar-", "CDMX", "Monterrey", "Guadalajara", "Toluca"};
        String[] itemsCategory = new String[] {"-Selecciona categoría-","Cafeterías", "Bares", "Antros", "Desayunos", "Restaurantes","Comida"};
        ArrayAdapter<String> adapterName = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsName);
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsCategory);
        dropdownCity.setAdapter(adapterName);
        dropdownCategory.setAdapter(adapterCategory);

        auth = FirebaseAuth.getInstance();

    }


    public void guardarLocal(View v){

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser  user = FirebaseAuth.getInstance().getCurrentUser();
        ref = db.getReference();

        String nomStr = nombre.getText().toString();
        String cityStr = dropdownCity.getSelectedItem().toString();
        String tipoStr = dropdownCategory.getSelectedItem().toString();
        String dis = "no";
        String resenia = rese.getText().toString();
        String caliStr = (calif.getRating()*2)+""; // se multiplica por 2 para que sea calif de 1 a 10

        if(chBox.isChecked()){
            dis = "si";
        }


        if(latitudMap==0||longitudMap==0){
            Toast.makeText(this, "Falta seleccionar ubicacion",Toast.LENGTH_SHORT).show();
        }
        else if(resenia.trim().equals("")||caliStr.isEmpty()||nomStr.trim().equals("") || cityStr.trim().equals("") || cityStr.trim().equals("-Selecciona lugar-") ||tipoStr.trim().equals("")||tipoStr.trim().equals("-Selecciona categoría-")){
            Toast.makeText(this, "Falta llenar algun campo",Toast.LENGTH_SHORT).show();
        }
        else{
            String  key = ref.child("lugares").push().getKey();
            ref.child("lugares").child(key).child("nombre").setValue(nomStr);
            ref.child("lugares").child(key).child("calif").setValue(caliStr);
            ref.child("lugares").child(key).child("tipo").setValue(tipoStr);
            ref.child("lugares").child(key).child("ciudad").setValue(cityStr);
            ref.child("lugares").child(key).child("creador").setValue(user.getUid());
            ref.child("lugares").child(key).child("disca").setValue(dis);
            ref.child("lugares").child(key).child("id").setValue(key);
            String resKey = ref.child("lugares").child(key).child("resena").push().getKey();
            ref.child("lugares").child(key).child("resena").child(resKey).setValue(caliStr+"$"+rese.getText().toString());
            ref.child("lugares").child(key).child("ubi").setValue(latitudMap+"$"+longitudMap);
            Toast.makeText(this, "Guardado correctamente",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, UserpointsActivity.class);
            startActivity(i);
        }
        finishActivity(1);
    }

    public void guardaMap(View view){
        Intent i = new Intent(this, MapsAgregarActivity.class);
        startActivityForResult(i, ACTIVITY_MAPS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ACTIVITY_MAPS && resultCode == Activity.RESULT_OK && data != null){
            latitudMap = data.getDoubleExtra("lat",0);
            longitudMap = data.getDoubleExtra("lon",0);

            Toast.makeText(this, "saludos: " + latitudMap + " LON: " + longitudMap, Toast.LENGTH_SHORT).show();
        }
    }
}