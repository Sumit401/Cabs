package com.example.azadcabs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecycleAdp extends RecyclerView.Adapter<RecycleAdp.MyViewHolder> {
    private Context context1;
    private ArrayList<String> vehicle;
    private ArrayList<String> city;
    private ArrayList<String> type;
    private ArrayList<String> desc;
    public RecycleAdp(Context context, ArrayList<String> dash_vehicle, ArrayList<String> dash_city, ArrayList<String> dash_against, ArrayList<String> dash_desc) {
        context1=context;
        city=dash_city;
        type=dash_against;
        desc=dash_desc;
        vehicle=dash_vehicle;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v1=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyc,viewGroup,false);
        return new MyViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.rv_vehicle.setText(vehicle.get(i));
        myViewHolder.rv_city.setText(city.get(i));
        myViewHolder.rv_type.setText(type.get(i));
        myViewHolder.rv_desc.setText(desc.get(i));
    }

    @Override
    public int getItemCount() {
        return city.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rv_city,rv_type,rv_desc,rv_vehicle;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_vehicle=itemView.findViewById(R.id.veh_reg);
            rv_city=itemView.findViewById(R.id.city);
            rv_type=itemView.findViewById(R.id.type);
            rv_desc=itemView.findViewById(R.id.desc);
            linearLayout=itemView.findViewById(R.id.recLinearlayout);
        }
    }
}