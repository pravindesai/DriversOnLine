package com.example.driversonline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class requestBooking extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_booking);
        tv=findViewById(R.id.tv);
        try{

            Bundle bundle=getIntent().getExtras();
            user u = (user) bundle.getSerializable("userObj");
            tv.setText(u.name);
        }catch (Exception e){
            Toast.makeText(getApplication(),"NULL POINTER ON requestBooking.java",Toast.LENGTH_SHORT).show();
        }

    }
}
