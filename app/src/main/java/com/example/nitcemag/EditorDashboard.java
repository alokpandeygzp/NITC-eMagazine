package com.example.nitcemag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditorDashboard extends AppCompatActivity {

    AlertDialog dialog;
    CardView addReviewer;
    CardView publishArticlesBtn;
    EditText emailDialog;
    Button addRevBtn;
    TextView demo;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    DatabaseReference ref = database.getReference();
    String userEmailId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_editor_dashboard);
        demo = findViewById(R.id.demo);
        demo.setVisibility(View.INVISIBLE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));


        addReviewer=findViewById(R.id.add_reviewer);
        publishArticlesBtn=findViewById(R.id.publish_articles);

        addReviewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogAddRev = new AlertDialog.Builder(EditorDashboard.this);
                View loginView = getLayoutInflater().inflate(R.layout.dialog_add_reviewer,null);

                emailDialog = loginView.findViewById(R.id.textViewReason);
                addRevBtn = loginView.findViewById(R.id.buttonDialogSubmit);

//                demo.setVisibility(View.INVISIBLE);


                dialogAddRev.setView(loginView);
                dialog = dialogAddRev.create();
                dialog.show();

                addRevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userEmailId = emailDialog.getText().toString();
                        reference.child("UserType").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                System.out.println("2");
                                for(DataSnapshot ds:snapshot.getChildren()){
                                    System.out.println(ds);
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
                                            demo.setText(role);
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
                        dialog.dismiss();
                    }
                });

            }
        });




        //Publish Articles Section goes here......


        publishArticlesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorDashboard.this, EditorArticlesList.class);
                startActivity(intent);
            }
        });


    }

    void setRole()
    {
        String role[] = demo.getText().toString().split(" ");
        System.out.println(role[0]);

        if(role[0].equalsIgnoreCase("student"))
        {
            reference.child("UserType").child(role[1]).child("role").setValue("Reviewer");
            Toast.makeText(EditorDashboard.this, "Reviewer added successfully", Toast.LENGTH_SHORT).show();
            String str=userEmailId;
            userEmailId = "Dummy";
            demo.setText("");


            reference.child("Student").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds:snapshot.getChildren()){
                        if(ds.child("email").getValue().toString().equals(str))
                        {
                            System.out.println(ds);
                            UserDetails ud=ds.getValue(UserDetails.class);
                            System.out.println(ud.email+"   "+ud.name+"  "+ud.photo);
                            ref.child("Reviewer").child(role[1]).setValue(ud);
                            ds.getRef().removeValue();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else if (role[0].equalsIgnoreCase("Reviewer")) {
            Toast.makeText(EditorDashboard.this, "Already a reviewer", Toast.LENGTH_SHORT).show();
            demo.setText("");
        } else if (role[0].equalsIgnoreCase("Editor")) {
            Toast.makeText(EditorDashboard.this, "Already a Editor", Toast.LENGTH_SHORT).show();
            demo.setText("");
        }
        else if(role[0].equalsIgnoreCase("1234")) {
            demo.setText("");
            Toast.makeText(this, "Not a register user.", Toast.LENGTH_SHORT).show();
        }
    }

}