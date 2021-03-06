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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private Toolbar mainToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private CircleImageView NavProfileImage;
    private TextView NavProfileName;
    private ImageButton AddNewPostButton;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();                                                         // initialise authentication for freebase
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");                    // Get the user reference number form fire base and store under users

        mainToolbar = (Toolbar) findViewById(R.id.main_page_Toolbar);                               // setup menu bar layout
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Home");

        AddNewPostButton = (ImageButton) findViewById(R.id.add_new_post_button);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_Layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_Open, R.string.drawer_Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigation_View);

        View navView = navigationView.inflateHeaderView(R.layout.header);                           // allow access to naview
        NavProfileImage = (CircleImageView) navView.findViewById(R.id.navProfileImage);             //cast nav profile image and name note Cast "NavView" to access crashes without.
        NavProfileName = (TextView) navView.findViewById(R.id.navProfileName);

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("Full Name")) // does the user name exists in fire base for the current user
                    {
                        String fullname = dataSnapshot.child("Full Name").getValue().toString();
                        NavProfileName.setText(fullname);
                    }
                    if (dataSnapshot.hasChild("profile Images"))
                    {
                        String image = dataSnapshot.child("profile Images").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(NavProfileImage);
                    }
                    else
                        {
                            Toast.makeText(MainActivity.this, "Profile does not exist", Toast.LENGTH_SHORT).show();
                        }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });
        AddNewPostButton.setOnClickListener(new View.OnClickListener() {                            // if user selects new post button/ image send them to post activity
            @Override
            public void onClick(View v) {
                SendUserToPostActivity();
           }
        });
    }

    private void SendUserToPostActivity() {

        Intent AddNewPostIntent = new Intent(MainActivity.this, Post.class);
        startActivity(AddNewPostIntent);

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            SendUserToLoginActivity();
        }
        else
        {
            CheckUserExistence();
        }
}

    private void CheckUserExistence() {
        final String current_user_id = mAuth.getCurrentUser().getUid();                             // Get the user ID from fire base

        UsersRef.addValueEventListener(new ValueEventListener()
        {

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(!dataSnapshot.hasChild(current_user_id))
        {
            SendUserToSetupActivity();
        }
    }

    public void onCancelled(DatabaseError databaseError) {
    }
        });
    }

    private void SendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(MainActivity.this, Setup_Activity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);      // validation
        startActivity(setupIntent);
        finish();
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);      // validation
        startActivity(loginIntent);
        finish();
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
                SendUserToPostActivity();
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
                mAuth.signOut();
                SendUserToLoginActivity();
                break;
        }
    }
}
