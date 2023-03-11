package com.example.nitcemag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    EditText emailDialog;
    Button addRevBtn;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_editor_dashboard);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));


        addReviewer=findViewById(R.id.add_reviewer);

        addReviewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogAddRev = new AlertDialog.Builder(EditorDashboard.this);
                View loginView = getLayoutInflater().inflate(R.layout.dialog_add_reviewer,null);

                emailDialog = loginView.findViewById(R.id.editTextDialogEmail);
                addRevBtn = loginView.findViewById(R.id.buttonDialogSubmit);

                dialogAddRev.setView(loginView);
                dialog = dialogAddRev.create();
                dialog.show();

                addRevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userEmailId = emailDialog.getText().toString();
                        reference.child("User").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for(DataSnapshot ds:snapshot.getChildren()){
                                    if(ds.child("email").getValue().toString().equals(userEmailId))
                                    {
                                        String role = ds.child("role").getValue().toString();
                                        if (role.equals("Student"))
                                        {
                                            reference.child("User").child(ds.getKey()).child("role").setValue("Reviewer");
                                            Toast.makeText(EditorDashboard.this, "Reviewer added successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else if(role.equals("Reviewer"))
                                        {
                                            Toast.makeText(EditorDashboard.this, "Already a reviewer.", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(EditorDashboard.this, "Not a student.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
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

    }
}