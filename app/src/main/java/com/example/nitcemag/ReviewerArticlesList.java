package com.example.nitcemag;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcemag.ui.postArticles.UserArticles;
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
    ArrayList<UserArticles> list;

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
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                   // System.out.println(dataSnapshot);
                    UserArticles articles = dataSnapshot.getValue(UserArticles.class);
                    if(articles!=null && articles.reviewer==0 && articles.editor==0)
                        list.add(articles);
                }
                reviewerArticlesListAdapter =new ReviewerArticlesListAdapter(ReviewerArticlesList.this,list);
                recyclerView.setAdapter(reviewerArticlesListAdapter);

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