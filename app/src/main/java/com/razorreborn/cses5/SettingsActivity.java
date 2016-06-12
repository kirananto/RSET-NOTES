package com.razorreborn.cses5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 *
 * Created by Kiran Anto aka RazorSharp on 2/22/2016.
 * For more Info Contact
 * Kirananto@gmail.com
 * 9495333724
 * All Copyrights Reserved 2016
 *
 */
public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button chkupdate = (Button) findViewById(R.id.Check_update);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","KIRAN ANTO", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "REGARDING CONTENT OF CSE BETA App");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));


            }
        });
        chkupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkforupdate();

            }
        });
        setupCollapsingToolbar();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void checkforupdate(){
        Toast.makeText(this," Update Available ",Toast.LENGTH_SHORT).show();
    }
    private void setupCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);

        collapsingToolbar.setTitleEnabled(true);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_maths) {
            Global.subject = "Engineering Maths";
            Global.subjectCode = "maths";
            Intent subject = new Intent(getApplicationContext(), OopActivity.class);
            startActivity(subject);
            finish();
        } else if (id == R.id.nav_pom) {
            Global.subject = "Principles of Management";
            Global.subjectCode = "pom";
            Intent subject = new Intent(getApplicationContext(), OopActivity.class);
            startActivity(subject);
            finish();
        } else if (id == R.id.nav_amp) {
            Global.subject = "Advanced Microprocessors and peripherals";
            Global.subjectCode = "amp";
            Intent subject = new Intent(getApplicationContext(), OopActivity.class);
            startActivity(subject);
            finish();
        } else if (id == R.id.nav_dms) {
            Global.subject = "Database Management Systems";
            Global.subjectCode = "dms";
            Intent subject = new Intent(getApplicationContext(), OopActivity.class);
            startActivity(subject);
            finish();
        } else if (id == R.id.nav_dsp) {
            Global.subject = "MicroProcessor";
            Global.subjectCode = "mps";
            Intent subject = new Intent(getApplicationContext(), OopActivity.class);
            startActivity(subject);
            finish();
        } else if (id == R.id.nav_os) {
            Global.subject = "Signals and Communication";
            Global.subjectCode = "scs";
            Intent subject = new Intent(getApplicationContext(), OopActivity.class);
            startActivity(subject);
            finish();
        } else if (id == R.id.nav_notif) {
            Intent notif = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(notif);
            finish();
        } else if (id == R.id.nav_databaselab) {
            Global.subject = "DSA LAb";
            Global.subjectCode = "dsalab";
            Intent subject = new Intent(getApplicationContext(), OopActivity.class);
            startActivity(subject);
            finish();
        } else if (id == R.id.nav_mplab) {
            Global.subject = "SCS LAB";
            Global.subjectCode = "scslab";
            Intent subject = new Intent(getApplicationContext(), OopActivity.class);
            startActivity(subject);
            finish();
        }else if (id == R.id.nav_exam) {
            Global.subject = "Question Papers";
            Global.subjectCode = "qp";
            Intent subject = new Intent(getApplicationContext(), OopActivity.class);
            startActivity(subject);
            finish();
        }else if (id == R.id.nav_misc) {
            Global.subject = "Miscellaneous";
            Global.subjectCode = "misc";
            Intent subject = new Intent(getApplicationContext(), OopActivity.class);
            startActivity(subject);
            finish();
        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.AppShare));
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.nav_send) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, Global.AppShare);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.nav_mail) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","KIRAN ANTO", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "REGARDING CONTENT OF CSE BETA App");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } else if (id == R.id.nav_setting){

            Intent subject = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(subject);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
