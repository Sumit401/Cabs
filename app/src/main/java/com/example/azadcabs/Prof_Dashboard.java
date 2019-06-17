package com.example.azadcabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Prof_Dashboard extends Fragment {
    String url="";
    ArrayList<String> dash_vehicle=new ArrayList<>();
    ArrayList<String> dash_city=new ArrayList<>();
    ArrayList<String> dash_against=new ArrayList<>();
    ArrayList<String> dash_desc=new ArrayList<>();
    TextView nodata;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.prof_dash_xml,container,false);
        recyclerView=view.findViewById(R.id.dash_recycler);
        nodata=view.findViewById(R.id.dash_nodata);

        JSONObject jsonObject=new JSONObject();
        try {
            SharedPreferences sharedPreferences=getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);

            String s_mobile=sharedPreferences.getString("Mobile",null);
            jsonObject.put("action","get_all_links");
            jsonObject.put("phone",s_mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LoadData loadData=new LoadData();
        loadData.execute(jsonObject.toString());

        return view;
    }

    private class LoadData extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            JSONObject object = JsonFunction.GettingData(url, param[0]);
            return object.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            JSONObject j1 = null;
            try {
                j1 = new JSONObject(s.toString());
                String res = j1.getString("response");
                if (res.equalsIgnoreCase("success")) {

                    JSONArray jsonArray = j1.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject j2 = jsonArray.getJSONObject(i);
                        String vehicle_regno=j2.getString("registration");
                        String city = j2.getString("complainagainst");
                        String type = j2.getString("citycomplain");
                        String comp = j2.getString("complain");
                        dash_vehicle.add(vehicle_regno);
                        dash_city.add(city);
                        dash_against.add(type);
                        dash_desc.add(comp);
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(new RecycleAdp(getContext(),dash_vehicle, dash_city, dash_against, dash_desc));
                } else {
                    nodata.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
