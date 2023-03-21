package com.example.nitcemag.ui.postArticles;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nitcemag.MyProfile;
import com.example.nitcemag.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class PostArticles extends AppCompatActivity {

   // FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String storagePath="Article_Imgs/";
    Uri image_uri;
    String key;
    String image;
    EditText titles;
    EditText name;
    TextView em;
    EditText article_descp;
    private  static  final int CAMERA_REQUEST_CODE=100;
    private  static  final int STORAGE_REQUEST_CODE=200;
    private  static  final int IMAGE_PICK_CAMERA_CODE=400;
    private  static  final int IMAGE_PICK_GALLERY_CODE=300;
    String cameraPermission[];
    String storagePermission[];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.fragment_post_articles);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));
        storageReference = FirebaseStorage.getInstance().getReference();
        cameraPermission=new String[]{android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        key=reference.child("Articles").push().getKey();
        Spinner category=findViewById(R.id.category);
        String[] cat={"Sports","Study","Events","Notice"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PostArticles.this, android.R.layout.simple_spinner_item, cat);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        category.setAdapter(adapter);

        final String[] article_cat = new String[1];

        titles = findViewById(R.id.textTitle);
        name= findViewById(R.id.textName);
        article_descp= findViewById(R.id.text_descp);
        em=findViewById(R.id.textEmail);
        em.setText(auth.getCurrentUser().getEmail());
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value=adapterView.getItemAtPosition(i).toString();
                article_cat[0] =value;
                // Toast.makeText(view.getContext(), value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });



        Button imagebtn=findViewById(R.id.img_btn);

        imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                //  imagebtn.setOnClickListener(view -> mGetContent.launch("image/*"));
                image="";
                showImagePicDialog();
            }
        });

        Button submit=findViewById(R.id.submit_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String author=name.getText().toString();
                String title= titles.getText().toString();
                String description=article_descp.getText().toString();
                String category=article_cat[0];
                String email=auth.getCurrentUser().getEmail();
                // Toast.makeText(view.getContext(), email[1], Toast.LENGTH_SHORT).show();
                if(author.isEmpty() || title.isEmpty()  || description.isEmpty() || category.isEmpty())
                {
                    Toast.makeText(view.getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(view.getContext(), "Your Article send successfully to Publish", Toast.LENGTH_SHORT).show();
                    article_by_userFirebase(view,author,email,title,description,category,image,key);
                }
                // Toast.makeText(view.getContext(), "Your Article send successfully to Publish", Toast.LENGTH_SHORT).show();


            }
        });

    }


        @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        storageReference = FirebaseStorage.getInstance().getReference();
        cameraPermission=new String[]{android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        key=reference.child("Articles").push().getKey();
        Spinner category;
        String[] cat={"Sports","Academic","Events","Notice"};
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_articles, container, false);
        category= view.findViewById(R.id.category);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cat);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        category.setAdapter(adapter);

        final String[] article_cat = new String[1];

        titles = view.findViewById(R.id.textTitle);
        name= view.findViewById(R.id.textName);
        article_descp= view.findViewById(R.id.text_descp);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               String value=adapterView.getItemAtPosition(i).toString();
               article_cat[0] =value;
               // Toast.makeText(view.getContext(), value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });



        Button imagebtn=view.findViewById(R.id.img_btn);

        imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
              //  imagebtn.setOnClickListener(view -> mGetContent.launch("image/*"));
                image="";
                showImagePicDialog();
            }
        });

        Button submit=view.findViewById(R.id.submit_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String author=name.getText().toString();
                String title= titles.getText().toString();
                String description=article_descp.getText().toString();
                String category=article_cat[0];
                String email=auth.getCurrentUser().getEmail();
               // Toast.makeText(view.getContext(), email[1], Toast.LENGTH_SHORT).show();
                if(author.isEmpty() || title.isEmpty()  || description.isEmpty() || category.isEmpty())
                {
                    Toast.makeText(view.getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(view.getContext(), "Your Article send successfully to Publish", Toast.LENGTH_SHORT).show();
                    article_by_userFirebase(view,author,email,title,description,category,image,key);
                }
               // Toast.makeText(view.getContext(), "Your Article send successfully to Publish", Toast.LENGTH_SHORT).show();


            }
        });

    return view;
    }

    private void article_by_userFirebase(View view,String author,String email,String title,String description,String category,String img,String key)
    {
        UserArticles userarticles= new UserArticles(author,email,title,description,category,img,key);
        reference.child("Articles").child(key).setValue(userarticles);
        Intent intent = new Intent(PostArticles.this, PostArticles.class);
        finish();
        startActivity(intent);
    }

    private boolean checkStoragePermission()
    {
        boolean result= ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission()
    {
        requestPermissions( storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission()
    {
        boolean result= ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1= ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission()
    {
        requestPermissions(cameraPermission, CAMERA_REQUEST_CODE);
    }

    private void showImagePicDialog() {

        String options[]={"Camera", "Gallery"};
        //alert
        AlertDialog.Builder builder=new AlertDialog.Builder((this));
        //set builder
        builder.setTitle("Pick Image from");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //handle dialog item click
                if(i == 0)
                {
//                    //camera
                    if(!checkCameraPermission())
                    {
                        Toast.makeText(PostArticles.this, "checking camera", Toast.LENGTH_SHORT).show();
                        requestCameraPermission();
                    }
                    else
                    {
                        Toast.makeText(PostArticles.this, "checking camera", Toast.LENGTH_SHORT).show();
                        pickFromCamera();
                    }
                }
                else if(i==1)
                {

//                    Gallery
                    if(!checkStoragePermission())
                    {
                        requestStoragePermission();
                        Toast.makeText(PostArticles.this, "storage permission", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        pickFromGallery();
                    }
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }

    @SuppressLint("MissingSuperCall")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        //permissions enabled
                        pickFromCamera();
                    } else {
                        //permissions denied
                        Toast.makeText(this, "Please Enable Camera and Storage Permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        //permissions enabled
                        pickFromGallery();
                    } else {
                        //permissions denied
                        Toast.makeText(this, "Please Enable Storage Permission", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == IMAGE_PICK_GALLERY_CODE)
            {
                image_uri=data.getData();
                uploadProfileCoverPhoto(image_uri);
            }
            if(requestCode == IMAGE_PICK_CAMERA_CODE)
            {
                uploadProfileCoverPhoto(image_uri);
            }
        }

    }


    private void uploadProfileCoverPhoto(Uri uri)
    {
        //path and name of image to be stored in firebase storage
        String filePathAndName=storagePath+"_"+key;
        StorageReference storageReference2= storageReference.child(filePathAndName);
        databaseReference =FirebaseDatabase.getInstance().getReference("Articles");
        storageReference2.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();

                if(uriTask.isSuccessful())
                {
                    //image uploaded
                    //add or update url in user's database
                    Toast.makeText(PostArticles.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    image=downloadUri.toString();
                }
                else
                {
                    //image error
                    Toast.makeText(PostArticles.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostArticles.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pickFromGallery()
    {
        Intent galleryIntent= new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera()
    {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp DEscription");
        //put image uri
        image_uri=PostArticles.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //intent to start camera
        Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    }