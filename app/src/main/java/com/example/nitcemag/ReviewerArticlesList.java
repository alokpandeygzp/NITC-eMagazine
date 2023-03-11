package com.example.nitcemag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReviewerArticlesList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    ReviewerArticlesListAdapter reviewerArticlesListAdapter;
    ArrayList<Articles> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_reviewer_articles_list);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));



        recyclerView=findViewById(R.id.articlesList);
        database= FirebaseDatabase.getInstance().getReference("Articles");

//        System.out.println(database+"******************************************");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();


        recyclerView.setAdapter(reviewerArticlesListAdapter);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    System.out.println(dataSnapshot);
                    Articles articles= dataSnapshot.getValue(Articles.class);


                    if(articles!=null && articles.reviewer==0)
                        list.add(articles);

                    reviewerArticlesListAdapter =new ReviewerArticlesListAdapter(ReviewerArticlesList.this,list);
                    recyclerView.setAdapter(reviewerArticlesListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}