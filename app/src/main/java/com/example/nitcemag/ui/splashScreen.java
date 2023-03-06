package com.example.nitcemag.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nitcemag.MainActivity;
import com.example.nitcemag.R;

public class splashScreen extends AppCompatActivity {

    TextView title;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        title = findViewById(R.id.textViewSplash);
        image = findViewById(R.id.imageViewSplash);

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_animation);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottom_animation);

        title.startAnimation(animation1);
        image.startAnimation(animation2);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(splashScreen.this, MainActivity.class);
            startActivity(i);
            finish();

        },5000);
    }
}
