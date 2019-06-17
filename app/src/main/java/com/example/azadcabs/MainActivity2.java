package com.example.azadcabs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button link;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        Prof_home re=new Prof_home();
        ft.replace(R.id.frameprofile, re);
        ft.commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences settings = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
            settings.edit().clear().apply();
            final ProgressDialog progressDialog=new ProgressDialog(MainActivity2.this);
            progressDialog.setMessage("Wait");
            progressDialog.setTitle("Logging Out");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Intent intent=new Intent(MainActivity2.this,MainActivity.class);
                    startActivity(intent);
                }
            },1500);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home2) {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            Prof_home re=new Prof_home();
            ft.replace(R.id.frameprofile, re);
            ft.commit();

        } else if (id == R.id.dashboard) {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            Prof_Dashboard re=new Prof_Dashboard();
            ft.replace(R.id.frameprofile, re);
            ft.commit();
        } else if (id == R.id.account) {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            Prof_Account re=new Prof_Account();
            ft.replace(R.id.frameprofile, re);
            ft.commit();
        } else if (id == R.id.nav_share) {
            Intent i =new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_TEXT,"Hey there check out my latest Application at: www.google.co.in");
            i.setType("text/plain");
            startActivity(i);
        } else if (id == R.id.feedback) {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "xyz@gmail.com"));
                i.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                i.putExtra(Intent.EXTRA_TEXT, "Text");
                startActivity(i);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(this,""+e, Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.about) {
            final Dialog dialog=new Dialog(MainActivity2.this);
            dialog.setContentView(R.layout.aboutcontent);
            dialog.setCancelable(false);
            Button button=dialog.findViewById(R.id.okbutton_about);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home: {
                    FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                    Prof_home re=new Prof_home();
                    ft.replace(R.id.frameprofile, re);
                    ft.commit();
                    return true;
                }
                case R.id.navigation_dashboard: {
                    FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                    Prof_Dashboard re=new Prof_Dashboard();
                    ft.replace(R.id.frameprofile, re);
                    ft.commit();
                    return true;
                }
                case R.id.navigation_account: {
                    FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                    Prof_Account re=new Prof_Account();
                    ft.replace(R.id.frameprofile, re);
                    ft.commit();
                    return true;
                }
            }
            return false;
        }
    };
}
