package com.example.nitcemag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.nitcemag.ui.postArticles.UserArticles;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RejectList extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    RejectListAdapter rejectListAdapter;
    ArrayList<UserArticles> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_rejectlist);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));



        recyclerView=findViewById(R.id.articlesList);
        database= FirebaseDatabase.getInstance().getReference("Articles");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();


        recyclerView.setAdapter(rejectListAdapter);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    System.out.println(dataSnapshot);
                    UserArticles articles= dataSnapshot.getValue(UserArticles.class);


                    if(articles!=null && articles.reviewer==-1)
                        list.add(articles);

                    rejectListAdapter =new RejectListAdapter(RejectList.this,list);
                    recyclerView.setAdapter(rejectListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}