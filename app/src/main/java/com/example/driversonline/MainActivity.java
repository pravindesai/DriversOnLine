package com.example.driversonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button OwnerBtn,DriverBtn;
    String UserType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OwnerBtn=(Button)findViewById(R.id.OwnerBtn);
        DriverBtn=(Button)findViewById(R.id.DriverBtn);
        final Intent CombineLoginPage=new Intent(getBaseContext(),CombineLogInPage.class);
        OwnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserType="Owner";
                CombineLoginPage.putExtra("UserType",UserType);
                startActivity(CombineLoginPage);
            }
        });

        DriverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserType="Driver";
                CombineLoginPage.putExtra("UserType",UserType);
                startActivity(CombineLoginPage);
            }
        });

    }
}
