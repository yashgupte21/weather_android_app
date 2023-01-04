package com.example.openweatherapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DailyAdapter extends RecyclerView.Adapter<DailyViewHolder> {

    private List<DayRecord> dayRecordList;
    private DailyActivity dailyAcitivty;
    public boolean fahrenheit = false;
    private static final String TAG = "DailyAdapter";

    DailyAdapter(List<DayRecord> dayRecordList, DailyActivity da){
        this.dayRecordList = dayRecordList;
        this.dailyAcitivty = da;
    }


    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Creating a New DailyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_weather_list_rows,parent,false);

        itemView.setOnClickListener(dailyAcitivty);
        itemView.setOnLongClickListener(dailyAcitivty);
        return new DailyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {

        DayRecord dayRecord =  dayRecordList.get(position);

        holder.dw_daydate.setText(dayRecord.getDw_daydate());
        holder.dw_minmax_temp.setText(dayRecord.getDw_minmax_temp());
        holder.dw_weather_desc.setText(dayRecord.getDw_weather_desc());
        holder.dw_precip.setText(dayRecord.getDw_pop());
        holder.dw_uvindex.setText(dayRecord.getDw_uvindex());
        holder.dw_morning_temp.setText(dayRecord.getDw_morning_temp());
        holder.dw_day_temp.setText(dayRecord.getDw_day_temp());
        holder.dw_evening_temp.setText(dayRecord.getDw_evening_temp());
        holder.dw_night_temp.setText(dayRecord.getDw_night_temp());

        int iconResId = dailyAcitivty.getResources().getIdentifier(dayRecord.getDw_weather_icon(), "drawable", dailyAcitivty.getPackageName());
        holder.dw_weather_icon.setImageResource(iconResId);

    }

    @Override
    public int getItemCount() {
        return dayRecordList.size();
    }
}
