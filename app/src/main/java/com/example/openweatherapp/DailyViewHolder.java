package com.example.openweatherapp;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;

public class DailyViewHolder extends RecyclerView.ViewHolder {

    public TextView dw_daydate;
    public TextView dw_minmax_temp;
    public TextView dw_weather_desc;
    public TextView dw_precip;
    public TextView dw_uvindex;
    public TextView dw_morning_temp;
    public TextView dw_day_temp;
    public TextView dw_evening_temp;
    public TextView dw_night_temp;
    public TextView dw_morning_time;
    public TextView dw_day_time;
    public TextView dw_evening_time;
    public TextView dw_night_time;
    public ImageView dw_weather_icon;



    public DailyViewHolder(@NonNull View itemView) {
        super(itemView);

        dw_daydate = itemView.findViewById(R.id.dw_daydate_tv);
        dw_minmax_temp = itemView.findViewById(R.id.dw_minmax_temp_tv);
        dw_weather_desc = itemView.findViewById(R.id.dw_weather_desc_tv);
        dw_precip = itemView.findViewById(R.id.dw_precip_tv);
        dw_uvindex = itemView.findViewById(R.id.dw_uvindex_tv);
        dw_morning_temp = itemView.findViewById(R.id.dw_morning_temp_tv);
        dw_day_temp = itemView.findViewById(R.id.dw_day_temp_tv);
        dw_evening_temp = itemView.findViewById(R.id.dw_evening_temp_tv);
        dw_night_temp = itemView.findViewById(R.id.dw_night_temp_tv);
        dw_morning_time = itemView.findViewById(R.id.dw_morning_time_tv);
        dw_day_time = itemView.findViewById(R.id.dw_day_time_tv);
        dw_evening_time = itemView.findViewById(R.id.dw_evening_time_tv);
        dw_night_time = itemView.findViewById(R.id.dw_night_time_tv);
        dw_weather_icon = itemView.findViewById(R.id.dw_weather_icon_imv);

    }
}
