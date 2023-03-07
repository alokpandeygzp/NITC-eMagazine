package com.example.nitcemag.ui.home.Adapters;

import android.content.Context;
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

import com.example.nitcemag.MainActivity;
import com.example.nitcemag.R;
import com.example.nitcemag.ui.home.Models.ModelSports;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class AdapterSports extends RecyclerView.Adapter<AdapterSports.MyHolder> {
    Context context;
    List<ModelSports> sportsList;

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

        //set data
        holder.mNameTv.setText(title);
        try {
            ImageView i = (holder.mavatarIv);
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(image.toString()).getContent());
            i.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try
//        {
//            Toast.makeText(context, ""+image, Toast.LENGTH_SHORT).show();
//            Picasso.get().load(image).placeholder(R.drawable.newspaper).into(holder.mavatarIv);
//        }
//        catch (Exception e)
//        {
//
//        }

        //handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("category",category);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sportsList.size();
    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder{

        ImageView mavatarIv;
        TextView mNameTv;

        public MyHolder(@NonNull View itemView) {

            super(itemView);

            //init views
            mavatarIv=itemView.findViewById(R.id.avatarIv);
            mNameTv=itemView.findViewById(R.id.nameTv);
        }
    }
}
