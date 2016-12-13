package com.treble.www.treble;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SONG_API_URL = "https://treble-mobile.herokuapp.com";
    public static final String SEARCH_API_URL = "https://treble-mobile.herokuapp.com/searchsong";
    public static final String ADD_API_URL = "https://treble-mobile.herokuapp.com/addsong";
    public static final String UPVOTE_API_URL = "https://treble-mobile.herokuapp.com/upvote";
    public static final String DOWNVOTE_API_URL = "https://treble-mobile.herokuapp.com/downvote";

    private LocationManager locationManager;
    private LocationListener locationListener;

    static final int MY_PERMISSION_REQUEST = 0;
    public double lat;
    public double lng;

    boolean initiateFeed = false;
    public boolean loaded = false;

//    TODO: improvements to make to the app
//    Implement android device id/token
//    Layout stuff: top bar on adding a song, relative layout for the feed
//    When you add a song, go back to the feed (? how?)
//    Persistent upvoting and downvoting (doesn't get removed after reloading the feed -> involves device id)
//    Comments (if we're adventurous)
//    A "hot" feed, based on votes rather than date submitted. (Easy to do on backend)


    @SuppressWarnings("WeakerAccess")
    static protected ListView feedView; // add static protected ? currently an error

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (savedInstanceState != null) {
            initiateFeed = savedInstanceState.getBoolean("toParse");
        }
    }

    protected void onResume() {
        super.onResume();
        boolean hasPermission = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasPermission = (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED) &&
                            (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED);
        }
        if(initiateFeed && hasPermission) {
            setContentView(R.layout.activity_main);
            final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location location;
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location!=null){
                lat = location.getLatitude();
                lng = location.getLongitude();
            }

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lng = location.getLongitude();
                    lat = location.getLatitude();
                    if (!loaded) {
                        parseFeed();
                        loaded = true;
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String s) {
                }

                @Override
                public void onProviderDisabled(String s) {
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(i, 1);
                }
            };

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(view.getContext(), AddSong.class);
                    myIntent.putExtra("lat", lat);
                    myIntent.putExtra("lng", lng);
                    startActivityForResult(myIntent, 1);
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            feedView = (ListView) findViewById(R.id.feedView);
            parseFeed();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},
                            MainActivity.MY_PERMISSION_REQUEST);
                }
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(requestCode == MY_PERMISSION_REQUEST) {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initiateFeed = true;
                } else {
                }
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
        if (id == R.id.action_about) {
            Toast.makeText(this, R.string.about_text, Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_refresh) {
            parseFeed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nearme) {
            parseFeed();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void parseFeed() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        new GetFeed(getApplicationContext(), lat, lng).execute();
    }

    @Override
    public void onActivityResult (int requestCode,int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            parseFeed();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("toParse", initiateFeed);
    }
}


