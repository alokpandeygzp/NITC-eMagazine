package com.example.nitcemag.ui.home.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcemag.ArticlesActivity;
import com.example.nitcemag.MainActivity;
import com.example.nitcemag.R;
import com.example.nitcemag.ui.home.Models.ModelSports;
import com.example.nitcemag.ui.postArticles.UserArticles;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class AdapterSports extends RecyclerView.Adapter<AdapterSports.MyHolder> {
    Context context;
    DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
    List<ModelSports> sportsList;
    String key;
    //constructor
    public AdapterSports(Context context,List<ModelSports> sportsList)
    {
        this.context=context;
        this.sportsList=sportsList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout(row_user.xml)
        View view= LayoutInflater.from(context).inflate(R.layout.row_articles, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data

        String category =sportsList.get(position).getCategory();
        String image=sportsList.get(position).getImage();
        String title=sportsList.get(position).getTitle();
        key=sportsList.get(position).getKey();
        //set data
        holder.mNameTv.setText(title);
        holder.author.setText(sportsList.get(position).getAuthor());
        try
        {
            Picasso.get().load(image).placeholder(R.drawable.newspaper).into(holder.mavatarIv);
        }
        catch (Exception e)
        {

        }

        //handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArticlesActivity.class);
                key=sportsList.get(holder.getAdapterPosition()).getKey();
                intent.putExtra("key",key);
                context.startActivity(intent);
            }
        });
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user!=null)
        {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view)
                {
                    int pos=holder.getAdapterPosition();
                    key=sportsList.get(pos).getKey();
                    reference.child("Editor").addValueEventListener(new ValueEventListener() {

                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot ds:snapshot.getChildren())
                            {
                                if(ds.getKey().equals(user.getUid()))
                                {
                                    DeleteDialog(pos);
                                    break;
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    return true;
                }
            });

        }
    }
    private void DeleteDialog(int pos) {

       //alert
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        //set builder
        builder.setMessage("Do you want to Delete this article?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                reference.child("PostedArticles").child("Articles").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds: snapshot.getChildren())
                            {
                                UserArticles ua=ds.getValue(UserArticles.class);
                                if(ua.getAuthor().equals(sportsList.get(pos).getAuthor()) && ua.getTitle().equals(sportsList.get(pos).getTitle())
                                        && ua.getDescription().equals(sportsList.get(pos).getDescription()))
                                {
                                    ds.getRef().removeValue();
                                    break;
                                }
                            }
                            DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference("Articles").child(key);
                            mPostReference.removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        //create and show dialog
        builder.create().show();
    }
    @Override
    public int getItemCount() {
        return sportsList.size();
    }

    //view holder class
    public class MyHolder extends RecyclerView.ViewHolder{

        ImageView mavatarIv;
        TextView mNameTv,author;

        public MyHolder(@NonNull View itemView) {

            super(itemView);

            //init views
            mavatarIv=itemView.findViewById(R.id.avatarIv);
            mNameTv=itemView.findViewById(R.id.nameTv);
            author=itemView.findViewById(R.id.author);
        }
    }
}
