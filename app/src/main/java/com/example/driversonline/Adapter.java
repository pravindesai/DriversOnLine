package com.example.driversonline;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.driversonline.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {

    private LayoutInflater layoutInflater;
    List<user> data;
    List<booking> bdata;
    Cust_Dialog d;
    String type,CurrentUserName,CurrentUserCity;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    DatabaseReference mdb= FirebaseDatabase.getInstance().getReference();

    public Adapter(HomeFragment context, List<user>data) {
        this.layoutInflater=LayoutInflater.from(context.getContext());
        this.data=data;
        sharedPreferences=context.getContext().getSharedPreferences("SHAREDPREFERECEFILE", Context.MODE_PRIVATE);
        editor =sharedPreferences.edit();
        type=sharedPreferences.getString("UserType",null);
        CurrentUserName=sharedPreferences.getString("CurrentUserName",null);
        CurrentUserCity=sharedPreferences.getString("CurrentUserCity",null);
    }

    public Adapter(HomeFragment context, ArrayList<booking> bdata) {
        this.layoutInflater=LayoutInflater.from(context.getContext());
        this.bdata=bdata;
        sharedPreferences=context.getContext().getSharedPreferences("SHAREDPREFERECEFILE", Context.MODE_PRIVATE);
        editor =sharedPreferences.edit();
        type=sharedPreferences.getString("UserType",null);
        CurrentUserName=sharedPreferences.getString("CurrentUserName",null);
        CurrentUserCity=sharedPreferences.getString("CurrentUserCity",null);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.card_for_owner,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position) {
        final user u=data.get(position);
        String num=u.num;
        String type=u.type;
        holder.nameTv.setText(u.name);
        holder.cityTv.setText(u.city);
        holder.ratingBar.setNumStars(5);
        //set rating here
        holder.detailsTv.setText(u.lno);
        holder.imageView.setImageResource(R.drawable.cargif);
        holder.Btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(),"Button 1 pressed of "+u.name,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+u.num));
                v.getContext().startActivity(intent);
            }
        });
        holder.Btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Button 2 pressed of "+u.name,Toast.LENGTH_SHORT).show();
                d=new Cust_Dialog();
                d.ShowCustomeDialog(u,v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView nameTv,cityTv,detailsTv;
        RatingBar ratingBar;
        Button Btn1,Btn2;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            nameTv=itemView.findViewById(R.id.nameTv);
            cityTv=itemView.findViewById(R.id.cityTv);
            detailsTv=itemView.findViewById(R.id.detailsTv);
            ratingBar=itemView.findViewById(R.id.ratingBar);
            Btn1=itemView.findViewById(R.id.Btn1);
            Btn2=itemView.findViewById(R.id.Btn2);
        }
    }
}
