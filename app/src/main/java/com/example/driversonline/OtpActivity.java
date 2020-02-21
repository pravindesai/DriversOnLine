package com.example.driversonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
/*class user{
    public String num;
    public String name;
    public String city;
    public  String type;
    public String lno;

    public user() {
    }

    public user(String num, String name, String city, String type, String lno) {
        this.num = num;
        this.name = name;
        this.city = city;
        this.type = type;
        this.lno = lno;
    }
}*/
public class OtpActivity extends AppCompatActivity {
    EditText otpEt;
    Button goBtn;
    private String vId;
    FirebaseAuth mAuth;
    String num;
    DatabaseReference mdb=FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otpEt=findViewById(R.id.otpEt);
        goBtn=findViewById(R.id.goBtn);
        mAuth=FirebaseAuth.getInstance();


        num=getIntent().getStringExtra("phonenumber");
        Toast.makeText(getBaseContext(),getIntent().getStringExtra("UserType"),Toast.LENGTH_LONG).show();
        sentOtp(num);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=otpEt.getText().toString().trim();
                if(code.isEmpty()){
                    otpEt.setError("enter code");
                    otpEt.requestFocus();
                    return;
                }
                verify(code);
            }
        });

    }

    private void verify(String code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(vId,code);
        signInwithCredential(credential);
    }

    private void signInwithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //start new Activity
                    //kill all activities
                    Query query=null;
                    query=mdb.child("user").orderByChild("num").equalTo(num);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Toast.makeText(getBaseContext(),"User alreay exists",Toast.LENGTH_LONG).show();
                                //start new Activity
                                Toast.makeText(getBaseContext(),"Successful start Activity",Toast.LENGTH_LONG).show();

                                return;
                            }else{
                                //forward details from sign up page
                                String name=getIntent().getStringExtra("name");
                                String UserType=getIntent().getStringExtra("UserType");
                                String city=getIntent().getStringExtra("city");
                                addnewUser(num,name,city,UserType);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else {
                    Toast.makeText(getBaseContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void addnewUser(String num,String name,String city,String type){
        user user=new user(num,name,city,type,"123");
        mdb.child("user").child(num).setValue(user);
        Toast.makeText(getBaseContext(),"new user added",Toast.LENGTH_LONG).show();
        Toast.makeText(getBaseContext(),"Successful start Activity",Toast.LENGTH_LONG).show();
    }

    private void sentOtp(String phonenumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber,120, TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack=new
            PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    vId=s;
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
}
