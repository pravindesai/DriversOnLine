package com.example.driversonline.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<user> users;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=root.findViewById(R.id.recyclerView);
        users=new ArrayList<user>();
        getListItems();


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new Adapter(this,users);
        recyclerView.setAdapter(adapter);
        return root;
    }

    public void getListItems(){
        users.add(new user("7020529425", "pravin", "Shrirampur", "Driver", "LNO1234"));
        users.add(new user("7020529425", "pravin", "Shrirampur", "Driver", "LNO1234"));
        users.add(new user("7020529425", "pravin", "Shrirampur", "Driver", "LNO1234"));
        users.add(new user("7020529425", "pravin", "Shrirampur", "Driver", "LNO1234"));
        users.add(new user("7020529425", "pravin", "Shrirampur", "Driver", "LNO1234"));
    }
}