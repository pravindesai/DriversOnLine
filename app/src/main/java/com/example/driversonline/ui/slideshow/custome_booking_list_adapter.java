package com.example.driversonline.ui.slideshow;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.driversonline.R;
import com.example.driversonline.booking;
import java.util.ArrayList;

public class custome_booking_list_adapter extends BaseAdapter {
    LayoutInflater inflter;
    public ArrayList<booking> bookings;

    public custome_booking_list_adapter(Context applicationContext, ArrayList<booking>bookings) {
        this.bookings = bookings;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        //return bookings.size();
        return (bookings == null) ? 0 : bookings.size();

    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.listview_content_for_bookings, null);
        TextView Onumtv = (TextView) view.findViewById(R.id.Onumtv);
        TextView Dnumtv = (TextView) view.findViewById(R.id.Dnumtv);
        TextView Citytv = (TextView) view.findViewById(R.id.Citytv);
        TextView Startdatetv = (TextView) view.findViewById(R.id.Startdatetv);
        TextView Enddatetv = (TextView) view.findViewById(R.id.Enddatetv);

        booking b=bookings.get(i);
        Onumtv.setText(b.Onum);
        Dnumtv.setText(b.Dnum);
        Citytv.setText(b.Ocity);
        Startdatetv.setText(b.startDate);
        Enddatetv.setText(b.endDate);
        return view;
    }
}