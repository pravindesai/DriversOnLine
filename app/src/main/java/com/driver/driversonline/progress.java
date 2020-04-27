package com.driver.driversonline;

import android.app.ProgressDialog;
import android.content.Context;

public class progress {
    private ProgressDialog pd;
    public progress(Context context){
        pd = new ProgressDialog(context);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setMessage("Loading...");
    }
    public void show(){
        pd.show();
    }
    public void dissmiss(){
        pd.dismiss();
    }
}
