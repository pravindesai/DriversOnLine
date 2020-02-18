package com.example.driversonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CombineSignUp extends AppCompatActivity {
    EditText nameEt,phoneNumberEt,passEt;
    Spinner cityspinner;
    Button signUpBtn;
    String UserType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_sign_up);

        nameEt=findViewById(R.id.nameET);
        phoneNumberEt=findViewById(R.id.phoneNumberEt);
        cityspinner=findViewById(R.id.cityspinner);
        signUpBtn=findViewById(R.id.signUpBtn);
        UserType=getIntent().getStringExtra("UserType");
        Toast.makeText(getBaseContext(),UserType,Toast.LENGTH_LONG).show();





        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prefix="+91";
                String num=phoneNumberEt.getText().toString();
                if(num.isEmpty() || num.length()<10){
                    phoneNumberEt.setError("phone number required!");
                    phoneNumberEt.requestFocus();
                    return;
                }
                     num=prefix+num;
                    Intent verifyOtp=new Intent(getBaseContext(),OtpActivity.class);
                    verifyOtp.putExtra("UserType",UserType);
                    verifyOtp.putExtra("name",nameEt.getText().toString());
                    verifyOtp.putExtra("city",cityspinner.getSelectedItem().toString());
                    verifyOtp.putExtra("phonenumber",num);
                    startActivity(verifyOtp);

            }
        });


    }
}
