package com.example.azadcabs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;
import static android.content.Context.MODE_PRIVATE;

public class Prof_home extends Fragment {

    String url="";
    String p_name,p_email,p_phone;
    String  s,t;
    String city[]={"Select City of Complain","Chandigarh Tricity","Ludhiana","Amritsar","Jalandhar"};
    String cabs[]={"Register Complain Against","Cab Operators","Traffic Police","Passenger"};
    Spinner comp_against,comp_city;
    Button comp_btn;
    EditText name,email,phone,vehicle_reg,complain_text;
    String vr;
    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.profhomexml,container,false);

        comp_btn=view.findViewById(R.id.comp_reg_btn);
        comp_city=view.findViewById(R.id.comp_city);
        comp_against=view.findViewById(R.id.comp_against);
        name=view.findViewById(R.id.comp_name);
        email=view.findViewById(R.id.comp_email);
        phone=view.findViewById(R.id.comp_phone);
        vehicle_reg=view.findViewById(R.id.comp_vehicle_regis);
        complain_text=view.findViewById(R.id.comp_desc);

        complain_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.comp_desc) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        ArrayAdapter arrayAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,cabs);
        comp_against.setAdapter(arrayAdapter);
        comp_against.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==1){
                    s="Cabs Operators";
                }else if (position==2){
                    s="Traffic Police";
                }else if (position==3){
                    s="Passangers";
                }else {
                   s=null;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Snackbar.make(view,"Field can't be empty",Snackbar.LENGTH_SHORT);
            }
        });

        ArrayAdapter arrayAdapter2=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,city);
        comp_city.setAdapter(arrayAdapter2);


        comp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    t = "Chandigarh Tricity";
                } else if (position == 2) {
                    t = "Ludhiana";
                } else if (position == 3) {
                    t = "Amritsar";
                } else if (position == 4) {
                    t = "Jalandhar";
                }else {
                   t=null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Snackbar.make(view,"Field can't be Empty",Snackbar.LENGTH_SHORT);
            }});

        SharedPreferences preferences= getActivity().getSharedPreferences("Login", MODE_PRIVATE);
        p_name=preferences.getString("Name",null);
        p_email=preferences.getString("Email",null);
        p_phone=preferences.getString("Mobile",null);
        name.setText(p_name, TextView.BufferType.EDITABLE);
        email.setText(p_email,TextView.BufferType.EDITABLE);
        phone.setText(p_phone,TextView.BufferType.EDITABLE);

        comp_btn.setOnClickListener(new View.OnClickListener() {
            JSONObject jsonObject=new JSONObject();
            @Override
            public void onClick(View v) {
                if (vehicle_reg.getText().toString()==null || t==null || s==null || complain_text.getText().toString()==null ){
                    Snackbar.make(view,"Fields Can't be Empty",Snackbar.LENGTH_SHORT);
                }else {
                    try {
                        jsonObject.put("name",p_name );
                        jsonObject.put("email",p_email);
                        jsonObject.put("mobile", p_phone);
                        jsonObject.put("registration", vehicle_reg.getText().toString().trim());
                        jsonObject.put("complainagainst",s);
                        jsonObject.put("citycomplain",t);
                        jsonObject.put("complain",complain_text.getText().toString().trim());
                        Submit_Data sd = new Submit_Data();
                        sd.execute(jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
        return view;
    }

    private class Submit_Data extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            try {
                JSONObject j1=new JSONObject(s.toString());
                String res=j1.getString("response");
                if (res.equalsIgnoreCase("Success")){
                    Snackbar.make(Objects.requireNonNull(getView()),"Complaint Registered Successfully",
                            Snackbar.LENGTH_SHORT).show();
                    FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                    Prof_Dashboard prof_dashboard=new Prof_Dashboard();
                    fragmentTransaction.replace(R.id.frameprofile,prof_dashboard);
                    fragmentTransaction.commit();
                }
                else if(res.equalsIgnoreCase("Failed")) {
                    Snackbar.make(Objects.requireNonNull(getView()),"Failed",
                            Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(Objects.requireNonNull(getView()),"Error",
                            Snackbar.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... param) {
            JSONObject object=JsonFunction.GettingData(url,param[0]);
            return object.toString();
        }
    }
}

