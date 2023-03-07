package com.example.nitcemag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signin extends AppCompatActivity {

    EditText username,password;
    Button signin;
    TextView signUp, forgotPassword;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_signin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));

        username = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        signin = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signupText);
        forgotPassword = findViewById(R.id.forgotTextView);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmailId = username.getText().toString();
                String userPassword = password.getText().toString();

                if (userEmailId.isEmpty() && userPassword.isEmpty()) {
                    Toast.makeText(Signin.this, "Please enter the Email id and Password", Toast.LENGTH_SHORT).show();
                } else if (userEmailId.isEmpty()) {
                    Toast.makeText(Signin.this, "Please enter a Email id", Toast.LENGTH_SHORT).show();

                } else if (userPassword.isEmpty()) {
                    Toast.makeText(Signin.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                } else if (Patterns.EMAIL_ADDRESS.matcher(userEmailId).matches()) {
                    signInWithFirebase(userEmailId, userPassword);


                } else {
                    Toast.makeText(Signin.this, "Please enter a valid email id", Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this, Signup.class);
                startActivity(intent);
            }
        });
    }


    private void signInWithFirebase(String userEmailId, String userPassword) {
        auth.signInWithEmailAndPassword(userEmailId,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    if(auth.getCurrentUser().isEmailVerified())
                    {
                        Toast.makeText(Signin.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Signin.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Signin.this, "Please verify your email.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}