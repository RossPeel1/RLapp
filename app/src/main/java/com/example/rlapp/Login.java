package com.example.rlapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
private Button loginButton;
private EditText userEmail, userPassword;
private TextView createAcountTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createAcountTxt = (TextView) findViewById(R.id.create_account_txt);
        userEmail = (EditText) findViewById(R.id.e_mail_input_txt);
        userPassword = (EditText) findViewById(R.id.password_input_txt);
        loginButton = (Button) findViewById(R.id.login_btn);

        createAcountTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegisterActivity();
            }
        });
    }

    private void sendUserToRegisterActivity() {
        Intent registerIntent  = new Intent(Login.this, Register.class);
        startActivity(registerIntent);


    }
}
