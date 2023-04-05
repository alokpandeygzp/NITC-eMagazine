package com.example.nitcemag;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcemag.ui.postArticles.UserArticles;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFollow extends RecyclerView.Adapter<AdapterFollow.MyHolder> {

    Context context;
    List<UserDetails> userDetails;
    public AdapterFollow(Context context, List<UserDetails> userDetails) {
        this.context = context;
        this.userDetails = userDetails;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.rowfollow,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        int pos=position;
        holder.user.setText(userDetails.get(position).name);
        holder.email.setText(userDetails.get(position).email);
        String img=userDetails.get(position).photo;
        try
        {
            Picasso.get().load(img).placeholder(R.drawable.ic_default_img).into(holder.imageView);
        }
        catch (Exception e)
        {

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent= new Intent(context,UserProfile.class);
                intent.putExtra("email",userDetails.get(pos).email);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return userDetails.size();
    }

    public static class MyHolder  extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView user, email;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image);
            this.user = itemView.findViewById(R.id.username);
            this.email = itemView.findViewById(R.id.email);
            // itemView.setOnClickListener(this);
        }
    }
}

