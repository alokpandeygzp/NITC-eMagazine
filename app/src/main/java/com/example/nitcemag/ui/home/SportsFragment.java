package com.example.nitcemag.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
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
import android.widget.SearchView;
import android.widget.Toast;

import com.example.nitcemag.R;
import com.example.nitcemag.ui.home.Adapters.AdapterSports;
import com.example.nitcemag.ui.home.Models.ModelSports;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SportsFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    AdapterSports adapterSports;
    List<ModelSports> sportsList;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sports, container, false);

        firebaseAuth =FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Articles");

        //init recycler view
        recyclerView=view.findViewById(R.id.users_recyclerView);
        //set it's properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //init user list
        sportsList=new ArrayList<>();

        //get All User
        getAllArticles();
        return  view;
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
                    if(modelSports.getCategory().equals("Sports") && modelSports.getEditor()==1)
                    {
                        sportsList.add(modelSports);
                    }

                    //adapter
                    adapterSports = new AdapterSports(getActivity(),sportsList);
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