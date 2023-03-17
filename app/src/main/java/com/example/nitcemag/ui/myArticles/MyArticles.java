package com.example.nitcemag.ui.myArticles;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nitcemag.R;
import com.example.nitcemag.ui.home.Adapters.AdapterSports;
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


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState) {
        View view = inflater.inflate(R.layout.fragment_myarticles, container, false);

        recyclerView = view.findViewById(R.id.myArticles_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<UserArticles> myArticleList = new ArrayList<>();

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
}


