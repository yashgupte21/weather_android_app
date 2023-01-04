package com.example.openweatherapp;

import java.io.Serializable;

//GETS AND SETS THE HOURLY WEATHER DATA FOR 48 HOURLY DATA
public class TimeRecord implements Serializable {

    private String  hw_day;
    private String  hw_time;
    private String  hw_weather_icon;
    private String  hw_temp;
    private String hw_weather_desc;
    private Long hw_dt;



    TimeRecord(String  hw_day, String  hw_time,String  hw_weather_icon,String  hw_temp,String hw_weather_desc,Long hw_dt){
        this.hw_day = hw_day;
        this.hw_time = hw_time;
        this.hw_weather_icon = hw_weather_icon;
        this.hw_temp = hw_temp;
        this.hw_weather_desc = hw_weather_desc;
        this.hw_dt = hw_dt;


    }

    public String getHw_day(){ return hw_day; }

    public void setHw_day(String hw_day) {
        this.hw_day = hw_day;
    }

    public String getHw_time(){ return hw_time; }

    public void setHw_time(String hw_time) {
        this.hw_time = hw_time;
    }

    public String getHw_weather_icon() { return hw_weather_icon;}

    public void setHw_weather_icon(String hw_weather_icon) {
        this.hw_weather_icon = hw_weather_icon;
    }

    public String getHw_temp() {
        return hw_temp;
    }

    public void setHw_temp(String hw_temp) {
        this.hw_temp = hw_temp;
    }

    public String getHw_weather_desc() {
        return hw_weather_desc;
    }

    public void setHw_weather_desc(String hw_weather_desc) {
            this.hw_weather_desc = hw_weather_desc; }

    public Long getHw_dt() {
        return hw_dt;
    }
}
