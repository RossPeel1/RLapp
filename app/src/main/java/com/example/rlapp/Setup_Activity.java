package com.example.rlapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setup_Activity extends AppCompatActivity {

private EditText userName, fullName, countryName;
private Button save;
private CircleImageView userProfleImage;

private FirebaseAuth mAuth;
private DatabaseReference userref;
private StorageReference userProfilePictureRefrance;

private ProgressDialog loadingBar;

String currentUserID;
final static int gallerypic = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userref = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        userProfilePictureRefrance = FirebaseStorage.getInstance().getReference().child("profile Images");

        userName = (EditText) findViewById(R.id.setupUsername);
        fullName = (EditText)findViewById(R.id.setupFullName);
        countryName = (EditText) findViewById(R.id.setupCountry);
        save = (Button) findViewById(R.id.setup_Save_btn);
        userProfleImage = (CircleImageView) findViewById(R.id.setup_profile_image);

        loadingBar = new ProgressDialog(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SaveAccountInformation();
            }
        });

        userProfleImage.setOnClickListener(new View.OnClickListener() {                             // redirect user to there mobile phone gallery
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, gallerypic);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {                 // when the user selects an image  send to the cropping activity
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == gallerypic && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        // Cuando se pulsa en el crop button
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)                               // get the image when croped button is pressed
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                loadingBar.setTitle("Profile image");
                loadingBar.setMessage("Please wait while we update your profile image...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri = result.getUri();

                StorageReference filePath = userProfilePictureRefrance.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {

                            Toast.makeText(Setup_Activity.this, "Profile image successfully stored in Firebase storage...", Toast.LENGTH_SHORT).show();

                            Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();

                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl = uri.toString();

                                    userref.child("profile Images").setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        Intent selfIntent = new Intent(Setup_Activity.this, Setup_Activity.class);
                                                        startActivity(selfIntent);

                                                        Toast.makeText(Setup_Activity.this, "Profile image stored in Firebase Storage successfully...", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    }
                                                    else
                                                        {
                                                        String message = task.getException().getMessage();
                                                        Toast.makeText(Setup_Activity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                        }
                                                }
                                            });
                                }
                            });
                        }
                    }
                });
            }
            else {
                Toast.makeText(Setup_Activity.this, "Error: La imagen no se ha cortado bien. Prueba otra vez.", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
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
