package com.example.nitcemag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    EditText email;
    EditText name;
    EditText password;
    Button signUpBtn;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.editTextSignUpName);
        email = findViewById(R.id.editTextSignUpEmail);
        password = findViewById(R.id.editTextSignUpPassword);
        signUpBtn = findViewById(R.id.SignUpBtn);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String userEmailId = email.getText().toString();
                String userPassword = password.getText().toString();

                if(userEmailId.isEmpty() && userPassword.isEmpty())
                {
                    Toast.makeText(Signup.this, "Please enter the Email id and Password", Toast.LENGTH_SHORT).show();
                }
                else if(userEmailId.isEmpty())
                {
                    Toast.makeText(Signup.this, "Please enter a Email id", Toast.LENGTH_SHORT).show();

                }
                else if(userPassword.isEmpty())
                {
                    Toast.makeText(Signup.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                }
                else if(Patterns.EMAIL_ADDRESS.matcher(userEmailId).matches())
                {
                    boolean check = checkNitcEmail(userEmailId);
                    if(check){
                        signUpBtn.setClickable(false);
                        signUpFirebase(userEmailId, userPassword);

                    }
                    else
                        Toast.makeText(Signup.this, "Please Enter NITC Email id", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(Signup.this, "Please enter a valid email id", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean checkNitcEmail(String userEmailId) {
        String email[] = userEmailId.split("@");
        if(email[1].equals("nitc.ac.in"))
            return true;
        return false;
    }

    private void signUpFirebase(String userEmailId, String userPassword) {

        auth.createUserWithEmailAndPassword(userEmailId,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Signup.this, "Your account is created successfully", Toast.LENGTH_LONG).show();
                    UserDetails user = new UserDetails(name.getText().toString(), email.getText().toString());
                    reference.child("User").child(auth.getCurrentUser().getUid()).setValue(user);
//                            getParentFragmentManager().popBackStack();

//                    NavigationView navigationView = findViewById(R.id.nav_view);
//                    Menu menu = navigationView.getMenu();
//                    //menu.findItem(R.id.nav_signup).setVisible(false);
//                    //menu.findItem(R.id.nav_signin).setVisible(false);
//                    menu.findItem(R.id.nav_signup).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(@NonNull MenuItem item) {
//                            System.out.println("HelLo Buffalo");
//                            return false;
//                        }
//                    });
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(Signup.this, "This email id is already present", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        System.out.println(task.getException());
                        Toast.makeText(Signup.this, "There is a problem, Please try after sometime", Toast.LENGTH_SHORT).show();
                    }
                    signUpBtn.setClickable(true);
                }
            }
        });
    }
}