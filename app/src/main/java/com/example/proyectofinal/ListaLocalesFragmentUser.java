package com.example.proyectofinal;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

public class ListaLocalesFragmentUser extends Fragment implements View.OnClickListener {

    private Callback observador;
    private ArrayList<LocalesDP> listaLocales;
    private LocalesDP local;
    private RecyclerView recyclerView;
    private PositionHolder h;
    private Integer selectedPosition;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setArray(ArrayList<LocalesDP> n){
        listaLocales = n;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_locales, container, false);

        recyclerView = v.findViewById(R.id.recy);

        selectedPosition = 0;

        h = new PositionHolder(selectedPosition);

        LocalesUserAdapter adapter =new LocalesUserAdapter(listaLocales, this, h, getContext());
        LinearLayoutManager llm=new LinearLayoutManager(v.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ListaLocalesFragmentUser.Callback){
            observador = (ListaLocalesFragmentUser.Callback)context;
        } else {
            throw new RuntimeException("FALTA IMPLEMENTAR LA INTERFAZ CALLBACK EN ACTIVIDAD QUE ANEXA FRAGMENTO");
        }
    }
    public interface Callback{
        public void saludoEnActividad(int pos);
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        observador.saludoEnActividad(pos);
    }


}