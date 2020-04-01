package com.example.driversonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    Button OwnerBtn,DriverBtn;
    SharedPreferences sharedPreferences;
    DatabaseReference mdb=FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser muser=mAuth.getCurrentUser();
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OwnerBtn=(Button)findViewById(R.id.OwnerBtn);
        DriverBtn=(Button)findViewById(R.id.DriverBtn);
        sharedPreferences=getSharedPreferences("SHAREDPREFERECEFILE",MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        final Intent CombineLoginPage=new Intent(getBaseContext(),CombineLogInPage.class);

        checkUserLogedIn();

        OwnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("UserType","Owner");
                editor.commit();
                startActivity(CombineLoginPage);
            }
        });

        DriverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("UserType","Driver");
                editor.commit();
                startActivity(CombineLoginPage);
            }
        });

    }

    private void checkUserLogedIn() {
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
