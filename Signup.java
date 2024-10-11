package com.example.stopwatch;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stopwatch.databinding.ActivityMainBinding;
import com.example.stopwatch.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {
    private FirebaseAuth auth;
    public EditText name, email, pswd, conpswd;
    private Button signupbtn;
    private TextView login_redirect;
    public String Name, Email, password;
    private ActivitySignupBinding activitySignupBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        name = findViewById(R.id.Name);
        pswd = findViewById(R.id.password);
        conpswd = findViewById(R.id.confirm_password);
        signupbtn = findViewById(R.id.signup_button);
        login_redirect = findViewById(R.id.loginredirect);
        TextView typingText = findViewById(R.id.intro);
        String targetText = "Please register and sign up if you are new to this !!!";
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



        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = name.getText().toString();
                Email = email.getText().toString();
                password = pswd.getText().toString();
                String confirm_password = conpswd.getText().toString();

                if (Name.equals("")  || Email.equals("") || password.equals("") || confirm_password.equals("")) {
                    Toast.makeText(Signup.this, "All Fields are mandatory !!!", Toast.LENGTH_SHORT).show();
                }else {
                    if(pswd.length() >= 6){
                        if (password.equals(confirm_password)){
                            auth.createUserWithEmailAndPassword(Email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Signup.this, "SIGNUP SUCCESSFUL !!!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Signup.this, Login.class));
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(Signup.this, "SIGNUP FAILED ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(Signup.this, "Invalid Password !!! ", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Signup.this, "PASSWORD MUST BE ATLEAST 6 CHARACTERS", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        login_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, Login.class));
            }
        });
    }
}
