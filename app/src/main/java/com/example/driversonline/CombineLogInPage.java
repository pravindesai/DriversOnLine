package com.example.driversonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CombineLogInPage extends AppCompatActivity {
    EditText idET;
    Button loginBtn;
    TextView signUpTV;
    String UserType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_log_in_page);

        idET=findViewById(R.id.idET);
        loginBtn=findViewById(R.id.loginBtn);
        signUpTV=findViewById(R.id.signUpTV);
        UserType=getIntent().getStringExtra("UserType");
        Toast.makeText(getBaseContext(),UserType,Toast.LENGTH_LONG).show();

        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CombineSignUp=new Intent(getBaseContext(), com.example.driversonline.CombineSignUp.class);
                CombineSignUp.putExtra("UserType",UserType);
                startActivity(new Intent(getBaseContext(),CombineSignUp.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num=idET.getText().toString().trim();
                Intent otpAct=new Intent(getBaseContext(),OtpActivity.class);
                otpAct.putExtra("num",num);
                otpAct.putExtra("UserType",UserType);
                if(num.isEmpty()){
                    idET.setError("Enter mobile number ");
                    idET.requestFocus();
                    return;
                }
                startActivity(otpAct);
            }
        });
    }
}
