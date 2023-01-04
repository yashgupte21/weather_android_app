package com.example.openweatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class DailyActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private  ArrayList<DayRecord> dayRecordArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DailyAdapter dailyAdapter;
    private static final String TAG ="Daily activity";
    private boolean fahrenheit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_daily);

        recyclerView = findViewById(R.id.daily_weather_rv);
        dailyAdapter = new DailyAdapter(dayRecordArrayList, this);
        recyclerView.setAdapter(dailyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "onCreate: "+ dailyAdapter);

        Intent intent = getIntent();
        if(intent.hasExtra("location")){
            String city = intent.getStringExtra("location");
            getSupportActionBar().setTitle(city);
        }

        if (intent.hasExtra("temp")) {
            ArrayList<DayRecord> dayTemp = (ArrayList<DayRecord>) intent.getSerializableExtra("temp");
            dayRecordArrayList.addAll(dayTemp);
            dailyAdapter.notifyDataSetChanged();

        }



    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}
