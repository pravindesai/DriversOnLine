package com.example.driversonline.ui.gallery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.example.driversonline.R;
import com.example.driversonline.booking;
import com.example.driversonline.user;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class GalleryFragment extends Fragment {

    ImageView editBtn;
    TextView nameTv,cityTv,userTypeTv;
    CircleImageView profilepic;
    private DatabaseReference db= FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseUser muser=mAuth.getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://driversonline-f306c.appspot.com");    //change the url according to your firebase app
    Bitmap bitmap;
    View root;

    private String type;
    user u;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SHAREDPREFERECEFILE", MODE_PRIVATE);
        final SharedPreferences.Editor editor= sharedPreferences.edit();
        type= sharedPreferences.getString("UserType",null);
        root = inflater.inflate(R.layout.fragment_gallery, container, false);

        editBtn=root.findViewById(R.id.editBtn);
        profilepic=root.findViewById(R.id.profilepic);
        nameTv=root.findViewById(R.id.nameTv);
        cityTv=root.findViewById(R.id.cityTv);
        userTypeTv=root.findViewById(R.id.userTypeTv);
        pd=new ProgressDialog(root.getContext());
        pd.setMessage("Its loading....");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        Query query=db.child("user").child(type).orderByChild("num").equalTo(muser.getPhoneNumber());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(getContext(),""+dataSnapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    u=snap.getValue(user.class);
                    nameTv.setText(u.name);
                    userTypeTv.setText(u.type);
                    cityTv.setText(u.city);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseProfilePic();
            }
        });
        StorageReference pic = storageRef.child("Photos/").child(muser.getPhoneNumber()+".jpg");
        pic.getBytes(1024 * 1024 )
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //Toast.makeText(getContext(),"image loading Success",Toast.LENGTH_LONG).show();
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilepic.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"image loading failed\n"+e,Toast.LENGTH_LONG).show();
            }
        });
        return root;
    }

    private void chooseProfilePic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Upload new profile picture");
        //builder.setMessage("");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"),PICK_IMAGE_REQUEST );

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                profilepic.setImageBitmap(bitmap);
                upload_image();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void upload_image(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] data = baos.toByteArray();

        if(bitmap != null) {
            pd.show();
            StorageReference childRef = storageRef.child("Photos/" +muser.getPhoneNumber()+".jpg");
            UploadTask uploadTask = childRef.putBytes(data);
            Toast.makeText(root.getContext(),"function called \n"+filePath,Toast.LENGTH_SHORT).show();

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    Toast.makeText(root.getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(root.getContext(), "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(root.getContext(), "Select an image", Toast.LENGTH_SHORT).show();
        }

    }
}