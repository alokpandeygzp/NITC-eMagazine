package com.example.nitcemag.ui.myArticles;

import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.nitcemag.R;
import com.example.nitcemag.ui.home.Adapters.AdapterSports;
import com.example.nitcemag.ui.home.Models.ModelSports;
import com.example.nitcemag.ui.postArticles.UserArticles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyArticles extends Fragment {

    private MyArticlesViewModel mViewModel;
    RecyclerView recyclerView;
    AdapterMyArticles adapterMyArticles;
    FirebaseUser user;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    List<UserArticles> myArticleList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState) {
        View view = inflater.inflate(R.layout.fragment_myarticles, container, false);

        recyclerView = view.findViewById(R.id.myArticles_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                    adapterMyArticles = new AdapterMyArticles(getActivity(), myArticleList);
                    recyclerView.setAdapter(adapterMyArticles);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //nuinflater.inflate(R.menu.search, menu);

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


        super.onCreateOptionsMenu(menu, inflater);
    }


    public void onCreate(Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
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
                    if(modelUser.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                            modelUser.getAuthor().toLowerCase().contains(query.toLowerCase()))
                    {
                        myArticleList.add(modelUser);
                    }


                    //adapter
                    adapterMyArticles= new AdapterMyArticles(getActivity(),myArticleList);
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


