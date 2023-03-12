package com.example.nitcemag.ui.postArticles;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.nitcemag.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostArticles extends Fragment {

   // FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();


    private PostArticlesViewModel mViewModel;
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Spinner category;
        String[] cat={"Sports","Academic","Events","Notice"};
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_articles, container, false);
        category= view.findViewById(R.id.category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, cat);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        category.setAdapter(adapter);

        final String[] article_cat = new String[1];
        EditText title;
        EditText name;
        EditText email;
        Button image_btn;
        Button submitbtn;
        EditText article_descp;
        title = view.findViewById(R.id.textTitle);
        name= view.findViewById(R.id.textName);
        email= view.findViewById(R.id.textEmail);
        article_descp= view.findViewById(R.id.text_descp);
        submitbtn = view.findViewById(R.id.submit_btn);

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
        ActivityResultLauncher<String> mGetContent =registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if(result !=null)
                        {

                            img.setImageURI(result);
                        }
                    }
                }
        );
        imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {

                imagebtn.setOnClickListener(view -> mGetContent.launch("image/*"));

                //Toast.makeText(view.getContext(), "image upload", Toast.LENGTH_SHORT).show();
            }
        });





        Button submit=view.findViewById(R.id.submit_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // String article_title,author_email,description,article_category;
                String author_name=name.getText().toString();
                String article_title= title.getText().toString();
                String author_email=email.getText().toString();
                String description=article_descp.getText().toString();
                String  article_category=article_cat[0];
                String email[] = author_email.split("@");

               // Toast.makeText(view.getContext(), email[1], Toast.LENGTH_SHORT).show();
                if(author_name.isEmpty() || article_title.isEmpty() || author_email.isEmpty() || description.isEmpty() || article_category.isEmpty())
                {
                    Toast.makeText(view.getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (email[1].equals("nitc.ac.in")==false)
                {
                    Toast.makeText(view.getContext(), "Enter Nitc email id", Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(view.getContext(), "Your Article send successfully to Publish", Toast.LENGTH_SHORT).show();
                    article_by_userFirebase(author_name,author_email,article_title,description,article_category);

                }
               // Toast.makeText(view.getContext(), "Your Article send successfully to Publish", Toast.LENGTH_SHORT).show();


            }
        });




       // Button b;
     //   b=view.findViewById(R.id.buttonPost);
     //   b.setOnClickListener(new View.OnClickListener() {
       //     @Override
      //      public void onClick(View view) {
       //         Toast.makeText(view.getContext(), "Success", Toast.LENGTH_SHORT).show();
         //   }
        //});
        
    return view;
    }


    private void article_by_userFirebase(String author_name,String author_email,String article_title,String description,String article_category)
    {
        //String generateUUIDNo = String.format("%010d",new BigInteger(UUID.randomUUID().toString().replace("-",""),16));
        //String article_no = generateUUIDNo.substring( generateUUIDNo.length() - 10);
        //System.out.println("article id  "+article_no);
       UserArticles userarticles= new UserArticles(author_name,author_email,article_title,description,article_category);
       reference.child("UserArticles").push().setValue(userarticles);

       // HashMap<String, Object> hash_articles = new HashMap<>();
        //hash_articles.put(article_no/"title",author_name);

    }
    }