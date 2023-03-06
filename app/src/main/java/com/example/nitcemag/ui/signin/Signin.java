package com.example.nitcemag.ui.signin;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;


import com.example.nitcemag.R;
import com.example.nitcemag.ui.myArticles.MyArticlesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Signin extends Fragment {

    private SigninViewModel mViewModel;
    EditText username;
    EditText password;
    Button loginButton;
    FloatingActionButton floatingBtn;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        mViewModel = new ViewModelProvider(this).get(SigninViewModel.class);
        View root = inflater.inflate(R.layout.fragment_signin, container, false);
//        final TextView textView = root.findViewById(R.id.text_signin);
//
//        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//
//        floatingBtn=root.findViewById(R.id.fab);
//        floatingBtn.setVisibility(View.INVISIBLE);

        username = root.findViewById(R.id.editTextEmail);
        password = root.findViewById(R.id.editTextPassword);
        loginButton = root.findViewById(R.id.loginButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("alok@gmail.com") && password.getText().toString().equals("12345")) {
                    Toast.makeText(view.getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return root;
    }
}