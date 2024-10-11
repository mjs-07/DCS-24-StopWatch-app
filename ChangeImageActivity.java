package com.example.stopwatch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChangeImageActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION_CODE = 7;
    private static final int REQUEST_IMAGE_CAPTURE = 108;
    public ImageView imageView;
    private Button cap_button;
    private String currentPhotoPath;
    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);

        imageView = findViewById(R.id.imageTaken);
        cap_button = findViewById(R.id.capture_button);
        TextView typingText = findViewById(R.id.changephoto);
        String targetText = "CHANGE PHOTO";

        // Create a string to display

        // Initialize an index to track the current character
        final int[] currentIndex = {0};

        // Create a handler to post delayed actions
        Handler handler = new Handler();

        // Define a runnable to update the text
        Runnable updateTextRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentIndex[0] < targetText.length()) {
                    typingText.append(String.valueOf(targetText.charAt(currentIndex[0])));
                    currentIndex[0]++;
                    handler.postDelayed(this, 100); // Adjust the delay as needed
                }
            }
        };


        // Start the animation
        handler.postDelayed(updateTextRunnable, 100);

        // Remember to stop the animation when appropriate (e.g., when the activity is paused)
        // handler.removeCallbacks(updateTextRunnable);

        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_camera);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_camera) {
                return true;
            }  else if (item.getItemId() == R.id.bottom_logout) {
                startActivity(new Intent(getApplicationContext(), LogoutActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        cap_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File imageFile = new File(storageDirectory,"photo.jpg");
                    currentPhotoPath = imageFile.getAbsolutePath();
                    Uri imageUri = FileProvider.getUriForFile(ChangeImageActivity.this, "com.example.stopwatch.fileprovider", imageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            imageView.setImageBitmap(bitmap);
        }
    }
}