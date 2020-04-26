package com.example.driversonline.ui.home;

import androidx.fragment.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
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

    public RecyclerView recyclerView;
    public Adapter adapter;
    public ArrayList<user> users;
    public ArrayList<booking> bookings;
    public DatabaseReference db=FirebaseDatabase.getInstance().getReference();
    public FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public FirebaseUser muser=mAuth.getCurrentUser();
    private String type;
    int flag=0;
    user u;
    booking b;
    Query query;
    SharedPreferences sharedPreferences;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        sharedPreferences = getContext().getSharedPreferences("SHAREDPREFERECEFILE", MODE_PRIVATE);
        //final SharedPreferences.Editor editor= sharedPreferences.edit();
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
        query= db.child("booking").orderByChild("Dnum").equalTo(muser.getPhoneNumber());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Toast.makeText(getContext(),"snap exists",Toast.LENGTH_SHORT).show();
                    for(DataSnapshot snap:dataSnapshot.getChildren()){
                        b=snap.getValue(booking.class);
                        if(b.Action.equals("None")){
                            bookings.add(b);
                            adapter.notifyDataSetChanged();
                            flag=1;
                        }
                    }
                }
                if (flag==0){
                    Toast.makeText(getContext(),"No data available ",Toast.LENGTH_SHORT).show();
                    adapter.dismissProgressBar();
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
         query= db.child("user/Driver").orderByChild("num");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Toast.makeText(getContext(),"snap exists"+dataSnapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();
                    for(DataSnapshot snap:dataSnapshot.getChildren()){
                        u=snap.getValue(user.class);
                        users.add(u);
                        adapter.notifyDataSetChanged();
                        flag=1;
                    }
                }
                if (flag==0){
                    Toast.makeText(getContext(),"No data available ",Toast.LENGTH_SHORT).show();
                    adapter.dismissProgressBar();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onDestroy() {
        //clear memory from adapter too
        //Toast.makeText(getContext(),"List destroyed",Toast.LENGTH_LONG).show();
        adapter.destroy();
        bookings.clear();
        users.clear();
        u=null;
        mAuth=null;
        recyclerView.getRecycledViewPool().clear();
        System.runFinalization();
        Runtime.getRuntime().gc();
        super.onDestroy();
    }
}