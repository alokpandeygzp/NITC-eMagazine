package com.example.nitcemag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditorChange extends AppCompatActivity {

    EditText email;
    Button submitBtn;
    String userEmailId;
    TextView demo;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    DatabaseReference ref = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_editor_change);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));



        email = findViewById(R.id.textViewEmail);
        submitBtn = findViewById(R.id.buttonDialogSubmit);
        demo = findViewById(R.id.demo);
        demo.setVisibility(View.INVISIBLE);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmailId = email.getText().toString();
                reference.child("UserType").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot ds:snapshot.getChildren()){

                            if(ds.child("email").getValue().toString().equals(userEmailId))
                            {
                                String role = ds.child("role").getValue().toString();
                                if (role.equals("Student"))
                                {
                                    String role1 = "student "+ds.getKey();
                                    demo.setText(role1);
                                    break;
                                }
                                else if(role.equals("Reviewer"))
                                {
                                    String role1 = "Reviewer "+ds.getKey();
                                    demo.setText(role1);
                                    break;
                                }
                                else
                                {
                                    demo.setText(role);
                                    break;
                                }
                            }
                            else {
                                if(!userEmailId.equalsIgnoreCase("dummy"))
                                    demo.setText("1234");
                            }
                        }
                        setRole();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        error.getDetails();
                    }
                });
            }
        });
    }


    void setRole()
    {
        DatabaseReference ref2 = database.getReference();
        String[] role = demo.getText().toString().split(" ");
        //System.out.println(role[0]);

        if(role[0].equalsIgnoreCase("student"))
        {
            Toast.makeText(EditorChange.this, "Editor added successfully", Toast.LENGTH_SHORT).show();
            demo.setText("");


            ref.child("Editor").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        String email = snapshot.child("email").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String photo = snapshot.child("photo").getValue().toString();

                        HashMap<String, String> mp = new HashMap<>();
                        mp.put("name", name);
                        mp.put("photo", photo);
                        mp.put("email", email);

                        DatabaseReference ref1 = database.getReference();
                        ref1.child("Student").child(user.getUid()).setValue(mp).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {



                                HashMap<String, String> mp2 = new HashMap<>();
                                mp2.put("email", email);
                                mp2.put("role","Student");

                                ref2.child("UserType").child(user.getUid()).setValue(mp2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ref2.child("Editor").child(user.getUid()).removeValue();
                                    }
                                });
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        System.out.println();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            ref.child("Student").child(role[1]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        String email = snapshot.child("email").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String photo = snapshot.child("photo").getValue().toString();

                        HashMap<String, String> mp = new HashMap<>();
                        mp.put("name", name);
                        mp.put("photo", photo);
                        mp.put("email", email);

                        DatabaseReference ref1 = database.getReference();
                        ref1.child("Editor").child(role[1]).setValue(mp).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                HashMap<String, String> mp2 = new HashMap<>();
                                mp2.put("email", email);
                                mp2.put("role","Editor");

                                ref2.child("UserType").child(role[1]).setValue(mp2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ref2.child("Student").child(role[1]).removeValue();
                                    }
                                });
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        System.out.println();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            FirebaseAuth auth=FirebaseAuth.getInstance();
            auth.signOut();

            Intent i= new Intent(EditorChange.this, MainActivity.class);
            startActivity(i);
            finishAffinity();
            Toast.makeText(this, "You are signed out", Toast.LENGTH_SHORT).show();

        } else if (role[0].equalsIgnoreCase("Reviewer")) {

            Toast.makeText(EditorChange.this, "Editor added successfully", Toast.LENGTH_SHORT).show();
            demo.setText("");

            ref.child("Editor").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        String email = snapshot.child("email").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String photo = snapshot.child("photo").getValue().toString();

                        HashMap<String, String> mp = new HashMap<>();
                        mp.put("name", name);
                        mp.put("photo", photo);
                        mp.put("email", email);

                        DatabaseReference ref1 = database.getReference();


                        ref1.child("Student").child(user.getUid()).setValue(mp).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {


                                DatabaseReference ref2 = database.getReference();
                                HashMap<String, String> mp2 = new HashMap<>();
                                mp2.put("email", email);
                                mp2.put("role","Student");

                                ref2.child("UserType").child(user.getUid()).setValue(mp2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ref2.child("Editor").child(user.getUid()).removeValue();
                                    }
                                });
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        System.out.println();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            ref.child("Reviewer").child(role[1]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        String email = snapshot.child("email").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String photo = snapshot.child("photo").getValue().toString();

                        HashMap<String, String> mp = new HashMap<>();
                        mp.put("name", name);
                        mp.put("photo", photo);
                        mp.put("email", email);

                        DatabaseReference ref1 = database.getReference();
                        ref1.child("Editor").child(role[1]).setValue(mp).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                DatabaseReference ref2 = database.getReference();

                                HashMap<String, String> mp2 = new HashMap<>();
                                mp2.put("email", email);
                                mp2.put("role","Editor");

                                ref2.child("UserType").child(role[1]).setValue(mp2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ref2.child("Reviewer").child(role[1]).removeValue();
                                    }
                                });
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        System.out.println();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            FirebaseAuth auth=FirebaseAuth.getInstance();
            auth.signOut();

            Intent i= new Intent(EditorChange.this, MainActivity.class);
            startActivity(i);
            finishAffinity();
            Toast.makeText(this, "You are signed out", Toast.LENGTH_SHORT).show();

        } else if (role[0].equalsIgnoreCase("Editor")) {
            Toast.makeText(EditorChange.this, "Already a Editor", Toast.LENGTH_SHORT).show();
            demo.setText("");
        }
        else if(role[0].equalsIgnoreCase("1234")) {
            demo.setText("");
            Toast.makeText(this, "Not a registered user.", Toast.LENGTH_SHORT).show();
        }
    }
}