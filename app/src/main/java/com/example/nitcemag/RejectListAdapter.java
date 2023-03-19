package com.example.nitcemag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nitcemag.ui.postArticles.UserArticles;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RejectListAdapter extends RecyclerView.Adapter<RejectListAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserArticles> list;
    AlertDialog dialog;
    TextView dialogDesc;
    Button close;
    String dialogDescStr;

    public RejectListAdapter(Context context, ArrayList<UserArticles> list) {
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
        try{
            Picasso.get().load(articles.getImage()).into(holder.img);
        }
        catch (Exception e)
        {

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, EditorAction.class);
//                intent.putExtra("key",articles.getKey());
//                context.startActivity(intent);
//                ((Activity)context).finish();
                AlertDialog.Builder dialogAddRev = new AlertDialog.Builder(context);
                View loginView = LayoutInflater.from(context).inflate(R.layout.dialog_rejectreason,null);

                dialogDesc = loginView.findViewById(R.id.textViewReason);
                close = loginView.findViewById(R.id.buttonDialogSubmit);

                dialogAddRev.setView(loginView);
                dialog = dialogAddRev.create();
                dialog.show();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Articles");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren())
                        {
                            if(ds.getKey().equals(articles.getKey())){
                                String reason = ds.child("rejectreason").getValue().toString();
                                dialogDesc.setText(reason);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                String reason = ref.child(articles.getKey()).child("rejectreason").get();
//                dialogDesc.setText(reason);



                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title,author;
        ImageView img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            author=itemView.findViewById(R.id.author);
            title=itemView.findViewById(R.id.nameTv);
            img=itemView.findViewById(R.id.avatarIv);
        }
    }
}
