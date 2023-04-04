package com.example.nitcemag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitcemag.ui.ModelFav;
import com.example.nitcemag.ui.ModelLike;
import com.example.nitcemag.ui.home.Models.ModelSports;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArticlesActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    List<ModelComment> list= new ArrayList<>();
    DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
    String name,key,email;
    LinearLayout ll;
    Bitmap bitmap;
    ModelSports ms;
    TextView tt, desc, author,lcount,auth1;
    ImageView img,send,like,star;
    TextInputEditText com;
    RecyclerView rv;
    TextToSpeech t1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_articles);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));


         Intent intent= getIntent();
         key=intent.getStringExtra("key");
         t1= new TextToSpeech(this, new TextToSpeech.OnInitListener() {
             @Override
             public void onInit(int i) {
                if(i!=TextToSpeech.ERROR)
                {
                    t1.setLanguage(Locale.ENGLISH);
                }
             }
         });

         tt= findViewById(R.id.title);
         img=findViewById(R.id.image);
         desc=findViewById(R.id.description);
         author=findViewById(R.id.author);
         send=findViewById(R.id.imgbutton);
         com=findViewById(R.id.comment);
         rv=findViewById(R.id.comments_recyclerView);
         like=findViewById(R.id.like);
         lcount=findViewById(R.id.lcount);
         star=findViewById(R.id.star);
         ll=findViewById(R.id.lLayout);
         rv.setHasFixedSize(true);
         rv.setLayoutManager(new LinearLayoutManager(ArticlesActivity.this));
         firebaseAuth=FirebaseAuth.getInstance();



        //get all
        reference.child("Articles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot ds:snapshot.getChildren())
                {
                    ModelSports modelSports =ds.getValue(ModelSports.class);
                    //
                   if(ds.getKey().equals(key))
                    {
                        ms=modelSports;
                        tt.setText(modelSports.getTitle());
                        desc.setText(modelSports.getDescription());
                        author.setText(modelSports.getAuthor());
                        String image=modelSports.getImage();
                        email=modelSports.getEmail();
                        try
                        {
                            Picasso.get().load(image).placeholder(R.drawable.newspaper).into(img);
                        }
                        catch (Exception e)
                        {           }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user==null ) {
            com.setVisibility(View.GONE);
            send.setVisibility(View.GONE);
            rv.setVisibility(View.GONE);
            like.setVisibility(View.GONE);
            lcount.setVisibility(View.GONE);
            star.setVisibility(View.GONE);
        }
        else {
            author.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(user.getEmail().equals(email))
                    {
                        Intent intent= new Intent(ArticlesActivity.this,MyProfile.class);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent= new Intent(ArticlesActivity.this,UserProfile.class);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }

                }
            });

            reference.child("PostedArticles").child("Favourites").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.getKey().equals(key)) {
                            try
                            {
                                Picasso.get().load(R.drawable.star).placeholder(R.drawable.unstar).into(star);
                            }
                            catch (Exception e)
                            {

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reference.child("PostedArticles").child("Favourites").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int flag=1;
                            for(DataSnapshot ds:snapshot.getChildren())
                            {
                                if(ds.child("article").getValue().equals(key))
                                {
                                    try
                                    {
                                        Picasso.get().load(R.drawable.unstar).placeholder(R.drawable.star).into(star);

                                    }
                                    catch (Exception e)
                                    {

                                    }
                                    ds.getRef().removeValue();
                                    flag=0;
                                    break;
                                }
                            }
                            if(flag==1)
                            {
                                String k = reference.child("PostedArticles").child("Favourites").child(user.getUid()).push().getKey();
                                ModelFav mc = new ModelFav( key);
                                reference.child("PostedArticles").child("Favourites").child(user.getUid()).child(key).setValue(mc);
                                try
                                {
                                    Picasso.get().load(R.drawable.star).placeholder(R.drawable.unstar).into(star);
                                }
                                catch (Exception e)
                                {

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            // Name needed for commenting on the article after Signin only
            reference.child("PostedArticles").child("Likes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        if(ds.getKey().equals(key))
                        {
                            reference.child("PostedArticles").child("Likes").child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {
                                    int count=0;
                                    int flag=0;
                                    for(DataSnapshot ds:snapshot.getChildren())
                                    {
                                        count=count+1;
                                        if(ds.child("email").getValue().toString().equals(firebaseAuth.getCurrentUser().getEmail()) && ds.child("article").getValue().equals(key))
                                        {
                                            flag=1;
                                            try
                                            {
                                                Picasso.get().load(R.drawable.img_1).placeholder(R.drawable.img_2).into(like);
                                            }
                                            catch (Exception e)
                                            {

                                            }
                                        }
                                    }
                                    if(flag==1)
                                    {
                                        if(count >1)
                                            lcount.setText("You and "+Integer.toString(count-1)+" other");
                                        else if(count==1)
                                            lcount.setText("You");
                                    }
                                    else
                                    {
                                        if(count >1)
                                            lcount.setText(Integer.toString(count)+" likes");
                                        else if(count==1)
                                            lcount.setText(Integer.toString(count)+" like");
                                        else
                                            lcount.setText("");
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            break;
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reference.child("PostedArticles").child("Likes").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int flag=1;
                            for(DataSnapshot ds:snapshot.getChildren())
                            {
                                if(ds.child("email").getValue().toString().equals(firebaseAuth.getCurrentUser().getEmail()) && ds.child("article").getValue().equals(key))
                                {
                                    try
                                    {
                                        Picasso.get().load(R.drawable.img_2).placeholder(R.drawable.img_1).into(like);

                                    }
                                    catch (Exception e)
                                    {

                                    }
                                    ds.getRef().removeValue();
                                    flag=0;
                                    break;
                                }
                            }
                            if(flag==1)
                            {
                                String k = reference.child("PostedArticles").child("Likes").child(key).push().getKey();
                                ModelLike mc = new ModelLike(firebaseAuth.getCurrentUser().getEmail().toString(), key, name, k);
                                reference.child("PostedArticles").child("Likes").child(key).child(k).setValue(mc);
                                try
                                {
                                    Picasso.get().load(R.drawable.img_1).placeholder(R.drawable.img_2).into(like);
                                }
                                catch (Exception e)
                                {

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

            reference.child("UserType").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String roles=snapshot.child("role").getValue().toString();

                    reference.child(roles).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds:snapshot.getChildren())
                            {
                                if(ds.getKey().equals(firebaseAuth.getCurrentUser().getUid().toString()))
                                {
                                    name=ds.child("name").getValue().toString();
                                }
                            }
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


            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String cmnt = com.getText().toString().trim();
                    cmnt.replaceAll("\n", " ");
                    com.setText("");
                    com.clearFocus();
                    if (cmnt.equals("")) {
                        Toast.makeText(ArticlesActivity.this, "Empty", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        reference = FirebaseDatabase.getInstance().getReference();
                        String k = reference.child("PostedArticles").child("Comments").child(key).push().getKey();
                        ModelComment mc = new ModelComment(firebaseAuth.getCurrentUser().getEmail().toString(), key, cmnt, name, k);
                        reference.child("PostedArticles").child("Comments").child(key).child(k).setValue(mc);
                    }
                }
            });

            reference.child("PostedArticles").child("Comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds: snapshot.getChildren())
                    {
                        if(ds.getKey().equals(key))
                        {
                            reference.child("PostedArticles").child("Comments").child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    list.clear();
                                    for (DataSnapshot ds : snapshot.getChildren())
                                    {
                                        ModelComment mc = ds.getValue(ModelComment.class);
                                        list.add(mc);
                                    }
                                    //adapter
                                    ArticlesAdapter aa = new ArticlesAdapter(ArticlesActivity.this, list);
                                    //set adapter to recycler view

                                    rv.setAdapter(aa);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        t1.stop();
        finish();
    }

    int convertwidth, convertheight;
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        FirebaseUser user= firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            reference.child("Editor").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        if(ds.getKey().equals(user.getUid().toString()))
                        {
                            getMenuInflater().inflate(R.menu.edit, menu);
                            MenuItem i= menu.findItem(R.id.edit);
                            i.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                                    Intent intent= new Intent(ArticlesActivity.this,EditorActionEdit.class);
                                    intent.putExtra("key",key);
                                    startActivity(intent);
                                    finish();
                                    return true;
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

        getMenuInflater().inflate(R.menu.speech, menu);
        MenuItem sitem= menu.findItem(R.id.speech);
        sitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
               String text = desc.getText().toString();
               t1.speak(text,TextToSpeech.QUEUE_FLUSH, null);

               return false;
            }
        });

        getMenuInflater().inflate(R.menu.download, menu);
        MenuItem item= menu.findItem(R.id.download);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                bitmap=loadBitmap(ll,ll.getWidth(),ll.getHeight());
                createPdf();
                PdfDocument pdfDocument = new PdfDocument();
                Paint paint= new Paint();
                PdfDocument.PageInfo pageInfo= new PdfDocument.PageInfo.Builder(convertwidth,convertheight,1).create();
                PdfDocument.Page page= pdfDocument.startPage(pageInfo);
                Canvas canvas= page.getCanvas();
                paint.setColor(Color.rgb(0,0,0));
                bitmap=Bitmap.createScaledBitmap(bitmap,convertwidth,convertheight, true);
                canvas.drawBitmap(bitmap,0,0,null);
                pdfDocument.finishPage(page);
                File file = new File(Environment.getExternalStorageDirectory()+"/Download",ms.getTitle()+".pdf");
                try {
                    Toast.makeText(ArticlesActivity.this, "Saving", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        pdfDocument.writeTo(Files.newOutputStream(file.toPath()));
                    }
                }
                catch (IOException e)
                {
                    Toast.makeText(ArticlesActivity.this, "Error"+e, Toast.LENGTH_LONG).show();
                    System.out.println(e);
                }
                pdfDocument.close();
                openPdf();
                return true;
            }
        });
        return true;
    }

    private  void openPdf()
    {
        File file = new File(Environment.getExternalStorageDirectory()+"/Download",ms.getTitle().trim()+".pdf");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (file.exists()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //Uri uri = Uri.fromFile(file);
                Uri uri = FileProvider.getUriForFile(ArticlesActivity.this, BuildConfig.APPLICATION_ID + ".provider",file);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    System.out.println("Error "+e);
                    Toast.makeText(this, "No application for pdf view", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void createPdf()
    {
        WindowManager windowManager=(WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics= new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float width = displayMetrics.widthPixels;
        float height= displayMetrics.heightPixels;
        convertwidth= (int) width;
        convertheight=(int) height;
    }
    private Bitmap loadBitmap(View view, int width, int height)
    {
        Bitmap b=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas= new Canvas(b);
        view.draw(canvas);
        return b;
    }

}