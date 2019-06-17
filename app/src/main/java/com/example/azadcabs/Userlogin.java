package com.example.azadcabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import static android.content.Context.MODE_PRIVATE;

public class Userlogin extends Fragment {

    Button login,reg;
    EditText mobile,pass;
    String url="";
    String MobilePattern = "[6-9]{1}+[0-9]{9}";
    String passpattern="(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.loginxml,container,false);
        mobile=view.findViewById(R.id.loginphone);
        pass=view.findViewById(R.id.loginpass);
        login=view.findViewById(R.id.loginbtn);
        reg=view.findViewById(R.id.loginregbtn);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft=getFragmentManager().beginTransaction();
                UserRegis r=new UserRegis();
                ft.replace(R.id.framelogin,r);
                ft.commit();
                /*Intent intent=new Intent(getContext(),Profile_Act.class);
                startActivity(intent);*/
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jsonObject = new JSONObject();
                if (mobile.getText().toString().equals("") || pass.getText().toString().equals("")) {
                    Snackbar.make(v, "Fields can't be Empty", Snackbar.LENGTH_LONG).show();
                } else {
                    if ((mobile.getText().toString().matches(MobilePattern))&& pass.getText().toString().matches(passpattern)) {
                        try {
                            jsonObject.put("mobile", mobile.getText().toString().trim());
                            jsonObject.put("pass", pass.getText().toString().trim());
                            jsonObject.put("action", "login_user");
                            Login_class lu = new Login_class();
                            lu.execute(jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (!mobile.getText().toString().matches(MobilePattern)) {
                            mobile.setError("Enter Valid Mobile No");
                            mobile.requestFocus();
                        }
                        else if(!pass.getText().toString().matches(passpattern)){
                            pass.setError("Valid Password (UpperCase, LowerCase, Number/SpecialChar and min 8 Chars)");
                            pass.requestFocus();
                        }
                    }
                }
            }
        });
        return view;
    }
    private class Login_class extends AsyncTask<String,String,String> {
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
            JSONObject j1=JsonFunction.GettingData(url,param[0]);
            return j1.toString();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            try {
                JSONObject object= new JSONObject(s);
                String res=object.getString("response");

                if (res.equalsIgnoreCase("Success"))
                {
                    String res1=object.getString("name");
                    String res3=object.getString("email");
                    String res4=object.getString("mobile");
                    String res5=object.getString("pass");
                    SharedPreferences preferences= getActivity().getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("Name",res1);
                    editor.putString("Email",res3);
                    editor.putString("Mobile",res4);
                    editor.putString("Pass",res5);
                    editor.apply();
                    Snackbar.make(Objects.requireNonNull(getView()),"Login Successful", Snackbar.LENGTH_SHORT).show();
                    Intent intent=new Intent(getActivity(),MainActivity2.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else if (res.equalsIgnoreCase("failed"))
                {
                    Snackbar.make(Objects.requireNonNull(getView()),"Email or Password Incorrect",
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

