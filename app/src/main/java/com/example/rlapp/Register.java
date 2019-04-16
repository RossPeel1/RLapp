package com.example.rlapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private EditText userEmail, userPassword, userConfirmPass;
    private Button createAccountButton;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        userEmail =(EditText) findViewById(R.id.e_mail_input_txt);
        userPassword = (EditText) findViewById(R.id.password_input_txt);
        userConfirmPass = (EditText) findViewById(R.id.confirm_password_inupt_txt);
        createAccountButton = (Button) findViewById(R.id.create_account_btn);
        loadingBar = new ProgressDialog(this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            CreateNewAccount();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            sendUserToMainActivity();
        }
    }

    private void sendUserToMainActivity()                                                           // method for the creation of an intent to the user to the main activity
    {
        Intent mainIntent = new Intent(Register.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


    private void CreateNewAccount() {
        String email = userEmail.getText().toString();                               // get user inputs from register text boxes [
        String password = userPassword.getText().toString();
        String confirm = userConfirmPass.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter your email address" ,Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
            {
            Toast.makeText(this,"Please enter your password" ,Toast.LENGTH_SHORT).show();
            }
        else if(TextUtils.isEmpty(confirm))
        {
            Toast.makeText(this,"Please enter your confirmed password" ,Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirm))
        {
            Toast.makeText(this, "password and confirm password do not match!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Creating new account");
            loadingBar.setMessage("creating account");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        sendUserToSetupActivity();
                        Toast.makeText(Register.this, " Account registration successful", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    else
                        {
                            String message = task.getException().getMessage();
                            Toast.makeText(Register.this,"error occurred" + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                }
            });
        }
    }

    private void sendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(Register.this ,Setup_Activity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }
}
