package com.example.nitcemag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewerAction extends AppCompatActivity {

    TextView tt, desc, auth;
    FirebaseDatabase firebaseDatabase;
    String title;
    DatabaseReference databaseReference;
    Button acceptBtn, rejectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_reviewer_action);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));


        Intent intent = getIntent();
        title = intent.getStringExtra("title");

        acceptBtn=findViewById(R.id.buttonAcceptReview);
        rejectBtn=findViewById(R.id.buttonRejectReview);


        tt = findViewById(R.id.title);
        desc = findViewById(R.id.description);
        auth = findViewById(R.id.author);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Articles");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Articles articles = ds.getValue(Articles.class);

                    if (articles.getTitle().equals(title)) {
                        tt.setText(title);
                        desc.setText(articles.getDescription());
                        auth.setText(articles.getAuthor());

                        acceptBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ref.child(ds.getKey()).child("reviewer").setValue(1);
                                Toast.makeText(ReviewerAction.this, "Article Accepted for Publish", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(ReviewerAction.this, ReviewerArticlesList.class);
                                finish();
//                                overridePendingTransition(0, 0);
                                startActivity(i);
//                                overridePendingTransition(0, 0);
                            }
                        });
                        rejectBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ref.child(ds.getKey()).child("reviewer").setValue(-1);
                                Toast.makeText(ReviewerAction.this, "Article Rejected for Publish", Toast.LENGTH_SHORT).show();


                                Intent i = new Intent(ReviewerAction.this, ReviewerArticlesList.class);
                                finish();
//                                overridePendingTransition(0, 0);
                                startActivity(i);
//                                overridePendingTransition(0, 0);
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
}