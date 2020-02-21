package com.example.driversonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CombineLogInPage extends AppCompatActivity {
    EditText idET,otpEt;
    Button loginBtn,verifyBtn;
    TextView signUpTV;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_log_in_page);

        idET=findViewById(R.id.idET);
        loginBtn=findViewById(R.id.loginBtn);
        signUpTV=findViewById(R.id.signUpTV);
        otpEt=findViewById(R.id.otpEt);
        verifyBtn=findViewById(R.id.verifyBtn);

        sharedPreferences=getSharedPreferences("SHAREDPREFERECEFILE",MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();

        //toast
        Toast.makeText(getBaseContext(),sharedPreferences.getString("UserType",null),Toast.LENGTH_LONG).show();

        //goto sign up activity
        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),CombineSignUp.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num=idET.getText().toString().trim();
                if(num.isEmpty()){
                    idET.setError("Enter mobile number ");
                    idET.requestFocus();
                    return;
                }
                loginBtn.setVisibility(View.INVISIBLE);
                signUpTV.setVisibility(View.INVISIBLE);
                otpEt.setVisibility(View.VISIBLE);
                verifyBtn.setVisibility(View.VISIBLE);
                idET.setInputType(InputType.TYPE_NULL);
                //verify otp
                //login from Firebase mAuth
                //start main activity
                //

            }
        });
    }
}
