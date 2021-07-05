package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.StringTokenizer;

public class ReseniaActivity extends AppCompatActivity {

    private RatingBar calif;
    private EditText rese;

    private FirebaseAuth auth;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resenia);

        calif = findViewById(R.id.ratingBar2);
        rese = findViewById(R.id.nuevaReseET);

        auth = FirebaseAuth.getInstance();
    }

    public void nuevaRese(View v){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ref = db.getReference();

        String resenia = rese.getText().toString();
        String caliStr = (calif.getRating()*2)+"";

        if(resenia.isEmpty()){
            Snackbar.make(v,"Tienes que llenar todos los campos",Snackbar.LENGTH_SHORT).show();
        }else{
            Intent desintent = getIntent();
            String lugerKey = desintent.getStringExtra("id");
            Double resCount = desintent.getDoubleExtra("count",0);
            String calif = desintent.getStringExtra("calif");
            Double newCali = ((Double.parseDouble(calif)*resCount)+(Double.parseDouble(caliStr)))/(resCount+1);
            String resKey = ref.child("lugares").child(lugerKey).child("resena").push().getKey();
            ref.child("lugares").child(lugerKey).child("resena").child(resKey).setValue(caliStr+"$"+rese.getText().toString());
            ref.child("lugares").child(lugerKey).child("calif").setValue(String.format("%.02f", newCali));

            Snackbar.make(v,"Reseña guardada",Snackbar.LENGTH_SHORT).show();

            setResult(Activity.RESULT_OK);
            //finishActivity(1);
            finish();
        }
    }
}