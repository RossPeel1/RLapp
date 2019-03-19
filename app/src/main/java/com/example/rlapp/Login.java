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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
private Button loginButton;
private EditText userEmail, userPassword;
private TextView createAccountTxt;
private FirebaseAuth mAuth;
private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        createAccountTxt = (TextView) findViewById(R.id.create_account_txt);
        userEmail = (EditText) findViewById(R.id.login_e_mail_input_txt);
        userPassword = (EditText) findViewById(R.id.login_password_input_txt);
        loginButton = (Button) findViewById(R.id.login_btn);

        loadingBar = new ProgressDialog(this);

        createAccountTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegisterActivity();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AllowUserLogin();
            }
        });
    }

    private void AllowUserLogin()
    {
        String email = userEmail.getText().toString();
        String password= userPassword.getText().toString();

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
        }
        else
            {
                loadingBar.setTitle("Login in");
                loadingBar.setMessage("Login in Please wait");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            sendUserToMainActivity();
                            Toast.makeText(Login.this, "Signed in", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                        else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(Login.this, "Error occurred"+ message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                    }
                });
            }
    }

    private void sendUserToMainActivity()
    {
        Intent mainIntent = new Intent(Login.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void sendUserToRegisterActivity() {
        Intent registerIntent  = new Intent(Login.this, Register.class);
        startActivity(registerIntent);


    }
}
