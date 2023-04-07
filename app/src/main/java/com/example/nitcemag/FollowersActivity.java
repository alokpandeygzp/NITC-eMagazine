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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowersActivity extends AppCompatActivity {

    AdapterFollow adapterFollow;
    RecyclerView recyclerView;

    ArrayList<String> akey=new ArrayList<>();
    ArrayList<UserDetails> userDetails=new ArrayList<>();
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));
        Intent intent=getIntent();
        String uid=intent.getStringExtra("uid");
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        recyclerView=findViewById(R.id.users_recyclerView);
        //set it's properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(FollowersActivity.this));


        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Followed");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                akey.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    for(DataSnapshot dsChild : ds.getChildren())
                    {
                        if(dsChild.child("email").getValue().equals(user.getEmail()))
                        {
                            akey.add(ds.getKey());
                        }
                    }
                }
                userDetails.clear();
                users(akey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

private void users(ArrayList<String > arr)
{
    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("UserType");
    ref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            userDetails.clear();
            for(DataSnapshot ds: snapshot.getChildren())
            {
                for(int i=0;i< arr.size();i++)
                {
                    if(arr.get(i).equals(ds.getKey()))
                    {
                        String role=ds.child("role").getValue().toString();
                        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference(role);
                        ref1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                for(DataSnapshot ds1: snapshot1.getChildren())
                                {
                                    UserDetails ud= ds1.getValue(UserDetails.class);
                                    if(ds1.getKey().equals(ds.getKey()))
                                    {
                                        userDetails.add(ud);

                                    }
                                }
                                adapterFollow = new AdapterFollow(FollowersActivity.this,userDetails);
                                //set adapter to recycler view
                                recyclerView.setAdapter(adapterFollow);
                                adapterFollow.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });}
}