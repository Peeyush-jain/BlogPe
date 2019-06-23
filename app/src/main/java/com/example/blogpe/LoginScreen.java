package com.example.blogpe;
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
import android.widget.ImageButton;

public class LoginScreen extends AppCompatActivity {

    private ImageButton pickImageButton;
    private static int REQUEST_CODE = 1;
    private static int PReqCode = 1;
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
