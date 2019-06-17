package com.example.azadcabs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;
import static android.content.Context.MODE_PRIVATE;

public class Prof_Account extends Fragment {

    EditText name,mobile,email;
    String url="";
    String url2="";
    Button update,pswd_upd;
    String s_name,s_email,s_mobile,s_pass;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.profaccountxml,container,false);
        name=view.findViewById(R.id.account_name);
        email=view.findViewById(R.id.account_email);
        mobile=view.findViewById(R.id.account_phone);
        update=view.findViewById(R.id.account_update);
        pswd_upd=view.findViewById(R.id.password_update);
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        s_name=sharedPreferences.getString("Name",null);
        s_email=sharedPreferences.getString("Email",null);
        s_mobile=sharedPreferences.getString("Mobile",null);
        s_pass=sharedPreferences.getString("Pass",null);

        email.setText(s_email, TextView.BufferType.EDITABLE);
        mobile.setText(s_mobile, TextView.BufferType.EDITABLE);
        name.setText(s_name, TextView.BufferType.EDITABLE);


        pswd_upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.passupdxml);
                dialog.setCancelable(false);
                final EditText newpass,cnfpass;
                newpass=dialog.findViewById(R.id.newpass);
                cnfpass=dialog.findViewById(R.id.newpasupd);
                Button close=dialog.findViewById(R.id.close);
                Button upd=dialog.findViewById(R.id.updpssbtn);
                upd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (newpass.getText().toString()==null||cnfpass.getText().toString()==null){
                            Toast.makeText(getContext(),"Fields Can't be empty",Toast.LENGTH_SHORT).show();
                        }else {
                            if (newpass.getText().toString().equals(cnfpass.getText().toString())) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("mobile",s_mobile);
                                    jsonObject.put("oldpass",s_pass);
                                    jsonObject.put("newpass",newpass.getText().toString().trim());
                                    Update_Pass update_pass=new Update_Pass();
                                    update_pass.execute(jsonObject.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                Toast.makeText(getContext(),"Password don't Match",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                });
                dialog.show();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        update.setOnClickListener(new View.OnClickListener() {

            JSONObject jsonObject=new JSONObject();
            @Override
            public void onClick(View v) {
                try {
                    jsonObject.put("name",name.getText().toString() );
                    jsonObject.put("email",email.getText().toString());
                    jsonObject.put("mobile", mobile.getText().toString());
                    jsonObject.put("mobileupd", s_mobile);
                    Input_Data sd = new Input_Data();
                    sd.execute(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class Input_Data extends AsyncTask<String,String,String> {
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
                    Snackbar.make(Objects.requireNonNull(getView()),"Updated Successfully",
                            Snackbar.LENGTH_SHORT).show();
                    SharedPreferences preferences= getActivity().getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("Name",name.getText().toString());
                    editor.putString("Email",email.getText().toString());
                    editor.putString("Mobile",mobile.getText().toString());
                    editor.apply();
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
    private class Update_Pass extends AsyncTask<String,String,String>{
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading");
            progressDialog.setTitle("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            JSONObject j1=JsonFunction.GettingData(url2,param[0]);
            return j1.toString();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                String res = object.getString("response");
                if (res.equalsIgnoreCase("Success")) {
                    Toast.makeText(getContext(),"Password Updated",Toast.LENGTH_SHORT).show();
                }else if (res.equalsIgnoreCase("Failed")){
                    Toast.makeText(getContext(),"Password Not Updated",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}