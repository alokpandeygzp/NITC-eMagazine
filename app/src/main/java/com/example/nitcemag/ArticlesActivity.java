package com.example.nitcemag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitcemag.ui.home.Adapters.AdapterSports;
import com.example.nitcemag.ui.home.Models.ModelSports;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ArticlesActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    AdapterSports adapterSports;
    FirebaseDatabase firebaseDatabase;
    List<ModelComment> list= new ArrayList<>();
    DatabaseReference ref;
    String title,name;
    TextView tt, desc, auth;
    ImageView img,send;
    TextInputEditText com;
    RecyclerView rv;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
         Intent intent= getIntent();
         String key=intent.getStringExtra("key");
         title = intent.getStringExtra("title");
         tt= (TextView) findViewById(R.id.title);
         img=findViewById(R.id.image);
         desc=findViewById(R.id.description);
         auth=findViewById(R.id.author);
         send=findViewById(R.id.imgbutton);
         com=findViewById(R.id.comment);
         rv=findViewById(R.id.comments_recyclerView);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(ArticlesActivity.this));
         firebaseAuth=FirebaseAuth.getInstance();


        ref= FirebaseDatabase.getInstance().getReference("Articles");

        //get all
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot ds:snapshot.getChildren())
                {
                    ModelSports modelSports =ds.getValue(ModelSports.class);
                    //
                   if(ds.getKey().equals(key))
                    {
                        tt.setText(title);
                        desc.setText(modelSports.getDescription());
                        auth.setText(modelSports.getAuthor());
                        String image=modelSports.getImage();
                        try
                        {
                            Picasso.get().load(image).placeholder(R.drawable.newspaper).into(img);
                        }
                        catch (Exception e)
                        {           }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user==null ) {
            com.setVisibility(View.GONE);
            send.setVisibility(View.GONE);
            rv.setVisibility(View.GONE);
        }
        else {
            ref= FirebaseDatabase.getInstance().getReference("User");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        if(ds.getKey().equals(firebaseAuth.getCurrentUser().getUid().toString()))
                        {
                            name=ds.child("name").getValue().toString();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String cmnt = com.getText().toString().trim();
                    cmnt.replaceAll("\n", " ");
                    com.setText("");
                    com.clearFocus();
                    if (cmnt.equals("")) {
                        Toast.makeText(ArticlesActivity.this, "Empty", Toast.LENGTH_SHORT).show();
                    } else {
                        ref = FirebaseDatabase.getInstance().getReference();
                        String k = ref.child("Comments").push().getKey();
                        ModelComment mc = new ModelComment(firebaseAuth.getCurrentUser().getEmail().toString(), key, cmnt, name, k);
                        ref.child("Comments").child(k).setValue(mc);
                    }
                }
            });

            ref = FirebaseDatabase.getInstance().getReference("Comments");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ModelComment mc = ds.getValue(ModelComment.class);
                        if (mc.getArticle().equals(key)) {
                            list.add(mc);
                        }
                    }
                    //adapter
                    ArtcilesAdapter aa = new ArtcilesAdapter(ArticlesActivity.this, list);
                    //set adapter to recycler view
                    rv.setAdapter(aa);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

}