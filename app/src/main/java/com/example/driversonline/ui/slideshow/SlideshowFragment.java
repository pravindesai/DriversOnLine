package com.example.driversonline.ui.slideshow;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.driversonline.Adapter;
import com.example.driversonline.R;
import com.example.driversonline.booking;
import com.example.driversonline.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SlideshowFragment extends Fragment {

    public ArrayList<booking> bookings;
    private DatabaseReference db= FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseUser muser=mAuth.getCurrentUser();
    private String type;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SHAREDPREFERECEFILE", MODE_PRIVATE);
        final SharedPreferences.Editor editor= sharedPreferences.edit();
        type= sharedPreferences.getString("UserType",null);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        Toast.makeText(root.getContext(),"slideshow fragment",Toast.LENGTH_SHORT).show();




        return root;
    }
}