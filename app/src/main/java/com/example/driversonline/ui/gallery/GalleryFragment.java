package com.example.driversonline.ui.gallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.driversonline.R;
import com.example.driversonline.booking;
import com.example.driversonline.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class GalleryFragment extends Fragment {

    ImageView editBtn;
    TextView nameTv,cityTv,userTypeTv;
    CircleImageView profilepic;
    private DatabaseReference db= FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseUser muser=mAuth.getCurrentUser();
    private String type;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SHAREDPREFERECEFILE", MODE_PRIVATE);
        final SharedPreferences.Editor editor= sharedPreferences.edit();
        type= sharedPreferences.getString("UserType",null);
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        editBtn=root.findViewById(R.id.editBtn);
        profilepic=root.findViewById(R.id.profilepic);
        nameTv=root.findViewById(R.id.nameTv);
        cityTv=root.findViewById(R.id.cityTv);
        userTypeTv=root.findViewById(R.id.userTypeTv);

        Query query=db.child("user").child(type).orderByChild("num").equalTo(muser.getPhoneNumber());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(getContext(),""+dataSnapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    user u=snap.getValue(user.class);
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
                uploadProfilePic();
            }
        });

        return root;
    }

    private void uploadProfilePic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Upload new profile picture");
        builder.setMessage("Call and ask question if you want ?\nCall : ");

        // add the buttons
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", callnum, null)));

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}