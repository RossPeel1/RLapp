package com.example.rlapp;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_Layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_View);
        View navView = navigationView.inflateHeaderView(R.layout.header);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });
    }

    private void UserMenuSelector(MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.nav_PoochProfile:
                Toast.makeText(getApplicationContext(), "Pooch profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_home:
                Toast.makeText(getApplicationContext(),"Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_Post:
                Toast.makeText(getApplicationContext(),"Post", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_friends:
                Toast.makeText(getApplicationContext(),"Friends", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_findFriend:
                Toast.makeText(getApplicationContext(),"Find Friends", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_messages:
                Toast.makeText(getApplicationContext(),"Messages", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                Toast.makeText(getApplicationContext(),"settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                Toast.makeText(getApplicationContext(),"Logout", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
