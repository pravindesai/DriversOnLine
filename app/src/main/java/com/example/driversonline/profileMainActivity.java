package com.example.driversonline;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class profileMainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public NavigationView navigationView;
    public DatabaseReference mdb= FirebaseDatabase.getInstance().getReference();
    public FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public SharedPreferences sharedPreferences;
    String type;
    user u = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences=getSharedPreferences("SHAREDPREFERECEFILE",MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        type=sharedPreferences.getString("UserType",null);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_request,
                R.id.nav_logout,R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_main, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    protected void onStart() {
        super.onStart();

        final View headerView = navigationView.getHeaderView(0);
        FirebaseUser muser=mAuth.getCurrentUser();
        //TextView header = (TextView) headerView.findViewById(R.id.headerText);
        //header.setText("NAME HERE");
        TextView navUsername = (TextView) headerView.findViewById(R.id.textView);
        navUsername.setText(type);

        if(muser!=null){
            //Toast.makeText(getBaseContext(),"checking.."+muser.getPhoneNumber(),Toast.LENGTH_SHORT).show();
            Query query= mdb.child("user/"+type).orderByChild("num").equalTo(muser.getPhoneNumber());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        TextView header = (TextView) headerView.findViewById(R.id.headerText);
                        for(DataSnapshot snap:dataSnapshot.getChildren()){
                             u=snap.getValue(user.class);
                        }
                        header.setText(u.name);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("CurrentUserName",u.name);
                        myEdit.putString("CurrentUserCity",u.city);
                        myEdit.commit();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            Toast.makeText(getBaseContext(),"user null",Toast.LENGTH_LONG).show();
        }

    }

    public void logout(MenuItem item) {
        Toast.makeText(getBaseContext(),"FINISH THIS ACTIVITY AFTER LOGOUT",Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("LOG OUT");
        builder.setMessage("We'll miss you ..");
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(),"CONFIREMED",Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finishAffinity();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(),"CANCELLED",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }



}
