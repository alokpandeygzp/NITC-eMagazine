package com.example.nitcemag;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.Toast;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    Menu menu;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_view);
        menu = navigationView.getMenu();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_myArticles,
                R.id.nav_postArticles,R.id.nav_signin, R.id.nav_signup)
                .setOpenableLayout(drawer)
                .build();


        Menu menu = navigationView.getMenu();


        menu.findItem(R.id.nav_signup).setVisible(true);
        menu.findItem(R.id.nav_signin).setVisible(true);

        menu.findItem(R.id.nav_reviewerDashboard).setVisible(false);
        menu.findItem(R.id.nav_editorDashboard).setVisible(false);
        menu.findItem(R.id.nav_myArticles).setVisible(false);
        menu.findItem(R.id.nav_postArticles).setVisible(false);

        

        setUtility();
        menu.findItem(R.id.nav_signup).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Intent i=new Intent(MainActivity.this,Signup.class);
                startActivity(i);
                drawer.closeDrawers();
                return false;
            }
        });

        menu.findItem(R.id.nav_signin).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Intent i=new Intent(MainActivity.this,Signin.class);
                startActivity(i);
                drawer.closeDrawers();
                return false;
            }
        });


        menu.findItem(R.id.nav_editorDashboard).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Intent i=new Intent(MainActivity.this,EditorDashboard.class);
                startActivity(i);
                drawer.closeDrawers();
                return false;
            }
        });

        menu.findItem(R.id.nav_reviewerDashboard).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Intent i=new Intent(MainActivity.this,ReviewerDashboard.class);
                startActivity(i);
                drawer.closeDrawers();
                return false;
            }
        });



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    public void setUtility()
    {
        

        if(user!=null && auth.getCurrentUser().isEmailVerified())
        {
            menu.findItem(R.id.nav_signup).setVisible(false);
            menu.findItem(R.id.nav_signin).setVisible(false);
            menu.findItem(R.id.nav_myArticles).setVisible(true);
            menu.findItem(R.id.nav_postArticles).setVisible(true);

            menu.getItem(1).setVisible(true);
            menu.getItem(0).setVisible(true);

            reference.child("UserType").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String role=snapshot.child("role").getValue().toString();
                    callingDatabase(role);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = (TextView) headerView.findViewById(R.id.editTextViewName);
            navUsername.setText("Hello, Guest !");
            TextView navEmail = (TextView) headerView.findViewById(R.id.editTextViewEmail);
            navEmail.setText("guest@nitc.ac.in");

            ImageView image=(ImageView)headerView.findViewById(R.id.imageView);
            Picasso.get().load(R.drawable.imgavatar).into(image);

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.logout)
        {
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            FirebaseAuth.getInstance().signOut();

            NavigationView navigationView = findViewById(R.id.nav_view);
            Menu menu = navigationView.getMenu();

            //To refresh the Action menu
            invalidateOptionsMenu();

            menu.findItem(R.id.nav_signup).setVisible(true);
            menu.findItem(R.id.nav_signin).setVisible(true);

            menu.findItem(R.id.nav_myArticles).setVisible(false);
            menu.findItem(R.id.nav_postArticles).setVisible(false);

            menu.findItem(R.id.nav_reviewerDashboard).setVisible(false);
            menu.findItem(R.id.nav_editorDashboard).setVisible(false);


            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = (TextView) headerView.findViewById(R.id.editTextViewName);
            navUsername.setText("Hello, Guest !");
            TextView navEmail = (TextView) headerView.findViewById(R.id.editTextViewEmail);
            navEmail.setText("guest@nitc.ac.in");

            ImageView image=(ImageView)headerView.findViewById(R.id.imageView);
            Picasso.get().load(R.drawable.imgavatar).into(image);

            return true;
        }
        else if(item.getItemId()==R.id.myProfile)
        {
            Intent i=new Intent(MainActivity.this,MyProfile.class);
            startActivity(i);

            return true;
        }
        else {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            return super.onOptionsItemSelected(item);
        }
    }





    public void callingDatabase(String roles)
    {
        System.out.println(roles);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.child(roles).child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(roles.equals("Editor")) {
                    menu.findItem(R.id.nav_editorDashboard).setVisible(true);
                }
                else if(roles.equals("Reviewer")){
                    menu.findItem(R.id.nav_reviewerDashboard).setVisible(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getDetails();
            }
        });



        ref.child(roles).child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String email= snapshot.child("email").getValue().toString();
                String img=""+snapshot.child("photo").getValue();


                View headerView = navigationView.getHeaderView(0);
                TextView textViewName = (TextView) headerView.findViewById(R.id.editTextViewName);
                ImageView image=(ImageView)headerView.findViewById(R.id.imageView);
                textViewName.setText(name);
                TextView textViewEmail = (TextView) headerView.findViewById(R.id.editTextViewEmail);
                textViewEmail.setText(email);
                if(!img.equals("")) {
                    try {
                        //if image is recieved
                        Picasso.get().load(img).into(image);
                    } catch (Exception e) {
                        //exception getting image
                        Picasso.get().load(R.drawable.ic_default_img).into(image);
                    }
                }
                else {
                    Picasso.get().load(R.drawable.imgavatar).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getDetails();
            }
        });
    }



    //Logout button to be visible or not
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user==null || (user!=null && !user.isEmailVerified()))
            menu.getItem(1).setVisible(false);

        if(user==null || (user!=null && !user.isEmailVerified()))
            menu.getItem(0).setVisible(false);


        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
