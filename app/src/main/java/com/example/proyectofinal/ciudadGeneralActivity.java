package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Vector;

public class ciudadGeneralActivity extends AppCompatActivity {
    private DBHelper db;
    private TextView cityTV;
    private String cityStr;
    private TextView locales;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciudad_general);
        cityTV = findViewById(R.id.ciudad);
        Intent desintent = getIntent();
        cityStr = desintent.getStringExtra("city");
        String resultadoLocales = "";
        cityTV.setText(cityStr);
        db = new DBHelper(this);
        Vector<String> resultado = db.buscarLocales(cityStr);
        for(int i=0;i+1<=resultado.size();i++){
            resultadoLocales = resultadoLocales+resultado.get(i)+"\n";
        }
        locales = findViewById(R.id.localesTF);
        locales.setText(resultadoLocales);
    }

    public void refresh(View v){
        String resultadoLocales = "";
        Vector<String> resultado = db.buscarLocales(cityStr);
        for(int i=0;i+1<=resultado.size();i++){
            resultadoLocales = resultadoLocales+resultado.get(i)+"\n";
        }
        locales = findViewById(R.id.localesTF);
        locales.setText(resultadoLocales);
    }

    public void agregarLocal(View v){
        Intent i = new Intent(this, AgregarActivity.class);
        i.putExtra("city",cityStr);
        startActivity(i);
    }
}