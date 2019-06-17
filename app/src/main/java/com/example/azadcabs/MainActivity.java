package com.example.azadcabs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button link;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        UserRegis reg= new UserRegis();
        ft.replace(R.id.framelogin,reg);
        ft.commit();

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home1) {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            UserRegis reg= new UserRegis();
            ft.replace(R.id.framelogin,reg);
            ft.commit();
        } else if (id == R.id.login) {

            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            Userlogin reg= new Userlogin();
            ft.replace(R.id.framelogin,reg);
            ft.commit();
        }
        else if (id == R.id.nav_share) {
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
            final Dialog dialog=new Dialog(MainActivity.this);
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
}
