package com.example.openweatherapp;

import android.graphics.Bitmap;

import java.io.Serializable;

//GETS AND SETS THE DAILY WEATHER DATA FOR NEXT 10 DAYS FROM CURRENT DAY
public class DayRecord implements Serializable {
    private String dw_daydate;
    private String dw_minmax_temp;
    private String dw_morning_temp;
    private String dw_day_temp;
    private String dw_evening_temp;
    private String dw_night_temp;
    private String dw_weather_id;
    private String dw_weather_desc;
    private String dw_weather_icon;
    private  String dw_pop;
    private String dw_uvindex;

    public DayRecord() {
    }

    public DayRecord(String dw_daydate, String dw_minmax_temp, String dw_morning_temp, String dw_day_temp, String dw_evening_temp, String dw_night_temp, String dw_weather_id, String dw_weather_desc, String dw_weather_icon, String dw_pop, String dw_uvindex) {
        this.dw_daydate = dw_daydate;
        this.dw_minmax_temp = dw_minmax_temp;
        this.dw_morning_temp = dw_morning_temp;
        this.dw_day_temp = dw_day_temp;
        this.dw_evening_temp = dw_evening_temp;
        this.dw_night_temp = dw_night_temp;
        this.dw_weather_id = dw_weather_id;
        this.dw_weather_desc = dw_weather_desc;
        this.dw_weather_icon = dw_weather_icon;
        this.dw_pop = dw_pop;
        this.dw_uvindex = dw_uvindex;
    }

    public String getDw_daydate() {
        return dw_daydate;
    }

    public void setDw_daydate(String dw_daydate){
        this.dw_daydate = dw_daydate;
    }

    public String getDw_minmax_temp() {
        return dw_minmax_temp;
    }

    public void setDw_minmax_temp(String dw_minmax_temp) {
        this.dw_minmax_temp = dw_minmax_temp;
    }

    public String getDw_weather_desc() {
        return dw_weather_desc;
    }

    public void setDw_weather_desc(String dw_weather_desc) {
        this.dw_weather_desc = dw_weather_desc;
    }

    public String getDw_uvindex() {
        return dw_uvindex;
    }

    public void setDw_uvindex(String dw_uvindex) {
        this.dw_uvindex = dw_uvindex;
    }

    public String getDw_morning_temp() {
        return dw_morning_temp;
    }

    public void setDw_morning_temp(String dw_morning_temp) {
        this.dw_morning_temp = dw_morning_temp;
    }

    public String getDw_day_temp() {
        return dw_day_temp;
    }

    public void setDw_day_temp(String dw_day_temp) {
        this.dw_day_temp = dw_day_temp;
    }

    public String getDw_evening_temp() {
        return dw_evening_temp;
    }

    public void setDw_evening_temp(String dw_evening_temp) {
        this.dw_evening_temp = dw_evening_temp;
    }

    public String getDw_night_temp() {
        return dw_night_temp;
    }

    public void setDw_night_temp(String dw_night_temp) {
        this.dw_night_temp = dw_night_temp;
    }

    public String getDw_weather_id() {
        return dw_weather_id;
    }

    public void setDw_weather_id(String dw_weather_id) {
        this.dw_weather_id = dw_weather_id;
    }

    public String getDw_pop() {
        return dw_pop;
    }

    public void setDw_pop(String dw_pop) {
        this.dw_pop = dw_pop;
    }

    public String getDw_weather_icon() {
        return dw_weather_icon;
    }

    public void setDw_weather_icon(String dw_weather_icon) {
        this.dw_weather_icon = dw_weather_icon;
    }

}

