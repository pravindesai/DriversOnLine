package com.example.driversonline;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Cust_Dialog {
    private Dialog MyDialog;
    private Calendar c;
    private Button ok,close,startDateBtn,endDateBtn;
    private int flag1,flag2=0;
    int y1,y2;
    int m1,m2;
    int d1,d2;
    String type,CurrentUserName,CurrentUserCity;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    SharedPreferences sharedPreferences;
    DatabaseReference mdb= FirebaseDatabase.getInstance().getReference();


    public void ShowCustomeDialog(final user u,View view) {
        MyDialog= new Dialog(view.getContext());
        MyDialog.setContentView(R.layout.customedialog);

        sharedPreferences=view.getContext().getSharedPreferences("SHAREDPREFERECEFILE", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        type=sharedPreferences.getString("UserType",null);
        CurrentUserName=sharedPreferences.getString("CurrentUserName",null);
        CurrentUserCity=sharedPreferences.getString("CurrentUserCity",null);
        //t.findViewById(R.id.textView);
       // t2.findViewById(R.id.textView2);

        ok = (Button) MyDialog.findViewById(R.id.ok);
        close = (Button) MyDialog.findViewById(R.id.close);
        startDateBtn=(Button) MyDialog.findViewById(R.id.startDateBtn);
        endDateBtn=(Button) MyDialog.findViewById(R.id.endDateBtn);
        ok.setEnabled(true);
        close.setEnabled(true);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag1==0||flag2==0){
                    Toast.makeText(v.getContext(),"select all dates",Toast.LENGTH_SHORT).show();                }
                else {
                    //ADD new Booking to DATABASE
                    /*node[id]-->|------startDate
                                 |------endDate
                                 |------owner number i.e. current user number
                                 |------owner city
                                 |------Driver num/id i.e user u
                                 |------Action [None/Accepted/Rejected]
                        */

                        booking b=new booking(d1+"/"+m1+"/"+y1,
                                            d2+"/"+m2+"/"+y2,
                                            mAuth.getCurrentUser().getPhoneNumber(),
                                            CurrentUserName,
                                            CurrentUserCity,
                                            u.num,
                                            "None");
                        //mdb.push().getKey()     generate Unique key
                        mdb.child("booking").child(mdb.push().getKey()).setValue(b);
                        Toast.makeText(v.getContext(),"Request Added",Toast.LENGTH_SHORT).show();

                    //Toast.makeText(v.getContext(),d1+"/"+m1+"/"+y1,Toast.LENGTH_SHORT).show();
                    //Toast.makeText(v.getContext(),d2+"/"+m2+"/"+y2,Toast.LENGTH_SHORT).show();
                    MyDialog.dismiss();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickStartDate(v);
            }
        });
        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickEndDate(v);
            }
        });
        MyDialog.show();
    }

    public void pickStartDate(View view) {
        c=Calendar.getInstance();
        y1=c.get(Calendar.YEAR);
        m1=c.get(Calendar.MONTH);
        d1=c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        y1=year;
                        m1=month+1;
                        d1=dayOfMonth;
                        flag1=1;
                       // Toast.makeText(getBaseContext(),d1+"/"+m1+"/"+y1,Toast.LENGTH_SHORT).show();
                    }
                },y1,m1,d1);
        datePickerDialog.show();

    }
    public void pickEndDate(View view) {
        c = Calendar.getInstance();
        y2=c.get(Calendar.YEAR);
        m2=c.get(Calendar.MONTH);
        d2=c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        y2=year;
                        m2=month+1;
                        d2=dayOfMonth;
                        flag2=1;
                        //Toast.makeText(getBaseContext(),d2+"/"+m2+"/"+y2,Toast.LENGTH_SHORT).show();
                    }
                },y2,m2,d2);
        datePickerDialog.show();
    }
}
