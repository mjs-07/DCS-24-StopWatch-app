package com.example.stopwatch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stopwatch.databinding.ActivityMainBinding;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 3;
    public ImageView img;
    private TextView timerText;
    private Button start, stop, reset;
    private boolean isRunning = false;
    private int  seconds, minutes, milliseconds;
    private long millisecond, updateTime, startTime, timeBuff = 0L;
    private String currentPhotoPath;
    File imageFile;
    Uri uri;
    Handler handler;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            millisecond = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuff + millisecond;
            seconds = (int)(updateTime/1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliseconds = (int)(updateTime % 1000);

            timerText.setText(MessageFormat.format("{0} : {1} : {2}", minutes, String.format(Locale.getDefault(), "%02d", seconds), String.format(Locale.getDefault(), "%02d", milliseconds)));
            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChangeImageActivity c = new ChangeImageActivity();

        img = findViewById(R.id.img);
        timerText = findViewById(R.id.timer);
        start = findViewById(R.id.start_btn);
        stop = findViewById(R.id.stop_btn);
        reset = findViewById(R.id.reset_btn);
        TextView typingText = findViewById(R.id.stopwatch);
        String targetText = "STOPWATCH";

        // Create a string to display

        // Initialize an index to track the current character
        final int[] currentIndex = {0};

        // Create a handler to post delayed actions
        Handler nhandler = new Handler();

        // Define a runnable to update the text
        Runnable updateTextRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentIndex[0] < targetText.length()) {
                    typingText.append(String.valueOf(targetText.charAt(currentIndex[0])));
                    currentIndex[0]++;
                    nhandler.postDelayed(this, 100); // Adjust the delay as needed
                }
            }
        };


        // Start the animation
        nhandler.postDelayed(updateTextRunnable, 100);

        // Remember to stop the animation when appropriate (e.g., when the activity is paused)
        // handler.removeCallbacks(updateTextRunnable);

        handler = new Handler(Looper.myLooper());

        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.bottom_home)
                return true;
            else if (item.getItemId() == R.id.bottom_camera) {
                startActivity(new Intent(getApplicationContext(), ChangeImageActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.bottom_logout) {
                startActivity(new Intent(getApplicationContext(), LogoutActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
            File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            imageFile = new File(storageDirectory+"/photo.jpg");
            currentPhotoPath = imageFile.getAbsolutePath();
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            img.setImageBitmap(bitmap);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

    }

    public void startTimer()
    {
        if(!isRunning){
            startTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);
            isRunning = true;
            start.setEnabled(false);
            stop.setEnabled(true);
            reset.setEnabled(false);
        }
    }

    public void stopTimer()
    {
        if(isRunning){
            timeBuff += millisecond;
            handler.removeCallbacks(runnable);
            isRunning = false;
            start.setEnabled(true);
            start.setText("RESUME");
            stop.setEnabled(false);
            reset.setEnabled(true);
        }
    }

    public void resetTimer()
    {
        millisecond = startTime = timeBuff = updateTime = 0L;
        seconds = minutes = milliseconds = 0;
        timerText.setText("00 : 00 : 000");

        start.setEnabled(true);
        start.setText("START");
        reset.setEnabled(false);
        stop.setEnabled(false);
    }
}