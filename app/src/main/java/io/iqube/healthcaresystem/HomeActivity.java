package io.iqube.healthcaresystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MedicineAddFragment.OnFragmentInteractionListener,
        ReminderFragment.OnFragmentInteractionListener{
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

            Fragment f = MedicineAddFragment.newInstance("", "");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getExtras()!=null){
            Fragment f = ReminderFragment.newInstance("","");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,f).commit();
        }else{
            Fragment f = MedicineAddFragment.newInstance("","");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,f).commit();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.pill_box:
                Fragment f = MedicineAddFragment.newInstance("","");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,f).commit();
                break;
            case R.id.reminder:

                f = ReminderFragment.newInstance("","");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,f).commit();
                break;
            case R.id.medical_profile:
                f = MedicineAddFragment.newInstance("","");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,f).commit();
                break;
            case R.id.calendar:
                startActivity(new Intent(HomeActivity.this,CalendarActivity.class));
                break;
            case R.id.profile:
                f = MedicineAddFragment.newInstance("","");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,f).commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
