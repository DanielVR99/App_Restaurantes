package com.example.proyectofinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReseAdapter extends RecyclerView.Adapter<ReseAdapter.ResenasViewHolder> {

    public class ResenasViewHolder extends RecyclerView.ViewHolder{

        public TextView calif, rese;

        public ResenasViewHolder(@NonNull View itemView){
            super(itemView);

            calif = itemView.findViewById(R.id.califReseTV);
            rese = itemView.findViewById(R.id.resenaTV);
        }
    }

    private ArrayList<ResenasDP> locales;

    public ReseAdapter(ArrayList<ResenasDP> perritos){
        this.locales = perritos;
    }

    @NonNull
    @Override
    public ResenasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.rowrese, parent,false);

        ResenasViewHolder avh = new ResenasViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull ResenasViewHolder holder, int position) {
        ResenasDP str = locales.get(position);
        holder.calif.setText(str.getCalif());
        holder.rese.setText(str.getText());
    }

    @Override
    public int getItemCount() {
        return locales.size();
    }
}
