package com.example.stopwatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_logout);


        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.bottom_camera) {
                startActivity(new Intent(getApplicationContext(), ChangeImageActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.bottom_logout) {
                return true;
            }
            return false;
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.logout_dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button yes = view.findViewById(R.id.yes_button);
        TextView typingText = view.findViewById(R.id.logout_text);
        String targetText = "Are you sure you want to logout ? ";

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

        cancel.setOnClickListener(v-> {
            startActivity(new Intent(LogoutActivity.this, MainActivity.class));
            dialog.dismiss();
        });
        yes.setOnClickListener(v-> {
            startActivity(new Intent(LogoutActivity.this, Login.class));
            dialog.dismiss();
        });

    }
}