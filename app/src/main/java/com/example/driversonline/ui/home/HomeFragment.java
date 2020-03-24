package com.example.driversonline.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driversonline.Adapter;
import com.example.driversonline.R;
import com.example.driversonline.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<user> users;
    DatabaseReference db=FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser muser=mAuth.getCurrentUser();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=root.findViewById(R.id.recyclerView);
        users=new ArrayList<user>();
        //getListItems();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new Adapter(this,users);
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(muser!=null){
            //Toast.makeText(getBaseContext(),"checking.."+muser.getPhoneNumber(),Toast.LENGTH_SHORT).show();
            //if user is driver start drivermainActivity or start ownermainActivuty
            Query query= db.child("user/Driver").orderByChild("num");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Toast.makeText(getContext(),"snap exists",Toast.LENGTH_SHORT).show();
                        for(DataSnapshot snap:dataSnapshot.getChildren()){
                            users.add(snap.getValue(user.class));
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void getListItems(){
        users.add(new user("7020529425", "pravin", "Shrirampur", "Driver", "LNO1234"));
        users.add(new user("7020529425", "pravin", "Shrirampur", "Driver", "LNO1234"));
        users.add(new user("7020529425", "pravin", "Shrirampur", "Driver", "LNO1234"));
        users.add(new user("7020529425", "pravin", "Shrirampur", "Driver", "LNO1234"));
        users.add(new user("7020529425", "pravin", "Shrirampur", "Driver", "LNO1234"));
    }
}