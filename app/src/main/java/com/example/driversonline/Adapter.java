package com.example.driversonline;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.driversonline.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {

    private LayoutInflater layoutInflater;
    Context c;
    progress progress;
    String id;
    List<user> data;
    List<booking> bdata;
    Cust_Dialog d;
    String type,CurrentUserName,CurrentUserCity;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    DatabaseReference mdb= FirebaseDatabase.getInstance().getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    AlertDialog alertDialog;
    Bitmap bitmap;
    StorageReference storageRef = storage.getReferenceFromUrl("gs://driversonline-f306c.appspot.com");    //change the url according to your firebase app


    public Adapter(HomeFragment context, List<user>data) {
        this.layoutInflater=LayoutInflater.from(context.getContext());
        progress=new progress(context.getContext());
        c=context.getContext();
        this.data=data;
        sharedPreferences=context.getContext().getSharedPreferences("SHAREDPREFERECEFILE", Context.MODE_PRIVATE);
        editor =sharedPreferences.edit();
        type=sharedPreferences.getString("UserType",null);
        CurrentUserName=sharedPreferences.getString("CurrentUserName",null);
        CurrentUserCity=sharedPreferences.getString("CurrentUserCity",null);
        editor.apply();
        progress.show();
    }

    public Adapter(HomeFragment context, List<booking> bdata,int i) {
        this.layoutInflater=LayoutInflater.from(context.getContext());
        progress=new progress(context.getContext());
        c=context.getContext();
        this.bdata=bdata;
        sharedPreferences=context.getContext().getSharedPreferences("SHAREDPREFERECEFILE", Context.MODE_PRIVATE);
        editor =sharedPreferences.edit();
        type=sharedPreferences.getString("UserType",null);
        CurrentUserName=sharedPreferences.getString("CurrentUserName",null);
        CurrentUserCity=sharedPreferences.getString("CurrentUserCity",null);
        editor.apply();
        progress.show();
        //Toast.makeText(context.getContext(),"Adapter.java booking constrructor",Toast.LENGTH_LONG).show();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.card_for_owner,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {
        progress.dissmiss();
        try {
            alertDialog.dismiss();
        }catch (Exception e){
            Log.wtf("SOMETHING WENT WRONG IN ADAPTER","SOMETHING WENT WRONG IN ADAPTER");
        }
        if(type.equals("Driver")){
            progress.dissmiss();
             final booking b=bdata.get(position);
             final String startDate=b.startDate;
             final String endDate=b.endDate;
             final String Onum=b.Onum;
             final String Oname=b.Oname;
             final String Ocity=b.Ocity;
             final String Dnum=b.Dnum;
             final String Action=b.Action;

            holder.nameTv.setText(Oname);
            holder.cityTv.setText(Ocity);
            holder.ratingBar.setNumStars(5);
            holder.ratingBar.setRating(Float.parseFloat("4"));
            holder.ratingBar.setEnabled(false);
            holder.detailsTv.setText(startDate+"   To   "+endDate);
            holder.imageView.setImageResource(R.drawable.cargif);
            holder.Btn1.setText("Accept");
            holder.Btn2.setText("Reject");

            holder.Btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    updateBookingMethod(b,"Accept");
                    bdata.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,bdata.size());
                }
            });
            holder.Btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateBookingMethod(b,"Reject");
                    bdata.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,bdata.size());
                }
            });
            update_img(holder,Onum);
        }else if(type.equals("Owner")){
            progress.dissmiss();
            final user u=data.get(position);
            String num=u.num;
            String type=u.type;
            holder.nameTv.setText(u.name);
            holder.cityTv.setText(u.city);
            holder.ratingBar.setNumStars(5);
            holder.ratingBar.setRating(Float.parseFloat("4"));
            holder.ratingBar.setEnabled(false);
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
                    //Toast.makeText(v.getContext(),"Button 2 pressed of "+u.name,Toast.LENGTH_SHORT).show();
                    d=new Cust_Dialog();
                    d.ShowCustomeDialog(u,v);
                }
            });
            update_img(holder,num);
        }
    }

    private void update_img(final viewHolder holder, final String num) {
        StorageReference pic = storageRef.child("Photos/").child(num+".jpg");
        pic.getBytes(1024 * 1024)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        //Toast.makeText(getContext(),"inge loding Success",Toast.LENGTH_LONG).show();
                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.imageView.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getContext(),"image loding failed\n"+e,Toast.LENGTH_LONG).show();
            }
        });

    }

    private void updateBookingMethod(final booking b, final String Action) {
        Query query= mdb.child("booking").orderByChild("Dnum")
                .equalTo(mAuth.getCurrentUser().getPhoneNumber());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Toast.makeText(getContext(),"snap exists",Toast.LENGTH_SHORT).show();
                    for(DataSnapshot snap:dataSnapshot.getChildren()){
                        booking Qb=snap.getValue(booking.class);
                        if(         Qb.Oname.equals(b.Oname)
                                && Qb.startDate.equals(b.startDate)
                                && Qb.endDate.equals(b.endDate)){
                            id=snap.getKey();
                            booking updateBooking=new booking(b.startDate,b.endDate,b.Onum,b.Oname,b.Ocity,b.Dnum,Action);
                            mdb.child("booking").child(id).setValue(updateBooking);
                            //Toast.makeText(v.getContext(),id+"  Done",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getItemCount() {
        int size=0;
        if(type.equals("Driver")){
            size=bdata.size();
        }else if(type.equals("Owner")){
            size=data.size();
        }
        return size;
    }

    public void dismissProgressBar(){
        progress.dissmiss();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        alertDialogBuilder.setMessage("No requests found !");
         alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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

    public void destroy(){
        if(bitmap!=null){
            bitmap=null;
        }
    }
}
