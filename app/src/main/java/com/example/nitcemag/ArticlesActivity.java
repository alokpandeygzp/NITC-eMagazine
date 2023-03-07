package com.example.nitcemag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitcemag.ui.home.Adapters.AdapterSports;
import com.example.nitcemag.ui.home.Models.ModelSports;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ArticlesActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    AdapterSports adapterSports;
    FirebaseDatabase firebaseDatabase;
    String title;
    DatabaseReference databaseReference;
    TextView tt, desc, auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        Intent intent= getIntent();
         title = intent.getStringExtra("title");
         tt= (TextView) findViewById(R.id.title);
         desc=findViewById(R.id.description);
         auth=findViewById(R.id.author);
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Articles");

        //get all
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot ds:snapshot.getChildren())
                {
                    ModelSports modelSports =ds.getValue(ModelSports.class);
                    //
                    if(modelSports.getTitle().equals(title))
                    {
//                        Toast.makeText(ArticlesActivity.this, ""+title, Toast.LENGTH_SHORT).show();
                        tt.setText(title);
                        desc.setText(modelSports.getDescription());
                        auth.setText(modelSports.getAuthor());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}