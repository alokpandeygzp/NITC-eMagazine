package com.example.nitcemag;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.nitcemag.ui.postArticles.UserArticles;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ReviewerAction extends AppCompatActivity {

    TextInputEditText tt;
    TextInputEditText desc;
    TextInputEditText auth;
    ImageView img;
    Button acceptBtn, rejectBtn, sendBtn;
    AlertDialog dialog;
    EditText descDialog;
    String dialogDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_reviewer_action);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));


        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        acceptBtn=findViewById(R.id.buttonAcceptReview);
        rejectBtn=findViewById(R.id.buttonRejectReview);


        tt = findViewById(R.id.title);
        desc = findViewById(R.id.description);
        auth = findViewById(R.id.author);
        img=findViewById(R.id.image);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Articles");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    UserArticles articles = ds.getValue(UserArticles.class);

                    if (articles.getKey().equals(key)) {
                        tt.setText(articles.getTitle());
                        desc.setText(articles.getDescription());
                        auth.setText(articles.getAuthor());
                        try
                        {
                            Picasso.get().load(articles.getImage()).placeholder(R.drawable.newspaper).into(img);
                        }
                        catch (Exception e)
                        {           }
                        acceptBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                ref.child(ds.getKey()).child("title").setValue(tt.getText().toString().trim());
                                ref.child(ds.getKey()).child("description").setValue(desc.getText().toString().trim());
                                ref.child(ds.getKey()).child("author").setValue(auth.getText().toString().trim());
                                ref.child(ds.getKey()).child("reviewer").setValue(1);
                                Toast.makeText(ReviewerAction.this, "Article Accepted for Publish", Toast.LENGTH_SHORT).show();

                                finish();
                            }
                        });
                        rejectBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder dialogAddRev = new AlertDialog.Builder(ReviewerAction.this);
                                View loginView = getLayoutInflater().inflate(R.layout.dialog_sendrejectmsg,null);

                                descDialog = loginView.findViewById(R.id.textViewReason);
                                sendBtn = loginView.findViewById(R.id.buttonDialogSubmit);

                                //                demo.setVisibility(View.INVISIBLE);


                                dialogAddRev.setView(loginView);
                                dialog = dialogAddRev.create();
                                dialog.show();


                                sendBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ref.child(ds.getKey()).child("reviewer").setValue(-1);
                                        Toast.makeText(ReviewerAction.this, "Article Rejected", Toast.LENGTH_SHORT).show();

                                        dialogDesc = descDialog.getText().toString();
                                        ref.child(ds.getKey()).child("rejectreason").setValue(dialogDesc);
                                        finish();
                                    }
                                });
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}