package com.example.driversonline;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class profileMainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public NavigationView navigationView;
    public DatabaseReference mdb= FirebaseDatabase.getInstance().getReference();
    public FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser muser;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://driversonline-f306c.appspot.com");    //change the url according to your firebase app
    Bitmap bitmap;
    ImageView picimg;
    TextView navUsername,header;

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
        muser=mAuth.getCurrentUser();
        //TextView header = (TextView) headerView.findViewById(R.id.headerText);
        //header.setText("NAME HERE");
        header = (TextView) headerView.findViewById(R.id.headerText);
        navUsername = (TextView) headerView.findViewById(R.id.textView);
        picimg=headerView.findViewById(R.id.imageView);


        if(muser!=null){
            //Toast.makeText(getBaseContext(),"checking.."+muser.getPhoneNumber(),Toast.LENGTH_SHORT).show();
            Query query= mdb.child("user/"+type).orderByChild("num").equalTo(muser.getPhoneNumber());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                        for(DataSnapshot snap:dataSnapshot.getChildren()){
                             u=snap.getValue(user.class);
                        }
                        header.setText(u.name);
                        navUsername.setText(u.type);

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
            //showprofile();
        }else{
            Toast.makeText(getBaseContext(),"user null",Toast.LENGTH_LONG).show();
        }

    }

    private void showprofile() {
        StorageReference pic = storageRef.child("Photos/").child(muser.getPhoneNumber()+".jpg");
        pic.getBytes(1024 * 1024 )
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        //Toast.makeText(getContext(),"image loading Success",Toast.LENGTH_LONG).show();
                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        picimg.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getContext(),"image loading failed\n"+e,Toast.LENGTH_LONG).show();
            }
        });
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
