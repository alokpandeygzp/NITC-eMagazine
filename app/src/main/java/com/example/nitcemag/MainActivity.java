package com.example.nitcemag;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.firebase.database.FirebaseDatabase;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_reviewedArticles, R.id.nav_myArticles,
                R.id.nav_postArticles,R.id.nav_signin, R.id.nav_signup)
                .setOpenableLayout(drawer)
                .build();

        Menu menu = navigationView.getMenu();

        setUtility();
        menu.findItem(R.id.nav_signup).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Intent i=new Intent(MainActivity.this,Signup.class);
                startActivity(i);

                return false;
            }
        });

        menu.findItem(R.id.nav_signin).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Intent i=new Intent(MainActivity.this,Signin.class);
                startActivity(i);

                return false;
            }
        });

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    public void setUtility()
    {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();

        if(user!=null)
        {
            menu.findItem(R.id.nav_signup).setVisible(false);
            menu.findItem(R.id.nav_signin).setVisible(false);
            menu.getItem(1).setVisible(true);

            reference.child("User").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child("name").getValue().toString();
                    String email= snapshot.child("email").getValue().toString();


                    System.out.println(name+"****************************");
                    System.out.println(email+"****************************");


                    View headerView = navigationView.getHeaderView(0);
                    TextView textViewName = (TextView) headerView.findViewById(R.id.editTextViewName);
                    textViewName.setText(name);
                    TextView textViewEmail = (TextView) headerView.findViewById(R.id.editTextViewEmail);
                    textViewEmail.setText(email);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    error.getDetails();
                }
            });
        }
        else {
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = (TextView) headerView.findViewById(R.id.editTextViewName);
            navUsername.setText("Hello, Guest !");
            TextView navEmail = (TextView) headerView.findViewById(R.id.editTextViewEmail);
            navEmail.setText("guest@nitc.ac.in");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myProfile:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.logout:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                FirebaseAuth.getInstance().signOut();
                item.setVisible(false);

                NavigationView navigationView = findViewById(R.id.nav_view);
                Menu menu = navigationView.getMenu();
                menu.findItem(R.id.nav_signup).setVisible(true);
                menu.findItem(R.id.nav_signin).setVisible(true);



                View headerView = navigationView.getHeaderView(0);
                TextView navUsername = (TextView) headerView.findViewById(R.id.editTextViewName);
                navUsername.setText("Hello, Guest !");
                TextView navEmail = (TextView) headerView.findViewById(R.id.editTextViewEmail);
                navEmail.setText("guest@nitc.ac.in");



                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user==null)
            menu.getItem(1).setVisible(false);


        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
