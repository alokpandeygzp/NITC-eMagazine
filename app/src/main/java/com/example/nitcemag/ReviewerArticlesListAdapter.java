package com.example.nitcemag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcemag.ui.postArticles.UserArticles;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewerArticlesListAdapter extends RecyclerView.Adapter<ReviewerArticlesListAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserArticles> list;

    public ReviewerArticlesListAdapter(Context context, ArrayList<UserArticles> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.row_articles,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserArticles articles=list.get(position);
        holder.title.setText(articles.getTitle());
        holder.author.setText(articles.getAuthor());
        String image=list.get(position).getImage();
        holder.dummy.setVisibility(View.GONE);
        try
        {
            Picasso.get().load(image).placeholder(R.drawable.newspaper).into(holder.avatarIv);
        }
        catch (Exception e)
        {

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReviewerAction.class);
                intent.putExtra("key",articles.getKey());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title,author;
        ImageView avatarIv;
        TextView dummy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.nameTv);
            avatarIv=itemView.findViewById(R.id.avatarIv);
            author=itemView.findViewById(R.id.author);
            dummy=itemView.findViewById(R.id.status);

        }
    }
}
