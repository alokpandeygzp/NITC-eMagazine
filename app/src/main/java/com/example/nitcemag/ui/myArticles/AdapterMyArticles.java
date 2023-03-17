package com.example.nitcemag.ui.myArticles;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcemag.ArticlesActivity;
import com.example.nitcemag.R;
import com.example.nitcemag.ui.postArticles.UserArticles;

import java.util.List;

public class AdapterMyArticles extends RecyclerView.Adapter<MyHolder> {

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





}
 class MyHolder  extends RecyclerView.ViewHolder{
    ImageView imageView;
    TextView textView;

    public MyHolder(@NonNull View itemView) {
        super(itemView);
        this.imageView = itemView.findViewById(R.id.avatarIv);
        this.textView = itemView.findViewById(R.id.nameTv);
        // itemView.setOnClickListener(this);
    }


}

