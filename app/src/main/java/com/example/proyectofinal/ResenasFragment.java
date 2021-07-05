package com.example.proyectofinal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ResenasFragment extends Fragment {

    private ResenasDP resenasDP;
    private ArrayList<ResenasDP> reseLista;

    private RecyclerView recyclerView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setArray(ArrayList<ResenasDP> n){
        reseLista = n;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_resenas, container, false);

        recyclerView = v.findViewById(R.id.recyclerResenas);
        ReseAdapter adapter =new ReseAdapter(reseLista);
        LinearLayoutManager llm=new LinearLayoutManager(v.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        return v;
    }
}