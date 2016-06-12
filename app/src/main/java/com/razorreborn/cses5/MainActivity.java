package com.razorreborn.cses5;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Kiran Anto aka RazorSharp on 1/26/2016.
 * For more Info Contact
 * Kirananto@gmail.com
 * 9495333724
 * All Copyrights Reserved 2016
 *
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView mRecyclerView;
    private maincardAdapter mAdapter;
    private CardView Emptyview;
    private ProgressDialog pDialog;
    private final Context context = this;
    private final List<maincontent> notiflist = new ArrayList<>();
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_DATE = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.Notifications);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        setupCollapsingToolbar();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        Emptyview = (CardView) findViewById(R.id.card_view_sub);
        Global.orientation = getResources().getConfiguration().orientation;
        switch (Global.orientation)
        {
            case Configuration.ORIENTATION_UNDEFINED: Global.Orientation = "Undefined"; break;
            case Configuration.ORIENTATION_LANDSCAPE: Global.Orientation = "Landscape"; break;
            case Configuration.ORIENTATION_PORTRAIT:  Global.Orientation = "Portrait"; break;
            default: Global.Orientation = "Square";break;
        }
        StaggeredGridLayoutManager mSGLM;
        if(Global.Orientation.equals("Portrait")) {

            mSGLM = new StaggeredGridLayoutManager(1,1);
        } else {

            mSGLM = new StaggeredGridLayoutManager(2,1);
        }
        mSGLM.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView)findViewById(R.id.list);
        mRecyclerView.setLayoutManager(mSGLM);
        mRecyclerView.setHasFixedSize(true);
        SwipeRefreshLayout swiperefresh = new SwipeRefreshLayout(getApplicationContext());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new maincardAdapter(notiflist,getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        jsonFetch();
        swiperefresh.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               jsonFetch();
            }
        });
        swiperefresh.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,R.color.cardview_dark_background);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest.Builder request = new AdRequest.Builder();
        request.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        mAdView.loadAd(request.build());
    }

    private void setupCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);

        collapsingToolbar.setTitleEnabled(true);
    }

    private void jsonFetch() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setTitle(getString(R.string.loading));
            pDialog.setMessage(getString(R.string.loading));
            pDialog.setCancelable(false);
            pDialog.show();
            String NOTIF = getString(R.string.notif_link);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(new CustomJsonRequestMain(NOTIF, null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        hidePDialog();
                                        // fetching json to shared preferences;
                                        SharedPreferences sharedPreferences= context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor= sharedPreferences.edit();
                                        editor.putString("Notifications",response.toString());
                                        editor.apply();
                                        jsonparse();

                                    }
                                }

                                , new Response.ErrorListener()

                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d("Error: " + error.getMessage());
                                hidePDialog();
                                jsonparse();
                            }
                        }

                        )

                        {


                            @Override
                            protected Response<JSONArray> parseNetworkResponse(
                                    NetworkResponse response) {
                                try {
                                    String jsonString = new String(response.data,
                                            HttpHeaderParser
                                                    .parseCharset(response.headers));
                                    return Response.success(new JSONArray(jsonString),
                                            HttpHeaderParser
                                                    .parseCacheHeaders(response));
                                } catch (UnsupportedEncodingException e) {
                                    return Response.error(new ParseError(e));
                                } catch (JSONException je) {
                                    return Response.error(new ParseError(je));
                                }
                            }
                        }

                    ).getCacheEntry();
        }
    private void jsonparse()
    {
        JSONArray notifications=new JSONArray();
        SharedPreferences sharedPreferences= context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        try {
            Object obj=sharedPreferences.getString("Notifications","0");
            notifications=new JSONArray(obj.toString());
        }catch(JSONException e)
        {
            Toast.makeText(context,"Please Connect to Internet and Try again..!!",Toast.LENGTH_LONG).show();
        e.printStackTrace();}
        try{

            for (int i=0;i<notifications.length();i++)
            {
                maincontent maincontent = new maincontent();
                JSONObject notif=notifications.getJSONObject(i);
                maincontent.setTitle(notif.getString(TAG_TITLE));
                maincontent.setContent(notif.getString(TAG_CONTENT));
                maincontent.setDate(notif.getString(TAG_DATE));
                notiflist.add(maincontent);
            }
            if(notiflist.isEmpty()) {
                Log.i("LOG :", "EMPTY RECYCLERVIEW");
                mRecyclerView.setVisibility(View.GONE);
                Emptyview.setVisibility(View.VISIBLE);
            }
            else {
                mRecyclerView.setVisibility(View.VISIBLE);
                Emptyview.setVisibility(View.GONE);
            }
            mAdapter = new maincardAdapter(notiflist, getApplicationContext());
            mRecyclerView.setAdapter(mAdapter);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
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
            Intent subject = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(subject);
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
        }else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.AppShare));
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.nav_send) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Global.AppShare);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.nav_mail) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","KIRAN ANTO", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "REGARDING CONTENT OF CSE BETA App");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } else if (id == R.id.nav_parents){
            Intent parents = new Intent(getApplicationContext(), ParentsActivity.class);
            startActivity(parents);
        } else if (id == R.id.nav_setting){

            Intent subject = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(subject);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
