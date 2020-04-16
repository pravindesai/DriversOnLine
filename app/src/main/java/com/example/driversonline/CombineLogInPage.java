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

public class CombineLogInPage extends AppCompatActivity {
    EditText idET,otpEt;
    String vId,type;
    String num;
    Button loginBtn,verifyBtn;
    TextView signUpTV;
    DatabaseReference mdb= FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
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
        type=sharedPreferences.getString("UserType",null);
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
                String prefix="+91";
                num=idET.getText().toString().trim();
                if(num.isEmpty()){
                    idET.setError("Enter mobile number ");
                    idET.requestFocus();
                    return;
                }
                num=prefix+num;

                ////////////////////////////////////
                Query query=mdb.child("user").child(type).orderByChild("num").equalTo(num);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Toast.makeText(getApplicationContext(),""+dataSnapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();
                        if(dataSnapshot.getChildrenCount()==0){
                            idET.setError("Enter valid mobile number ");
                            idET.requestFocus();
                        }else {
                            sendOtp(num);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                ///////////////////////////////////
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String code=otpEt.getText().toString().trim();
                if(code.isEmpty()){
                    otpEt.setError("enter code");
                    otpEt.requestFocus();
                    return;
                }
                verify(code);
            }
        });
    }

    private void sendOtp(String phonenumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber,120, TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack);
        Toast.makeText(getApplicationContext(),"OTP sent to "+idET.getText().toString(),Toast.LENGTH_LONG).show();
        loginBtn.setVisibility(View.INVISIBLE);
        signUpTV.setVisibility(View.INVISIBLE);
        otpEt.setVisibility(View.VISIBLE);
        verifyBtn.setVisibility(View.VISIBLE);
        idET.setInputType(InputType.TYPE_NULL);
        idET.setClickable(false);
        idET.setFocusableInTouchMode(false);
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
        }
    }

    private void signInwithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getBaseContext(),"Signed in succesful..\n"+mAuth.getCurrentUser().getPhoneNumber()+"\n"+type,Toast.LENGTH_LONG).show();
                   /* if(type.equals("Driver")){
                        //start driver main activty
                    }else if(type.equals("Owner")){
                        //start owner activity
                    }*/
                   Intent intent=new Intent(getBaseContext(),profileMainActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent);
                   finish();
                }
                else {
                    Toast.makeText(getBaseContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
