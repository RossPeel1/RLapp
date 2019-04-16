package com.example.rlapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setup_Activity extends AppCompatActivity {

private EditText userName, fullName, countryName;
private Button save;
private CircleImageView userProfleImage;

private FirebaseAuth mAuth;
private DatabaseReference userref;

    private ProgressDialog loadingBar;

String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userref = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        userName = (EditText) findViewById(R.id.setupUsername);
        fullName = (EditText)findViewById(R.id.setupFullName);
        countryName = (EditText) findViewById(R.id.setupCountry);
        save = (Button) findViewById(R.id.setup_Save_btn);
        userProfleImage = (CircleImageView) findViewById(R.id.setup_profile_image);

        loadingBar = new ProgressDialog(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountInformation();
            }
        });
    }

    private void SaveAccountInformation() {                                                                    // Save the users account information
        String username = userName.getText().toString();
        String fullname = fullName.getText().toString();
        String countryname = countryName.getText().toString();

        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please Enter Your Username...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(fullname))
        {
            Toast.makeText(this, "Please Enter Your Full Name...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(countryname))
        {
            Toast.makeText(this, "Please Enter Your Country...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Saving account Information");
            loadingBar.setMessage("Creating Account Information");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("Username", username);
            userMap.put("Full Name",fullname);
            userMap.put("Country Name", countryname);
            userMap.put("Status", "Defualt value" );
            userMap.put("Gender", "None");
            userMap.put("Date of Birth", "DOB");
            userMap.put("Personality", "None");
            userref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {

                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        SendUserToMainActivity();
                        Toast.makeText(Setup_Activity.this, "Your Account is Created successfully", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(Setup_Activity.this, "Error Occurred" + message, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(Setup_Activity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
