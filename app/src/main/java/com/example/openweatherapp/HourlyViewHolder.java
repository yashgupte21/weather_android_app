package com.example.openweatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HourlyViewHolder extends RecyclerView.ViewHolder {

    public TextView hw_day;
    public TextView hw_time;
    public ImageView hw_weather_icon;
    public TextView hw_temp;
    public TextView hw_weather_desc;

    public HourlyViewHolder(@NonNull View itemView) {
        super(itemView);

        hw_day = itemView.findViewById(R.id.hw_day_tv);
        hw_time = itemView.findViewById(R.id.hw_time_tv);
        hw_weather_icon = itemView.findViewById(R.id.hw_weathericon_imv);
        hw_temp = itemView.findViewById(R.id.hw_temp_tv);
        hw_weather_desc = itemView.findViewById(R.id.hw_weather_desc_tv);
    }
}
