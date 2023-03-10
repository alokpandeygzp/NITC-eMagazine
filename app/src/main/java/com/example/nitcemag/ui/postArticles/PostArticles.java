package com.example.nitcemag.ui.postArticles;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.nitcemag.MyProfile;
import com.example.nitcemag.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class PostArticles extends Fragment {

   // FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    DatabaseReference databaseReference;
    StorageReference storageReference;
    String storagePath="Article_Imgs/";
    Uri image_uri;
    String key;
    String image;
    private  static  final int CAMERA_REQUEST_CODE=100;
    private  static  final int STORAGE_REQUEST_CODE=200;
    private  static  final int IMAGE_PICK_CAMERA_CODE=400;
    private  static  final int IMAGE_PICK_GALLERY_CODE=300;
    private static  final int PICK_IMAGE_REQUEST = 300;
    private PostArticlesViewModel mViewModel;
    String cameraPermission[];
    String storagePermission[];

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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, cat);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        category.setAdapter(adapter);

        final String[] article_cat = new String[1];
        EditText titles;
        EditText name;
        EditText emails;
        EditText article_descp;
        titles = view.findViewById(R.id.textTitle);
        name= view.findViewById(R.id.textName);
        emails= view.findViewById(R.id.textEmail);
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
        ImageView img=view.findViewById(R.id.imageView);

        imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
              //  imagebtn.setOnClickListener(view -> mGetContent.launch("image/*"));
                showImagePicDialog();
            }
        });

        Button submit=view.findViewById(R.id.submit_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String author=name.getText().toString();
                String title= titles.getText().toString();
                String email=emails.getText().toString();
                String description=article_descp.getText().toString();
                String category=article_cat[0];
                String emails[] = email.split("@");

               // Toast.makeText(view.getContext(), email[1], Toast.LENGTH_SHORT).show();
                if(author.isEmpty() || title.isEmpty() || email.isEmpty() || description.isEmpty() || category.isEmpty())
                {
                    Toast.makeText(view.getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (emails[1].equals("nitc.ac.in")==false)
                {
                    Toast.makeText(view.getContext(), "Enter Nitc email id", Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(view.getContext(), "Your Article send successfully to Publish", Toast.LENGTH_SHORT).show();
                    article_by_userFirebase(author,email,title,description,category,image,key);

                }
               // Toast.makeText(view.getContext(), "Your Article send successfully to Publish", Toast.LENGTH_SHORT).show();


            }
        });

    return view;
    }

    private void article_by_userFirebase(String author,String email,String title,String description,String category,String img,String key)
    {
        UserArticles userarticles= new UserArticles(author,email,title,description,category,img,key);
        Toast.makeText(getContext(), ""+img, Toast.LENGTH_SHORT).show();
        reference.child("Articles").child(key).setValue(userarticles);
    }

    private boolean checkStoragePermission()
    {
        boolean result= ContextCompat.checkSelfPermission(getContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission()
    {
        requestPermissions( storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission()
    {
        boolean result= ContextCompat.checkSelfPermission(getContext(),android.Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1= ContextCompat.checkSelfPermission(getContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        AlertDialog.Builder builder=new AlertDialog.Builder((getContext()));
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
                        requestCameraPermission();
                    }
                    else
                    {
                        pickFromCamera();
                    }
                }
                else if(i==1)
                {

//                    Gallery
                    if(!checkStoragePermission())
                    {
                        requestStoragePermission();
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
                        Toast.makeText(getContext(), "Please Enable Camera and Storage Permission", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Please Enable Storage Permission", Toast.LENGTH_SHORT).show();
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
            if(requestCode == PICK_IMAGE_REQUEST)
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
                    image=downloadUri.toString();
                }
                else
                {
                    //image error
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        //put image uri
        image_uri=getContext() .getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //intent to start camera
        Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }




    }