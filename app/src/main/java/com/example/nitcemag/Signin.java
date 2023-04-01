package com.example.nitcemag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signin extends AppCompatActivity {

    EditText username,password;
    Button signin;
    TextView signUp, forgotPassword;
    FirebaseUser user;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    Animation scaleUp, scaleDown;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_signin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));

        scaleUp= android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown= android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_down);

        username = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        signin = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signupText);
        forgotPassword = findViewById(R.id.forgotTextView);


        signin.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                    signin.startAnimation(scaleUp);
                else if(event.getAction()==MotionEvent.ACTION_UP)
                    signin.startAnimation(scaleDown);

                return false;
            }
        });


        signUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                    signUp.startAnimation(scaleUp);
                else if(event.getAction()==MotionEvent.ACTION_UP)
                    signUp.startAnimation(scaleDown);

                return false;
            }
        });


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

                showRecoverPasswordDialog();
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
                else
                {
                    Toast.makeText(Signin.this, "Incorrect Email or Password Entered.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showRecoverPasswordDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        //set layout linear
        LinearLayout linearLayout=new LinearLayout(this);
        //views to set in dialog
        EditText emailet=new EditText(this);
        emailet.setHint("Email");
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailet.setMinEms(16);

        linearLayout.addView(emailet);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        //buttons
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String em= emailet.getText().toString().trim();
//                if(em.equals(user.getEmail()))
//                {
                    beginRecovery(em);
//                }
//                else
//                {
//                    Toast.makeText(Signin.this, "Wrong Email", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        //buttons
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //show dialog
        builder.create().show();
    }
    private void beginRecovery(String em)
    {
        auth.sendPasswordResetEmail(em).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Signin.this, "Email Sent", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Signin.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //get and show proper error
                Toast.makeText(Signin.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}