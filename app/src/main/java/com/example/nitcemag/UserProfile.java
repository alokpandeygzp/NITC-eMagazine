package com.example.nitcemag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitcemag.ui.ModelFav;
import com.example.nitcemag.ui.home.Adapters.AdapterSports;
import com.example.nitcemag.ui.home.Models.ModelSports;
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

public class UserProfile extends AppCompatActivity {
TextView name,em;
ImageView img;
String email;
RecyclerView recyclerView;
AdapterSports adapterSports;
FirebaseAuth auth=FirebaseAuth.getInstance();
FirebaseUser user=auth.getCurrentUser();
Button button;
List<ModelSports> sportsList;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));


        name=findViewById(R.id.username);
        em=findViewById(R.id.email);
        img=findViewById(R.id.image);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        em.setText(email);
        button=findViewById((R.id.button));
        recyclerView=findViewById(R.id.users_recyclerView);
        //set it's properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserProfile.this));
        //init user list
        sportsList=new ArrayList<>();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("UserType");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    if(ds.child("email").getValue().toString().equals(email))
                    {
                        String role=ds.child("role").getValue().toString();

                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(role);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot ds: snapshot.getChildren())
                                {
                                    if (ds.child("email").getValue().toString().equals(email))
                                    {
                                        String image=""+ds.child("photo").getValue();
                                        name.setText(""+ds.child("name").getValue());
                                        if(!image.equals("")) {
                                            try {
                                                //if image is recieved
                                                Picasso.get().load(image).into(img);
                                            } catch (Exception e) {
                                                //exception getting image
                                                Picasso.get().load(R.drawable.ic_default_img).into(img);
                                            }
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Followed");
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    if(ds.child("email").getValue().equals(email))
                    {
                        button.setBackgroundColor(Color.parseColor("#ffffff"));
                        button.setTextColor(Color.parseColor("#48b4e0"));
                        button.setText("Following");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int flag=1;
                        for(DataSnapshot ds: snapshot.getChildren())
                        {
                            if(ds.child("email").getValue().equals(email))
                            {
                                button.setBackgroundColor(Color.parseColor("#48b4e0"));
                                button.setTextColor(Color.parseColor("#ffffff"));
                                button.setText("Follow");
                                ds.getRef().removeValue();
                                flag=0;
                                break;
                            }
                        }
                        if(flag==1)
                        {
                            String k = reference.child(user.getUid()).push().getKey();
                            ModelFollow mc = new ModelFollow(email,k);
                            reference.child(user.getUid()).child(k).setValue(mc);
                            button.setBackgroundColor(Color.parseColor("#ffffff"));
                            button.setTextColor(Color.parseColor("#48b4e0"));
                            button.setText("Following");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        getAllArticles();
    }

    private void getAllArticles()
    {
        //get current user
        FirebaseUser fUser= FirebaseAuth.getInstance().getCurrentUser();
        //get path of database named "users" containing users info
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Articles");

        //get all
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sportsList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    ModelSports modelSports =ds.getValue(ModelSports.class);
                    //
                    if(modelSports.getEmail().equals(email) && modelSports.getEditor()==1)
                    {
                        sportsList.add(modelSports);
                    }

                    //adapter
                    adapterSports = new AdapterSports(UserProfile.this,sportsList);
                    //set adapter to recycler view
                    recyclerView.setAdapter(adapterSports);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}