package com.example.nitcemag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitcemag.ui.ModelLike;
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
    List<ModelComment> list= new ArrayList<>();
    DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
    String title,name;
    TextView tt, desc, auth,lcount;
    ImageView img,send,like;
    TextInputEditText com;
    RecyclerView rv;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_articles);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));


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
         like=findViewById(R.id.like);
         lcount=findViewById(R.id.lcount);
         rv.setHasFixedSize(true);
         rv.setLayoutManager(new LinearLayoutManager(ArticlesActivity.this));
         firebaseAuth=FirebaseAuth.getInstance();


        //get all
        reference.child("Articles").addValueEventListener(new ValueEventListener() {
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
            like.setVisibility(View.GONE);
            lcount.setVisibility(View.GONE);
        }
        else {
            // Name needed for commenting on the article after Signin only
            reference.child("Likes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int count=0;
                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        if(ds.child("article").getValue().toString().equals(key))
                        {
                            count=count+1;
                        }
                        if(ds.child("email").getValue().toString().equals(firebaseAuth.getCurrentUser().getEmail()) && ds.child("article").getValue().equals(key))
                        {
                            try
                            {
                                Picasso.get().load(R.drawable.like).placeholder(R.drawable.liked).into(like);
                            }
                            catch (Exception e)
                            {

                            }
                            break;
                        }
                    }
                    lcount.setText(Integer.toString(count)+"Like");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reference.child("Likes").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int flag=1;
                            for(DataSnapshot ds:snapshot.getChildren())
                            {
                                if(ds.child("email").getValue().toString().equals(firebaseAuth.getCurrentUser().getEmail()) && ds.child("article").getValue().equals(key))
                                {
                                    try
                                    {
                                        Picasso.get().load(R.drawable.liked).placeholder(R.drawable.like).into(like);

                                    }
                                    catch (Exception e)
                                    {

                                    }
                                    ds.getRef().removeValue();
                                    //DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference("Likes").child(ds.child("key").getValue().toString());
                                    //mPostReference.removeValue();
                                    flag=0;
                                    break;
                                }
                            }

                            if(flag==1)
                            {
                                String k = reference.child("Likes").push().getKey();
                                ModelLike mc = new ModelLike(firebaseAuth.getCurrentUser().getEmail().toString(), key, name, k);
                                reference.child("Likes").child(k).setValue(mc);
                                try
                                {
                                    Picasso.get().load(R.drawable.like).placeholder(R.drawable.liked).into(like);
                                }
                                catch (Exception e)
                                {

                                }
                                return;
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

            reference.child("UserType").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String roles=snapshot.child("role").getValue().toString();

                    reference.child(roles).addValueEventListener(new ValueEventListener() {
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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            System.out.println("************************************"+name);
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
                        reference = FirebaseDatabase.getInstance().getReference();
                        String k = reference.child("Comments").push().getKey();
                        ModelComment mc = new ModelComment(firebaseAuth.getCurrentUser().getEmail().toString(), key, cmnt, name, k);
                        reference.child("Comments").child(k).setValue(mc);
                    }
                }
            });

            reference.child("Comments").addValueEventListener(new ValueEventListener() {
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
                    ArticlesAdapter aa = new ArticlesAdapter(ArticlesActivity.this, list);
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