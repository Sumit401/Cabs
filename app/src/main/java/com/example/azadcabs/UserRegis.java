package com.example.azadcabs;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class UserRegis extends Fragment {

    Button log, reg;
    EditText name, email, phone, pass, cnfpass;
    String MobilePattern = "[6-9]{1}+[0-9]{9}";
    String passpattern="(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String namepattern="[a-zA-Z ]+";
    String url="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.registerxml,container,false);

        log=view.findViewById(R.id.Regloginbtn);
        reg=view.findViewById(R.id.Regbtn);
        name = view.findViewById(R.id.Regname);
        email = view.findViewById(R.id.Regemail);
        phone = view.findViewById(R.id.Regphone);
        pass = view.findViewById(R.id.Regpass);
        cnfpass = view.findViewById(R.id.Regcnfpass);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Userlogin login_user = new Userlogin();
                ft.replace(R.id.framelogin, login_user);
                ft.commit();
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();

                if (name.getText().toString().equals("") || phone.getText().toString().equals("") || pass.getText().toString().equals("") || cnfpass.getText().toString().equals("")) {
                    Snackbar.make(v, "Fields can't be Empty", Snackbar.LENGTH_LONG).show();
                } else {

                    if(email.getText().toString().matches(emailPattern)||( phone.getText().toString().matches(MobilePattern) && pass.getText().toString().matches(passpattern) && name.getText().toString().matches(namepattern))){

                        if (pass.getText().toString().equals(cnfpass.getText().toString())) {
                            try {
                                jsonObject.put("name", name.getText().toString().trim());
                                jsonObject.put("mobile", phone.getText().toString().trim());
                                jsonObject.put("email", email.getText().toString().trim());
                                jsonObject.put("pass", pass.getText().toString().trim());
                                jsonObject.put("action", "register_user");
                                Regi loginUser = new Regi();
                                loginUser.execute(jsonObject.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            Snackbar.make(v, "Check Password do match", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        if(!name.getText().toString().matches(namepattern)) {
                            //Snackbar.make(v, "Enter Valid Name",Snackbar.LENGTH_SHORT).show();
                            name.setError("Enter Valid Name");
                            name.requestFocus();
                        }
                        else if(!email.getText().toString().matches(emailPattern)){
                            //Snackbar.make(v, "Enter Valid Email", Snackbar.LENGTH_SHORT).show();
                            email.setError("Enter Valid Email");
                            email.requestFocus();
                        }
                        else if(!phone.getText().toString().matches(MobilePattern)) {
                            // Snackbar.make(v, "Enter Valid Mobile Number",Snackbar.LENGTH_SHORT).show();
                            phone.setError("Enter Valid Contact No.");
                            phone.requestFocus();
                        }
                        else if (!pass.getText().toString().matches(passpattern)){
                            pass.setError("Valid Password (UpperCase, LowerCase, Number/SpecialChar and min 8 Chars");
                            pass.requestFocus();
                        }
                    }
                }
            }
        });

        return view;
    }
    private class Regi extends AsyncTask<String,String,String> {

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
        protected String doInBackground(String... param) {
            JSONObject object=JsonFunction.GettingData(url,param[0]);
            return object.toString();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            JSONObject object;
            try {
                object = new JSONObject(s);
                String res=object.getString("response");
                if (res.equalsIgnoreCase("Success")) {

                    Snackbar.make(Objects.requireNonNull(getView()),"Successfully Registered",
                            Snackbar.LENGTH_SHORT).show();
                    FragmentTransaction ft=getFragmentManager().beginTransaction();
                    Userlogin profile_activity= new Userlogin();
                    ft.replace(R.id.framelogin,profile_activity);
                    ft.commit();
                }
                else if (res.equalsIgnoreCase("Failed")) {
                    Snackbar.make(Objects.requireNonNull(getView()),"Failed! Already Registered",
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
    }
}

