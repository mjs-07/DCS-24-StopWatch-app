package com.example.stopwatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stopwatch.databinding.ActivityLoginBinding;
import com.example.stopwatch.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private Button login_btn;
    private TextView signupRedirect;
    private boolean cursorVisible = true;
    private ActivityLoginBinding loginBinding;
    String targetText = "Please fill the below details to login to app !!!";

        // Start cursor blinking


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.login_button);
        signupRedirect = findViewById(R.id.signup_redirect);
        // Assume you have a TextView named "typingText" in your layout
        TextView typingText = findViewById(R.id.login_intro);

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


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                if(!email.equals("") && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if(!password.equals("")){
                        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(Login.this, "LOGIN SUCCESSFUL !!!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "LOGIN FAILED ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(Login.this, "PASSWORD IS MANDATORY !!", Toast.LENGTH_SHORT).show();
                    }
                } else if (email.isEmpty()) {
                    Toast.makeText(Login.this, "EMAIL IS MANDATORY !!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Login.this, "PLEASE ENTER VALID EMAIL !!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Signup.class));
            }
        });
    }
}
