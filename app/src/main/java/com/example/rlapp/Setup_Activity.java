package com.example.rlapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setup_Activity extends AppCompatActivity {

private EditText username, fullName, country;
private Button save;
private CircleImageView userProfleImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_);

        username = (EditText) findViewById(R.id.setupUsername);
        fullName = (EditText)findViewById(R.id.setupFullName);
        country = (EditText) findViewById(R.id.setupCountry);
        save = (Button) findViewById(R.id.setup_Save_btn);
        userProfleImage = (CircleImageView) findViewById(R.id.setup_profile_image);

    }
}
