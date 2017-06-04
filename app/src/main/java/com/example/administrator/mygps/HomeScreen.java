package com.example.administrator.mygps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.mygps.Fragments.MapsActivity;
import com.example.administrator.mygps.Fragments.Roads;
import com.example.administrator.mygps.Type.User;
import com.example.administrator.mygps.Utility.CircleTransform;
import com.example.administrator.mygps.Utility.UtilFirebase;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.Picasso;

import static android.support.design.R.attr.headerLayout;
import static java.security.AccessController.getContext;

public class HomeScreen extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);

        if (!User.getUserInstance().isGuest())
        {
            Toast.makeText(this,"u r logdin",Toast.LENGTH_LONG).show();
            ImageView imageView = (ImageView)headerLayout.findViewById(R.id.userImage);
            TextView userNme = (TextView)headerLayout.findViewById(R.id.userName);
            TextView userEmail = (TextView)headerLayout.findViewById(R.id.email);
            Picasso.with(this).load(Uri.parse(User.getUserInstance().getPhotoUrl()==null?"":User.getUserInstance().getPhotoUrl())).transform(new CircleTransform()).into(imageView);
            userNme.setText(User.getUserInstance().getDisplayName());
            userEmail.setText(User.getUserInstance().getEmail() );
        }else
        {
            ImageView imageView = (ImageView)headerLayout.findViewById(R.id.userImage);
            TextView userNme = (TextView)headerLayout.findViewById(R.id.userName);
            TextView userEmail = (TextView)headerLayout.findViewById(R.id.email);
            userNme.setText("guest");
            userEmail.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            User.getUserInstance().logOut();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (id == R.id.my_rode) {
            fragmentTransaction.replace(R.id.container_body, new Roads());
            fragmentTransaction.commit();
        } else if (id == R.id.new_road) {
            fragmentTransaction.replace(R.id.container_body, new MapsActivity());
            fragmentTransaction.commit();
        } else if (id == R.id.sher_rode) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.log_out) {
            LogIn.logOut(this);
            Intent intent = new Intent(this,WelcomeScreen.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
