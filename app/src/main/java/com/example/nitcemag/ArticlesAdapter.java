package com.example.nitcemag;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.MyHolder> {
    Context context;
    List<ModelComment> list;
    String key;

    //constructor
    public ArticlesAdapter(Context context, List<ModelComment> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ArticlesAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comments, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesAdapter.MyHolder holder, int position) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String des = list.get(position).getText();
        String email = list.get(position).getEmail();
        String name = list.get(position).getName();
        key = list.get(position).getKey();
        holder.name.setText(name);
        holder.comment.setText(des);
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int pos=holder.getAdapterPosition();
                String em = list.get(pos).getEmail();
                if (em .equals(user.getEmail())) {
                    key=list.get(pos).getKey();
                    commentDialog();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void commentDialog() {

        String options[] = {"Edit", "Delete"};
        //alert
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //set builder
        builder.setTitle("");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //handle dialog item click
                if (i == 0) {
                   editComment("Comment");

                } else if (i == 1) {
                    DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference("Comments").child(key);
                    mPostReference.removeValue();
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }
    private void editComment(String k)
    {
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference("Comments").child(key);
        //custom dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Update "+k);

        //set Layout of dialog
        LinearLayout linearLayout=new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //add edit text
        EditText editText=new EditText(context);
        editText.setHint("Enter "+k);//hint Enter name or Enter Phone
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        //add buttons in dialog to update
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //input text from edit text
                String value= editText.getText().toString().trim();
                if(!TextUtils.isEmpty(value))
                {
                    mPostReference.child("text").setValue(value);
                }
                else
                {
                    Toast.makeText(context, "Please Enter "+k, Toast.LENGTH_SHORT).show();
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
    public class MyHolder extends RecyclerView.ViewHolder {
        TextView comment;
        TextView name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.recycle_comment);
        }
    }    }

