package com.example.openweatherapp;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Weather implements Serializable {

    private final String latitude;
    private final String longitude;
    private final String dt;
    private final String current_temp;
    private final String current_feels_like;
    private final String current_weather_desc;
    private final String current_wind;
    private final String current_wind_speed;
    private final String current_wind_gust;
    private final String current_humidity;
    private final String current_uv_idx;
    private final String current_rain_snow;
    private final String current_visibility;
    private final String current_sunrise;
    private final String current_sunset;
    private final String current_clouds;
    private final String morning_temp;
    private final String daytime_temp;
    private final String evening_temp;
    private final String night_temp;
    private final String current_weather_icon;
    private final List<TimeRecord> timeRecordArrayList;
    private  final List<DayRecord> dayRecordList;


    public Weather(String latitude, String longitude, String dt, String current_temp, String current_feels_like, String current_weather_desc, String current_wind, String current_wind_speed, String current_wind_gust, String current_humidity, String current_uv_idx, String current_rain_snow, String current_visibility, String current_sunrise, String current_sunset, String current_clouds, String morning_temp, String daytime_temp, String evening_temp, String night_temp, String current_weather_icon, List<TimeRecord> timeRecordArrayList, List<DayRecord> dayRecordList) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dt = dt;
        this.current_temp = current_temp;
        this.current_feels_like = current_feels_like;
        this.current_weather_desc = current_weather_desc;
        this.current_wind = current_wind;
        this.current_wind_speed = current_wind_speed;
        this.current_wind_gust = current_wind_gust;
        this.current_humidity = current_humidity;
        this.current_uv_idx = current_uv_idx;
        this.current_rain_snow = current_rain_snow;
        this.current_visibility = current_visibility;
        this.current_sunrise = current_sunrise;
        this.current_sunset = current_sunset;
        this.current_clouds = current_clouds;
        this.morning_temp = morning_temp;
        this.daytime_temp = daytime_temp;
        this.evening_temp = evening_temp;
        this.night_temp = night_temp;
        this.current_weather_icon = current_weather_icon;
        this.timeRecordArrayList = timeRecordArrayList;
        this.dayRecordList = dayRecordList;
    }


    String getLatitude(){return latitude;}

    String getLongitude(){ return  longitude;}

    String getDt(){ return dt;}

    String getCurrent_temp() { return current_temp;}

    String getCurrent_feels_like() { return current_feels_like;}

    String getCurrent_weather_desc() { return current_weather_desc;}

    String getCurrent_wind() { return current_wind;}

    String getCurrent_wind_speed(){ return current_wind_speed;}

    String getCurrent_wind_gust(){ return current_wind_gust;}


    String getCurrent_humidity(){ return current_humidity;}

    String getCurrent_uv_idx(){ return current_uv_idx;}

    String getCurrent_rain_snow(){ return current_rain_snow;}

    String getCurrent_visibility(){ return current_visibility;}

    String getMorning_temp(){ return morning_temp;}

    String getDaytime_temp(){ return daytime_temp;}

    String getEvening_temp(){ return evening_temp;}

    String getNight_temp(){ return night_temp;}
//
//    String getMorning_time(){ return morning_time;}
//
//    String getDaytime_time(){ return daytime_time;}
//
//    String getEvening_time(){ return evening_time;}
//
//    String getNight_time(){ return night_time;}

    String getCurrent_sunrise(){ return current_sunrise;}

    String getCurrent_sunset(){ return current_sunset;}

    String getCurrent_clouds(){ return  current_clouds;}

    String getCurrent_weather_icon() { return  current_weather_icon;}

    List<TimeRecord> getTimeRecordArrayList(){ return timeRecordArrayList;}

    List<DayRecord> getDayRecordList(){ return  dayRecordList;}
}
