package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference ref;

    private EditText nombre, rese;
    private RatingBar calif;
    private Spinner dropdownCity, dropdownCategory;
    private CheckBox chBox;
    private String idStr;
    private ImageButton ubiB;
    private TextView ubiTV;
    private double latitudMap=0, longitudMap=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser  user = FirebaseAuth.getInstance().getCurrentUser();
        ref = db.getReference();


        Intent desintent = getIntent();
        idStr = desintent.getStringExtra("id");
        Log.wtf("idlol", idStr);

        dropdownCity = findViewById(R.id.spinnerCiudad);
        nombre = findViewById(R.id.ETnombre);
        dropdownCategory = findViewById(R.id.spinnerTipo);
        chBox = findViewById(R.id.checkBox);
        calif = findViewById(R.id.ratingBar);
        rese = findViewById(R.id.resET);
        ubiB = findViewById(R.id.imageButton4);
        ubiTV = findViewById(R.id.textView8);

        // no se editan comentarios
        ViewGroup layout = (ViewGroup) rese.getParent();
        layout.removeView(rese);
        //No se edita ubi
        layout = (ViewGroup) ubiB.getParent();
        layout.removeView(ubiB);
        layout = (ViewGroup) ubiTV.getParent();
        layout.removeView(ubiTV);
        //No se puede editar la calificacion  ya que es info de la comunidad
        calif.setIsIndicator(true);

        String isDisca="no";

        String[] itemsName = new String[] {"-Selecciona lugar-", "CDMX", "Monterrey", "Guadalajara", "Toluca"};
        String[] itemsCategory = new String[] {"-Selecciona categoría-","Cafeterías", "Bares", "Antros", "Desayunos", "Restaurantes","Comida"};
        ArrayAdapter<String> adapterName = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsName);
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsCategory);
        dropdownCity.setAdapter(adapterName);
        dropdownCategory.setAdapter(adapterCategory);

        auth = FirebaseAuth.getInstance();

        ref.child("lugares").orderByChild("id").equalTo(idStr).addChildEventListener(new ChildEventListener() {

            public void onChildAdded (DataSnapshot dataSnapshot, String prevChildKey) {
                Float num = Float.parseFloat(dataSnapshot.child("calif").getValue(String.class)) / 2;
                calif.setRating(num);
                nombre.setText(dataSnapshot.child("nombre").getValue(String.class));

                for (int i = 0; i < dropdownCity.getCount(); i++) {
                    if (dropdownCity.getItemAtPosition(i).equals(dataSnapshot.child("ciudad").getValue(String.class))) {
                        dropdownCity.setSelection(i);
                        break;
                    }
                }

                for (int i = 0; i < dropdownCategory.getCount(); i++) {
                    if (dropdownCategory.getItemAtPosition(i).equals(dataSnapshot.child("tipo").getValue(String.class))) {
                        dropdownCategory.setSelection(i);
                         break;
                    }
                }

                if (dataSnapshot.child("disca").getValue(String.class).equals("si"))
                    chBox.setChecked(true);

            }
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}


            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName){}

            public void onCancelled(@NonNull DatabaseError error) {
                Log.wtf("TAG","murio");
            }
        });

    }


    public void guardarLocal(View v){

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser  user = FirebaseAuth.getInstance().getCurrentUser();
        ref = db.getReference();

        String nomStr = nombre.getText().toString();
        String cityStr = dropdownCity.getSelectedItem().toString();
        String tipoStr = dropdownCategory.getSelectedItem().toString();
        String dis = "no";
        String caliStr = (calif.getRating()*2)+""; // se multiplica por 2 para que sea calif de 1 a 10


        if(chBox.isChecked()){
            dis = "si";
        }

        if(caliStr.isEmpty()||nomStr.trim().equals("") || cityStr.trim().equals("") || cityStr.trim().equals("-Selecciona lugar-") ||tipoStr.trim().equals("")||tipoStr.trim().equals("-Selecciona categoría-")){
            Toast.makeText(this, "Falta llenar algun campo",Toast.LENGTH_SHORT).show();
        }
        else{
            ref.child("lugares").child(idStr).child("nombre").setValue(nomStr);
            //ref.child("lugares").child(idStr).child("calif").setValue(caliStr);
            ref.child("lugares").child(idStr).child("tipo").setValue(tipoStr);
            ref.child("lugares").child(idStr).child("ciudad").setValue(cityStr);
            ref.child("lugares").child(idStr).child("creador").setValue(user.getUid());
            ref.child("lugares").child(idStr).child("disca").setValue(dis);
            /*
            Actualizar reseña y ubicación para entrega final
            String resKey = ref.child("lugares").child(key).child("resena").push().getKey();
            ref.child("lugares").child(key).child("resena").child(resKey).setValue(rese.getText().toString());
            ref.child("lugares").child(key).child("ubi").setValue("coordenadas");
            */
            Toast.makeText(this, "Cambio guardado correctamente",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, UserpointsActivity.class);
            startActivity(i);
        }
       // finishActivity(1);
    }
}