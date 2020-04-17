package com.example.driversonline;

import android.app.ProgressDialog;
import android.content.Context;

public class progress {
    private ProgressDialog pd;
    progress(Context context){
        pd = new ProgressDialog(context);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setMessage("Loading...");
    }
    void show(){
        pd.show();
    }
    void dissmiss(){
        pd.dismiss();
    }
}
