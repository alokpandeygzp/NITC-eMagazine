package com.example.nitcemag;

import android.content.Intent;
import android.os.Bundle;
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

public class RejectedArticleRepost extends AppCompatActivity {

    TextInputEditText tt;
    TextInputEditText desc;
    TextInputEditText auth;
    ImageView img;
    Button repostBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.rejected_article_repost);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));


        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        repostBtn=findViewById(R.id.buttonRejectedRepost);


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
                        {
                            System.out.println("****************************************************");
                        }
                        repostBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                ref.child(ds.getKey()).child("title").setValue(tt.getText().toString().trim());
                                ref.child(ds.getKey()).child("description").setValue(desc.getText().toString().trim());
                                ref.child(ds.getKey()).child("author").setValue(auth.getText().toString().trim());
                                ref.child(ds.getKey()).child("reviewer").setValue(0);
                                ref.child(ds.getKey()).child("editor").setValue(0);
                                ref.child(ds.getKey()).child("rejectreason").setValue("");
                                Toast.makeText(RejectedArticleRepost.this, "Article request sent for Publish", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(RejectedArticleRepost.this, RejectList.class);
                                finish();
                                startActivity(i);
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