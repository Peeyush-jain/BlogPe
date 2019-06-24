package com.example.blogpe;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import Models.Post;

public class LoginScreen extends AppCompatActivity {

    private ImageButton pickImageButton;
    private static int REQUEST_CODE = 1;
    private static int PReqCode = 1;
    private static EditText titleText;
    private static EditText authorName;
    private static EditText content;
    private static Button postButton;
    Uri pickedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        //Initialize Image
        pickImageButton = findViewById(R.id.addImage);

        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check build view for permissions
                if(Build.VERSION.SDK_INT >= 22) {
                    checkAndAskPermission();
                }
                else {
                    openGallery();
                }
            }
        });


        //Init
        postButton = findViewById(R.id.publishButton);
        titleText = findViewById(R.id.titleText);
        authorName = findViewById(R.id.authorName);
        content = findViewById(R.id.contentBox);


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!titleText.getText().toString().isEmpty()
                        && !authorName.getText().toString().isEmpty()
                        && pickedImageUri != null
                        && !content.getText().toString().isEmpty()) {

                    //Upload image to firebase
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImageUri.getLastPathSegment());

                    imageFilePath.putFile(pickedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownloadLink = uri.toString();

                                    //Create post object
                                    Post post = new Post(titleText.getText().toString(),
                                            content.getText().toString(),
                                            56,
                                            imageDownloadLink, authorName.getText().toString());

                                    //Add post to firebase database
                                    addPost(post);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //In case of error in uploading image
                                    showMessage(e.getMessage());
                                }
                            });
                        }
                    });
                }
                else {
                    showMessage("Please fill every field");
                }
            }
        });
    }

    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("blogpefirebase").push();

        //Add post data to firebase database
        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post added successfully");
            }
        });

    }

    private void showMessage(String message) {
        Toast.makeText(LoginScreen.this, message, Toast.LENGTH_SHORT).show();
    }

    private void openGallery() {

        Intent galleyIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleyIntent.setType("image/*");
        startActivityForResult(galleyIntent, REQUEST_CODE);

    }

    private void checkAndAskPermission() {
        if(ContextCompat.checkSelfPermission(LoginScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(LoginScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            }
            else {
                ActivityCompat.requestPermissions(LoginScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }
        else {
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            //Upload successful
            //Add image to Uri
            pickedImageUri = data.getData();
            pickImageButton.setImageURI(pickedImageUri);

        }
    }
}
