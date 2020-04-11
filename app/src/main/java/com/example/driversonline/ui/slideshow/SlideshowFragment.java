package com.example.driversonline.ui.slideshow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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

public class SlideshowFragment extends Fragment {

    public ArrayList<booking> bookings;
    private DatabaseReference db= FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseUser muser=mAuth.getCurrentUser();
    custome_booking_list_adapter customAdapter;
    private String type;
    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SHAREDPREFERECEFILE", MODE_PRIVATE);
        final SharedPreferences.Editor editor= sharedPreferences.edit();
        type= sharedPreferences.getString("UserType",null);
        final View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        listView=root.findViewById(R.id.listView);
        Toast.makeText(root.getContext(),"slideshow fragment",Toast.LENGTH_SHORT).show();

        bookings=new ArrayList<booking>();
        customAdapter = new custome_booking_list_adapter(root.getContext(), bookings);
        listView.setAdapter(customAdapter);
        updateList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                booking b=bookings.get(position);
                String callnum="";
                if(type.equals("Driver")){
                    callnum=b.Onum;
                }else if(type.equals("Owner")){
                    callnum=b.Dnum;
                }
                //Toast.makeText(root.getContext(),callnum,Toast.LENGTH_SHORT).show();
                showAlertDialogButtonClicked(view,b,callnum);
            }
        });

        return root;
    }

    void updateList(){
        String numString="";
        if(type.equals("Driver")){
            numString="Dnum";
        }else {
            numString="Onum";
        }
        Query query= db.child("booking").orderByChild(numString)
                .equalTo(muser.getPhoneNumber());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Toast.makeText(getContext(),"snap exists",Toast.LENGTH_SHORT).show();
                    for(DataSnapshot snap:dataSnapshot.getChildren()){
                        booking b=snap.getValue(booking.class);
                        if(b.Action.equals("Accept")){
                            bookings.add(b);
                        }
                        customAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void showAlertDialogButtonClicked(View view, booking b, final String callnum) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Would you like to Call ?");
        builder.setMessage("Call and ask question if you want ?\nCall : "+callnum);

        // add the buttons
        builder.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", callnum, null)));

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