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

public class AdapterMyArticles extends RecyclerView.Adapter<AdapterMyArticles.MyHolder> {

    Context context;
    List<UserArticles> userArticles;

    public AdapterMyArticles(Context context, List<UserArticles> userArticles) {
        this.context = context;
        this.userArticles = userArticles;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.row_articles,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.textView.setText(userArticles.get(position).getTitle());
        // holder.imageView.setI;
        String key=userArticles.get(position).getKey();
        String image=userArticles.get(position).getImage();
        holder.author.setText(userArticles.get(position).getAuthor());

        if(userArticles.get(position).getEditor()==1)
        {
            holder.status.setText("Posted Articles");
            holder.status.setTextColor(Color.GREEN);
            holder.status.setBackgroundColor(R.drawable.rounded_button);
        }
        else if(userArticles.get(position).getReviewer()==1)
        {
            holder.status.setText("Reviewed Articles");
            holder.status.setTextColor(Color.YELLOW);
            holder.status.setBackgroundColor(R.drawable.rounded_button);
        }
        else if(userArticles.get(position).getEditor()==-1 || userArticles.get(position).getReviewer()==-1)
        {
            holder.status.setText("Rejected Articles");
            holder.status.setTextColor(Color.RED);
            holder.status.setBackgroundColor(R.drawable.rounded_button);
        }
        else if(userArticles.get(position).getReviewer()==0)
        {
            holder.status.setText("Pending Articles");
            holder.status.setTextColor(Color.WHITE);
            holder.status.setBackgroundColor(R.drawable.rounded_button);
        }
        try
        {
            Picasso.get().load(image).placeholder(R.drawable.newspaper).into(holder.imageView);
        }
        catch (Exception e)
        {

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArticlesActivity.class);
                intent.putExtra("key",key);
                context.startActivity(intent);}
        });
    }

    @Override
    public int getItemCount() {
        return userArticles.size();
    }

    public static class MyHolder  extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView,author,status;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.avatarIv);
            this.textView = itemView.findViewById(R.id.nameTv);
            this.author=itemView.findViewById(R.id.author);
            this.status= itemView.findViewById(R.id.status);
            // itemView.setOnClickListener(this);
        }
    }
}

