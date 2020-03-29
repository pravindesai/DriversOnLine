package com.example.driversonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);

        getSupportActionBar().hide();
        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();

    }

    private class LogoLauncher extends Thread{
        private static final int SLEEP_TIMER = 3;

        public void run(){
            try{
                sleep(1000 * SLEEP_TIMER);
            }catch(InterruptedException e){
                e.printStackTrace();
            }

            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkUserLogedIn() {
        DatabaseReference mdb= FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser muser=mAuth.getCurrentUser();

        muser=mAuth.getCurrentUser();
        //Toast.makeText(getBaseContext(),"checking.."+muser.getPhoneNumber(),Toast.LENGTH_SHORT).show();
        if(muser!=null){
            //Toast.makeText(getBaseContext(),"checking.."+muser.getPhoneNumber(),Toast.LENGTH_SHORT).show();
            //if user is driver start drivermainActivity or start ownermainActivuty
            Query query= mdb.child("user/Driver").orderByChild("num").equalTo(muser.getPhoneNumber());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Toast.makeText(getBaseContext(),"current user is Driver",Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getBaseContext(),DriverMainActivity.class));
                        startActivity(new Intent(getBaseContext(),profileMainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(getBaseContext(),"current user is Owner",Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getBaseContext(),OwnerMainActivity.class));
                        startActivity(new Intent(getBaseContext(),profileMainActivity.class));
                        finish();
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
}
