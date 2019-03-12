package com.example.rlapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private Toolbar mainToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = (Toolbar) findViewById(R.id.main_page_Toolbar);                               // setup menu bar layout
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Home");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_Layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_Open, R.string.drawer_Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem menuItem) {                                              // allows the user to select a feature on the app which starts the corresponding activity.
        switch (menuItem.getItemId()){
            case R.id.nav_PoochProfile:
                Toast.makeText(getApplicationContext(), "Pooch profile", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Pooch_Profile.class);
                startActivity(intent);
                break;
            case R.id.nav_home:
                Toast.makeText(getApplicationContext(),"Home", Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(this, Home.class );
                startActivity(homeIntent);
                break;
            case R.id.nav_Post:
                Toast.makeText(getApplicationContext(),"Post", Toast.LENGTH_SHORT).show();
                Intent postIntent = new Intent(this, Post.class);
                startActivity(postIntent);
                break;
            case R.id.nav_friends:
                Toast.makeText(getApplicationContext(),"Friends", Toast.LENGTH_SHORT).show();
                Intent fiendsIntent = new Intent(this, Friends.class);
                startActivity(fiendsIntent);
                break;
            case R.id.nav_findFriend:
                Toast.makeText(getApplicationContext(),"Find Friends", Toast.LENGTH_SHORT).show();
                Intent findFriendsIntent = new Intent(this, FindFriends.class);
                startActivity(findFriendsIntent);
                break;
            case R.id.nav_messages:
                Toast.makeText(getApplicationContext(),"Messages", Toast.LENGTH_SHORT).show();
                Intent messageIntent = new Intent(this, Message.class);
                startActivity(messageIntent);
                startActivity(messageIntent);
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
