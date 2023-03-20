package com.example.nitcemag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.nitcemag.ui.postArticles.UserArticles;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyArticles extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterMyArticles adapterMyArticles;
    FirebaseUser user;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    List<UserArticles> myArticleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_my_articles);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));


        recyclerView = findViewById(R.id.myArticles_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyArticles.this));
        myArticleList = new ArrayList<>();

        user = auth.getCurrentUser();

        String curr_user = user.getEmail().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Articles");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myArticleList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserArticles userArticles1 = snapshot1.getValue(UserArticles.class);
                    if (userArticles1.getEmail().equals(curr_user)) {
                        // String article_name=userArticles1.getArticle_title().toString();
                        if(userArticles1.getEditor()==1)
                            myArticleList.add(userArticles1);
                    }
                    adapterMyArticles = new AdapterMyArticles(MyArticles.this, myArticleList);
                    recyclerView.setAdapter(adapterMyArticles);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);

        //search View
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        //search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //called when ever user press search button
                //if search query is not empty then search
                if (!TextUtils.isEmpty(s.trim())) {
                    //Search text contains text, search it
                    searchUsers(s);
                } else {
                    //search text empty, get all users
                    //getAllArticles();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //called whenever user press any single letter
                //if search query is not empty then search
                if (!TextUtils.isEmpty(s.trim())) {
                    //Search text contains text, search it
                    searchUsers(s);
                } else {
                    //search text empty, get all users
                    //getAllArticles();
                }
                return false;
            }
        });
    }
    private void searchUsers(String query)
    {
        //get path of database named "users" containing users info
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Articles");
        //get all
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myArticleList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    UserArticles modelUser =ds.getValue(UserArticles.class);
                    //get all searched users except currently signed in user
                    if((modelUser.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                            modelUser.getAuthor().toLowerCase().contains(query.toLowerCase()) )&& modelUser.getEditor()==1)
                    {
                        myArticleList.add(modelUser);
                    }


                    //adapter
                    adapterMyArticles= new AdapterMyArticles(MyArticles.this,myArticleList);
                    //refresh adapter
                    adapterMyArticles.notifyDataSetChanged();
                    //set adapter to recycler view
                    recyclerView.setAdapter(adapterMyArticles);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}