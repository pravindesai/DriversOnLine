package com.example.driversonline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class about_me {
    CircleImageView devpic;
    ImageView imageView;
    ImageView gittv,mailtv,instatv;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://driversonline-f306c.appspot.com");    //change the url according to your firebase app
    Bitmap bitmap;


    about_me(final Context context){
        final Dialog MyDialog;
        MyDialog= new Dialog(context);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.about_me);

        devpic=MyDialog.findViewById(R.id.devpic);
        imageView=MyDialog.findViewById(R.id.imageView);
        gittv=MyDialog.findViewById(R.id.gittv);
        mailtv=MyDialog.findViewById(R.id.mailtv);
        instatv=MyDialog.findViewById(R.id.instatv);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialog.dismiss();
            }
        });
        instatv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/pravindesai__");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    context.startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/pravindesai__")));
                }
            }
        });
        gittv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/pravindesai/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });
        mailtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"pravindesai100@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Mail through app");
                try {
                    context.startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                }
            }
        });

        StorageReference pic = storageRef.child("devimg.jpeg");
        pic.getBytes(1024 * 1024 )
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        //Toast.makeText(getContext(),"image loading Success",Toast.LENGTH_LONG).show();
                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        devpic.setImageBitmap(bitmap);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"image loading failed\n"+e,Toast.LENGTH_LONG).show();
            }
        });




        MyDialog.show();

    }


}
