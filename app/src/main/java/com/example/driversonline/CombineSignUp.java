package com.example.driversonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class CombineSignUp extends AppCompatActivity {
    EditText nameEt,phoneNumberEt,otpEt,licenseEt;
    Spinner cityspinner;
    Button signUpBtn,verifyBtn;
    TextView textView;
    progress progress;
    String vId;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    DatabaseReference mdb= FirebaseDatabase.getInstance().getReference();
    String num,type,name,city,lno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_sign_up);

        progress=new progress(this);
        sharedPreferences=getSharedPreferences("SHAREDPREFERECEFILE",MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        type=sharedPreferences.getString("UserType",null);

        textView=findViewById(R.id.textView);
        nameEt=findViewById(R.id.nameET);
        phoneNumberEt=findViewById(R.id.phoneNumberEt);
        cityspinner=findViewById(R.id.cityspinner);
        signUpBtn=findViewById(R.id.signUpBtn);
        verifyBtn=findViewById(R.id.verifyBtn);
        otpEt=findViewById(R.id.otpEt);
        licenseEt=findViewById(R.id.licenseEt);
        licenseEt.setVisibility(View.INVISIBLE);

        mAuth=FirebaseAuth.getInstance();


        if(sharedPreferences.getString("UserType",null).equals("Driver")){
            //user is driver , inflate and replace view stub
            textView.setText("You can Drive?\nSign up here.");
            licenseEt.setVisibility(View.VISIBLE);
        }else{
            //user is owner keep view stub
        }

        //Toast.makeText(getBaseContext(),sharedPreferences.getString("UserType",null),Toast.LENGTH_LONG).show();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prefix="+91";
                num=phoneNumberEt.getText().toString().trim();
                name=nameEt.getText().toString().trim();
                city=cityspinner.getSelectedItem().toString();
                String lno="";
                if(name.isEmpty()){
                    nameEt.requestFocus();
                    nameEt.setError("Require");
                    Toast.makeText(getBaseContext(),"name empty",Toast.LENGTH_LONG).show();
                    return;
                }
                if(type.equals("Driver")){
                    lno=licenseEt.getText().toString().trim();
                    if(lno.isEmpty()){
                        licenseEt.requestFocus();
                        licenseEt.setError("Require");
                        return;
                    }

                }
                if(num.isEmpty() || num.length()<10){
                    phoneNumberEt.setError("phone number required!");
                    phoneNumberEt.requestFocus();
                    progress.dissmiss();
                    return;
                }
                num=prefix+num;
                progress.show();
///////////////////////////////////////////////////
                Query query=mdb.child("user");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("Driver/"+num)||dataSnapshot.hasChild("Owner/"+num))
                        {
                            Toast.makeText(getBaseContext(),"\n User alreay exists",Toast.LENGTH_LONG).show();
                            phoneNumberEt.setError("User Already Exists.");
                            phoneNumberEt.requestFocus();
                            progress.dissmiss();
                        }else {
                            //forward details from sign up page
                            //send otp[verify otp -- start  new activity]
                            sendOtp(num);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
///////////////////////////////////////////////////
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final String code=otpEt.getText().toString().trim();
               if(code.isEmpty()){
                    otpEt.setError("enter code");
                    otpEt.requestFocus();
                    progress.dissmiss();
                    return;
                }
               progress.show();
                verify(code);
            }
        });
    }

    private void sendOtp(String phonenumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber,120, TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack);

        signUpBtn.setVisibility(View.INVISIBLE);
        nameEt.setInputType(InputType.TYPE_NULL);
        nameEt.setClickable(false);
        nameEt.setFocusableInTouchMode(false);
        cityspinner.setClickable(false);
        cityspinner.setEnabled(false);
        phoneNumberEt.setInputType(InputType.TYPE_NULL);
        phoneNumberEt.setClickable(false);
        phoneNumberEt.setFocusableInTouchMode(false);
        if(type.equals("Driver"))
                {
                    licenseEt.setInputType(InputType.TYPE_NULL);
                    licenseEt.setClickable(false);
                    licenseEt.setFocusableInTouchMode(false);
                }
        otpEt.setVisibility(View.VISIBLE);
        verifyBtn.setVisibility(View.VISIBLE);
        otpEt.requestFocus();
        progress.dissmiss();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack=new
            PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    vId=s;
                    //Toast.makeText(getBaseContext(),"S : "+s,Toast.LENGTH_LONG).show();
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                    String code=phoneAuthCredential.getSmsCode();
                    otpEt.setText(code);
                    if(code!=null)
                        verify(code);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            };

    private void verify(String code){

        try {
            PhoneAuthCredential credential=PhoneAuthProvider.getCredential(vId,code);
            signInwithCredential(credential);
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();
            Log.i("exception",e.toString());
            progress.dissmiss();
        }

    }

    private void signInwithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(getBaseContext(),"Task Successful.."+type,Toast.LENGTH_LONG).show();
                    //check user already exists or not
                    //Query query=mdb.child("user").child(type).orderByChild("num").equalTo(num);
                    String UserType=type;




                    addnewUser(num,name,city,UserType,lno);

                }
                else {
                    otpEt.setError("Enter valid OTP");
                    progress.dissmiss();
                    Toast.makeText(getBaseContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void addnewUser(String num,String name,String city,String type,String lno){
        user user=new user(num,name,city,type,lno);
        mdb.child("user").child(type).child(num).setValue(user);
        Toast.makeText(getBaseContext(),"new user added",Toast.LENGTH_LONG).show();
        //start new activity
        Intent intent=new Intent(getBaseContext(),profileMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        progress.dissmiss();
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
