package com.example.openweatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.List;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyViewHolder> {

    private List<TimeRecord> timeRecordList;
    private  MainActivity mainActivity;
    private static final String TAG = "HourlyAdapter";
    private Context mContext;

    HourlyAdapter(List<TimeRecord> timeRecordList, MainActivity ma ){
        this.timeRecordList = timeRecordList;
        this.mainActivity =  ma;
    }
    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Creating a New HourlyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_weather_list_cols,parent,false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new HourlyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
        TimeRecord timeRecord = timeRecordList.get(position);
        holder.hw_day.setText(timeRecord.getHw_day());
        holder.hw_time.setText(timeRecord.getHw_time());

        int iconResId = mainActivity.getResources().getIdentifier(timeRecord.getHw_weather_icon(), "drawable", mainActivity.getPackageName());
        holder.hw_weather_icon.setImageResource(iconResId);

        holder.hw_temp.setText(timeRecord.getHw_temp()+"°"+(mainActivity.fahrenheit ? "C" : "F"));
        holder.hw_weather_desc.setText(timeRecord.getHw_weather_desc());

        holder.itemView.setOnClickListener(v -> {
            ((MainActivity) mainActivity).openCal(timeRecord.getHw_dt());
        });

    }


    @Override
    public int getItemCount() {
        return timeRecordList.size();
    }
}
