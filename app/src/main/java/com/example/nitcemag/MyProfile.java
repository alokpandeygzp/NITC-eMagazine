package com.example.nitcemag;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcemag.ui.home.Adapters.AdapterSports;
import com.example.nitcemag.ui.home.Models.ModelSports;
import com.example.nitcemag.ui.postArticles.UserArticles;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyProfile extends AppCompatActivity {

    TextView change_password,name,email;
    ImageView img;
    FirebaseUser user;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Button fab;
    StorageReference storageReference;
    //path where image of user profile and cover will be stored
    String storagePath="Users_Profile_Cover_Imgs/";
    Uri image_uri;
    RecyclerView recyclerView;
    AdapterSports adapterSports;

    AlertDialog dialog;
    EditText currPass, newPass;
    Button submit;
    TextView followingCount,articleCount,followerCount;

      //permission constants
    private  static  final int CAMERA_REQUEST_CODE=100;
    private  static  final int STORAGE_REQUEST_CODE=200;
    private  static  final int IMAGE_PICK_CAMERA_CODE=400;
    private  static  final int IMAGE_PICK_GALLERY_CODE=300;
    private static  final int PICK_IMAGE_REQUEST = 300;
    //Arrays of permission to be requested
    String cameraPermission[];
    String storagePermission[];
    List<ModelSports> sportsList;

    final ArrayList<String> akey= new ArrayList<>();

    DatabaseReference databaseReference;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_myprofile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.side_nav_bar));


        user=auth.getCurrentUser();
        name=findViewById(R.id.username);
        email=findViewById(R.id.email);
        img=findViewById(R.id.image);
        fab=findViewById(R.id.fab);
        String uid=auth.getUid();
        followingCount=findViewById(R.id.followingCount);
        articleCount=findViewById(R.id.ArticleCount);
        followerCount=findViewById(R.id.followerCount);


        recyclerView=findViewById(R.id.users_recyclerView);
        //set it's properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyProfile.this));
        //init user list
        sportsList=new ArrayList<>();




        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        cameraPermission=new String[]{android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Followed").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                myArticleList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    akey.add(ds.child("email").getValue().toString());
                    followingCount.setText(""+akey.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        akey.clear();
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Followed");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counting=0;
                for (DataSnapshot ds : snapshot.getChildren()) {

                    for(DataSnapshot dsChild : ds.getChildren())
                    {
                        if(dsChild.child("email").getValue().equals(user.getEmail())) {
                            counting++;
                            akey.add(dsChild.child("email").getValue().toString());
                            followerCount.setText("" +counting);
                            System.out.println("Final:-"+dsChild.child("email").getValue());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        String curr_user = user.getEmail();
        List<UserArticles> myArticleList = new ArrayList<>();

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Articles");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myArticleList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserArticles userArticles1 = snapshot1.getValue(UserArticles.class);
                    if (userArticles1.getEmail().equals(curr_user)) {
                        if(userArticles1.getEditor()==1)
                            myArticleList.add(userArticles1);
                    }
                    articleCount.setText(""+myArticleList.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









            ref.child("UserType").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String roles=snapshot.child("role").getValue().toString();

                ref.child(roles).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren())
                        {
                            if(ds.getKey().equals(uid))
                            {
                                String image=""+ds.child("photo").getValue();
                                name.setText(""+ds.child("name").getValue());
                                if(!image.equals("")) {
                                    try {
                                        //if image is recieved
                                        Picasso.get().load(image).into(img);
                                    } catch (Exception e) {
                                        //exception getting image
                                        Picasso.get().load(R.drawable.ic_default_img).into(img);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        email.setText(auth.getCurrentUser().getEmail());

        change_password = findViewById(R.id.btn_change_password);
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPasswordDialog();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditProfileDialog();
            }
        });

        getAllArticles();
    }

    private void showRecoverPasswordDialog()
    {
        AlertDialog.Builder dialogAddRev = new AlertDialog.Builder(MyProfile.this);
        View loginView = getLayoutInflater().inflate(R.layout.dialog_change_password,null);

        currPass=loginView.findViewById(R.id.editTextCurrPass);
        newPass=loginView.findViewById(R.id.editTextNewPass);
        submit=loginView.findViewById(R.id.buttonDialogSubmit);


        dialogAddRev.setView(loginView);
        dialog = dialogAddRev.create();
        dialog.show();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String currUserEmail = user.getEmail();
                String currPassword=currPass.getText().toString();
                String newPassword=newPass.getText().toString();

//                System.out.println("765**************"+currPassword+"*******************");
//                System.out.println(newPassword+"987*********************************");


                AuthCredential credential = EmailAuthProvider.getCredential(currUserEmail, currPassword);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MyProfile.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(MyProfile.this, "Error password not updated", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(MyProfile.this, "Error. Authentication Failed", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }




    private boolean checkStoragePermission()
    {
        boolean result= ContextCompat.checkSelfPermission(MyProfile.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission()
    {
        requestPermissions( storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission()
    {
        boolean result= ContextCompat.checkSelfPermission(MyProfile.this,android.Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1= ContextCompat.checkSelfPermission(MyProfile.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission()
    {
        requestPermissions(cameraPermission, CAMERA_REQUEST_CODE);
    }
    private void showEditProfileDialog()
    {

        String options[]={"Edit Profile Photo", "Edit Name"};
        //alert
        AlertDialog.Builder builder=new AlertDialog.Builder((MyProfile.this));
        //set builder
        builder.setTitle("Choose Action");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //handle dialog item click
                if(i == 0)
                {
                    //edit profile clicked
                    showImagePicDialog();
                }
                else if(i==1)
                {
                    //edit cover clicked
                    //showImagePicDialog();
                    showNamePhoneUpdateDialog("name");
                }

            }
        });
        //create and show dialog
        builder.create().show();
    }

    private void showNamePhoneUpdateDialog(String key)
    {
        //custom dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(MyProfile.this);
        builder.setTitle("Update "+key);

        //set Layout of dialog
        LinearLayout linearLayout=new LinearLayout(MyProfile.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //add edit text
        EditText editText=new EditText(MyProfile.this);
        editText.setHint("Enter "+key);//hint Enter name or Enter Phone
        linearLayout.addView(editText);

        builder.setView(linearLayout);


        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        ref.child("UserType").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String roles=snapshot.child("role").getValue().toString();

                //add buttons in dialog to update
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //input text from edit text
                        String value= editText.getText().toString().trim();
                        if(!TextUtils.isEmpty(value))
                        {
                            HashMap<String,Object> result= new HashMap<>();
                            result.put(key, value);

                            ref.child(roles).child(auth.getCurrentUser().getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //updated
                                    Toast.makeText(MyProfile.this, "Updated", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //failed
                                    Toast.makeText(MyProfile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(MyProfile.this, "Please Enter "+key, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //add button in dialog to cancel
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showImagePicDialog() {

        String options[]={"Camera", "Gallery"};
        //alert
        AlertDialog.Builder builder=new AlertDialog.Builder((MyProfile.this));
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
                        Toast.makeText(MyProfile.this, "Please Enable Camera and Storage Permission", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MyProfile.this, "Please Enable Storage Permission", Toast.LENGTH_SHORT).show();
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
            if(requestCode == PICK_IMAGE_REQUEST && data!=null && data.getData()!=null)
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
        String filePathAndName=storagePath+"_"+user.getUid();
        StorageReference storageReference2= storageReference.child(filePathAndName);
        databaseReference =FirebaseDatabase.getInstance().getReference();


        databaseReference.child("UserType").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String roles=snapshot.child("role").getValue().toString();


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
                            HashMap<String, Object> results=new HashMap<>();

                            results.put("photo",downloadUri.toString());
                            databaseReference.child(roles).child(user.getUid()).updateChildren(results).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MyProfile.this, "Image Updated", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MyProfile.this, "Error Updating Image", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            //image error
                            Toast.makeText(MyProfile.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MyProfile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        image_uri=MyProfile.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //intent to start camera
        Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
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
                    if(modelSports.getEmail().equals(fUser.getEmail()) && modelSports.getEditor()==1)
                    {
                        sportsList.add(modelSports);
                    }

                    //adapter
                    adapterSports = new AdapterSports(MyProfile.this,sportsList);
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