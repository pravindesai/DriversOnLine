package com.example.driversonline.ui.home;

import android.content.SharedPreferences;
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

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private Adapter adapter;
    public ArrayList<user> users;
    public ArrayList<booking> bookings;
    private DatabaseReference db=FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseUser muser=mAuth.getCurrentUser();
    private String type;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SHAREDPREFERECEFILE", MODE_PRIVATE);
        final SharedPreferences.Editor editor= sharedPreferences.edit();
        type= sharedPreferences.getString("UserType",null);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=root.findViewById(R.id.recyclerView);

        users=new ArrayList<user>();
        bookings=new ArrayList<booking>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

        @Override
        public void onStart() {
            super.onStart();
            if(muser!=null){
                //if user is driver start drivermainActivity or start ownermainActivuty
                if(type.equals("Driver")){
                    //send bookings
                    adapter=new Adapter(this,bookings,1);
                    recyclerView.setAdapter(adapter);
                    getListForDriver();
                }else if(type.equals("Owner")){
                    adapter=new Adapter(this,users);
                    recyclerView.setAdapter(adapter);
                    getListForOwner();
                }
            }
        }
    private void getListForDriver(){
        bookings.clear();
        Query query= db.child("booking").orderByChild("Dnum").equalTo(muser.getPhoneNumber());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Toast.makeText(getContext(),"snap exists",Toast.LENGTH_SHORT).show();
                    for(DataSnapshot snap:dataSnapshot.getChildren()){
                        booking b=snap.getValue(booking.class);
                        if(b.Action.equals("None")){
                            bookings.add(b);
                            adapter.notifyDataSetChanged();

                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //Toast.makeText(getContext(),"getListForDriver called..",Toast.LENGTH_SHORT).show();
    }

    private void getListForOwner(){
        users.clear();
        Query query= db.child("user/Driver").orderByChild("num");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Toast.makeText(getContext(),"snap exists",Toast.LENGTH_SHORT).show();
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