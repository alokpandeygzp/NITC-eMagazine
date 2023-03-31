package com.example.nitcemag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.nitcemag.ui.home.Adapters.AdapterSports;
import com.example.nitcemag.ui.home.Models.ModelSports;
import com.example.nitcemag.ui.postArticles.UserArticles;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyFavourites extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterSports adapterMyArticles;
    FirebaseUser user;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    List<ModelSports> myArticleList;
    ArrayList<String> akey= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_myfavourites);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));

        recyclerView = findViewById(R.id.myArticles_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyFavourites.this));
        myArticleList = new ArrayList<>();

        user = auth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PostedArticles").child("Favourites").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                myArticleList.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    akey.add(ds.getKey());
                }
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Articles");
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        for(DataSnapshot ds:snapshot1.getChildren())
                        {
                            ModelSports userArticles1 = ds.getValue(ModelSports.class);
                            for(int i=0;i<akey.size();i++)
                            {
                                if(akey.get(i).equals(ds.getKey()))
                                {
                                    myArticleList.add(userArticles1);
                                    break;
                                }
                            }
                        }
                        adapterMyArticles = new AdapterSports(MyFavourites.this, myArticleList);
                        recyclerView.setAdapter(adapterMyArticles);
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




        }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

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
        return false;
    }
    private void searchUsers(String query)
    {
        //get path of database named "users" containing users info

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Articles");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                for(DataSnapshot ds:snapshot1.getChildren())
                {
                    ModelSports userArticles1 = ds.getValue(ModelSports.class);
                    for(int i=0;i<akey.size();i++)
                    {
                        if(akey.get(i).equals(ds.getKey()))
                        {
                            myArticleList.add(userArticles1);
                            break;
                        }
                    }
                }
                adapterMyArticles = new AdapterSports(MyFavourites.this, myArticleList);
                recyclerView.setAdapter(adapterMyArticles);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Articles");
        //get all

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myArticleList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    ModelSports modelUser =ds.getValue(ModelSports.class);
                    //get all searched users except currently signed in user
                    for(int i=0;i<akey.size();i++)
                    {
                        if(akey.get(i).equals(ds.getKey()))
                        {
                            if((modelUser.getTitle().toLowerCase().contains(query.toLowerCase()) || modelUser.getAuthor().toLowerCase().contains(query.toLowerCase()) ))
                            {
                                myArticleList.add(modelUser);
                            }

                        }
                    }


                    //adapter
                    adapterMyArticles= new AdapterSports(MyFavourites.this,myArticleList);
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