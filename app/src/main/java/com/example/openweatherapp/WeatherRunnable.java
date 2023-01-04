package com.example.openweatherapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class WeatherRunnable implements Runnable{
    private static final String TAG = "WeatherDownloadRunnable";

    private final MainActivity mainActivity;
    private final String city;
    private final String latitude;
    private final String longitude;
    private final boolean fahrenheit;


    
    private static final String weatherURL = "https://api.openweathermap.org/data/2.5/onecall?";//lat=41.8675766&lon=-87.616232&appid=c7c7fb5b6f95f4dc7dcea144c805e686&units=imperial&lang=en&exclude=minutely";
    private static final String yourAPIKey = "c7c7fb5b6f95f4dc7dcea144c805e686";


    public WeatherRunnable(MainActivity mainActivity,String city,String latitude,String longitude , boolean fahrenheit) {
        this.mainActivity = mainActivity;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fahrenheit = fahrenheit;
    }

    @Override
    public void run() {

        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();

        buildURL.appendQueryParameter("lat", latitude);
        buildURL.appendQueryParameter("lon", longitude);
        buildURL.appendQueryParameter("units", (fahrenheit ? "metric" : "imperial"));
        buildURL.appendQueryParameter("appid", yourAPIKey);
        buildURL.appendQueryParameter("lang", "en");
        buildURL.appendQueryParameter("exclude","minutely");

        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(urlToUse);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                InputStream is = connection.getErrorStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                handleError(sb.toString());
                return;
            }

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            Log.d(TAG, "doInBackground: " + sb.toString());
        }
        catch (Exception e){
            Log.e(TAG, "doInBackground: ", e);
            handleResults(null);
            return;
        }
        handleResults(sb.toString());
    }

    public void handleError (String s){
        String msg = "Error: ";
        try {
            JSONObject jObjMain = new JSONObject(s);
            msg += jObjMain.getString("message");

        } catch (JSONException e) {
            msg += e.getMessage();
        }

        String finalMsg = String.format("%s (%s , %s)", msg, latitude,longitude);
        mainActivity.runOnUiThread(() -> mainActivity.handleError(finalMsg));
    }

    public void handleResults(final String jsonString) {
        final Weather w = parseJSON1(jsonString);
        mainActivity.runOnUiThread(() -> mainActivity.updateData(w));
    }

    public static String getDateTime(long dt, long timeZone, String desired_pattern) {
        LocalDateTime ldt =
                LocalDateTime.ofEpochSecond(dt + timeZone, 0, ZoneOffset.UTC);
        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern(desired_pattern, Locale.getDefault());
        String formattedTimeString = ldt.format(dtf);
        return formattedTimeString;
    }



    private Weather parseJSON1(String s){
        try{
            //json reader
            JSONObject jObjMain = new JSONObject(s);
            String latitude = jObjMain.getString("lat");
            String longitude = jObjMain.getString("lon");

            //convert lat lon into city and country and pass those values in -> Main activity
            String city="";
            String country = "";

            String timezone = jObjMain.getString("timezone");
            long timezone_offset = jObjMain.getLong("timezone_offset");


            // "current" section
            JSONObject jCurrent = jObjMain.getJSONObject("current");

            //convert current_dt into string format
            long current_date = jCurrent.getLong("dt");
            //String current_dt = new SimpleDateFormat("EEE MMM dd h:mm a, yyyy", Locale.getDefault()).format(new Date(current_date * 1000));
            String current_dt = getDateTime(current_date,timezone_offset, "EEE MMM dd h:mm a, yyyy");

            //current_sunrise and current_sunset into string format
            long current_sunrise_long = jCurrent.getLong("sunrise");
            String current_sunrise = getDateTime(current_sunrise_long,timezone_offset, "hh:mm");
            long current_sunset_long = jCurrent.getLong("sunset");
            String current_sunset = getDateTime(current_sunset_long,timezone_offset, "hh:mm");

            String current_temp =  jCurrent.getString("temp");

            Integer current_feels_like_int = (int) Math.round(jCurrent.getDouble("feels_like"));
            String current_feels_like = current_feels_like_int.toString();
            String current_pressure = jCurrent.getString("pressure");
            String current_humidity = jCurrent.getString("humidity");

            Integer current_uv_idx_int = (int) Math.round(jCurrent.getDouble("uvi"));
            String current_uv_idx = current_uv_idx_int.toString();
            String current_clouds = jCurrent.getString("clouds"); //75% clouds in main activity

            long current_visibility_long = jCurrent.getLong("visibility");
            String current_visibility = String.valueOf(current_visibility_long/1000);

            // convert wind degree into wind direction like N,S,E,W,etc with our given func return current_wind string
            String current_wind = jCurrent.getString("wind_deg");


            String current_wind_speed = jCurrent.getString("wind_speed"); //convert wind speed to mph

            String current_wind_gust;
            if(jCurrent.has("wind_gust")){
                current_wind_gust = jCurrent.getString("wind_gust");
            }
            else{
                current_wind_gust ="";
            }

            //weather
            JSONArray weather = jCurrent.getJSONArray("weather");
            JSONObject jWeather = (JSONObject) weather.get(0);
            Integer weather_id = jWeather.getInt("id");
            String weather_main = jWeather.getString("main");
            String weather_desc = jWeather.getString("description");

            //Convert this weather icon into its equivalent bitmap from drawable class ( weather images)
            String weather_icon = "_"+jWeather.getString("icon");
            //Bitmap weather_icon ;

            // check if rain or snow is present int json only then create its object
            // rain  - part of current

            String current_rain_snow = "";
            if(jCurrent.has("rain"))
            {
                JSONObject jRain = jCurrent.getJSONObject("rain");
                String current_rain = jRain.getString("1h");
                current_rain_snow =  current_rain;
            }

            if(jCurrent.has("snow")){
                // snow - part of current
                JSONObject jSnow = jCurrent.getJSONObject("snow");
                String current_snow = jSnow.getString("1h");
                current_rain_snow = current_snow;
            }

            JSONArray daily = jObjMain.getJSONArray("daily");
            JSONObject jDaily = (JSONObject) daily.get(0);
            JSONObject jDailyTemp = jDaily.getJSONObject("temp");
            String dw_morning_temp = jDailyTemp.getString("morn");
            String dw_day_temp = jDailyTemp.getString("day");
            String dw_evening_temp = jDailyTemp.getString("eve");
            String dw_night_temp = jDailyTemp.getString("night");


            //hourly:

            ArrayList<TimeRecord> timelist = new ArrayList<>();
            // json reader
            JSONArray hourly = jObjMain.getJSONArray("hourly");
            for (int i = 0; i < hourly.length(); i++) {
                JSONObject jHourly = (JSONObject) hourly.get(i); //next 48 hours of data

                long hw_dt = jHourly.getLong("dt");
                //String hw_daytime = getDateTime(hw_dt,timezone_offset, "EEE MMM dd h:mm a, yyyy");

                String hw_day = getDateTime(hw_dt,timezone_offset, "EEE MMM dd yyyy");
                String current_dt_compare = getDateTime(current_date,timezone_offset, "EEE MMM dd yyyy");
                if(hw_day.equals(current_dt_compare)){
                    hw_day = "Today";
                }

                else{
                    hw_day = getDateTime(hw_dt,timezone_offset, "EEEE");
                }
                //convert dt to day and time differently and store it in two strings hw_day and hw_time and pass it into TimeRecord
                String hw_time = getDateTime(hw_dt,timezone_offset, "h:mm a");

                Integer hw_temp_int = (int) Math.round(jHourly.getDouble("temp"));
                String hw_temp = hw_temp_int.toString();

                JSONArray hw_weather = jHourly.getJSONArray("weather");
                JSONObject jHW_Weather = (JSONObject) hw_weather.get(0);
                String hw_weather_desc = jHW_Weather.getString("description");

                //call the funcn which compares icon with icon_id from drawable folder and then pass it as a bitmap
                String hw_weather_icon = "_"+jHW_Weather.getString("icon");

                //long hw_pop = jHourly.getLong("pop");

                timelist.add(new TimeRecord(hw_day, hw_time, hw_weather_icon, hw_temp, hw_weather_desc,hw_dt));
            }

            ArrayList<DayRecord> dailyList = new ArrayList<>();
            for (int i = 0; i < daily.length(); i++) {
                JSONObject jDaily_all = (JSONObject) daily.get(i); //next 7 days of data
                long dw_dt = jDaily_all.getLong("dt");
                String dw_dt_all = getDateTime(dw_dt,timezone_offset, "EEEE, dd/MM");

                JSONObject jDailyTemp_all = jDaily_all.getJSONObject("temp");

                //convert min and max into a single string value dw_minmax_temp
                Integer dw_max_temp = (int) Math.round(jDailyTemp_all.getDouble("max"));
                Integer dw_min_temp = (int)Math.round(jDailyTemp_all.getDouble("min"));
                
                //String dw_minmax_temp = dw_max_temp +"/"+dw_min_temp;
                String dw_minmax_temp = String.format("%d °"+(fahrenheit ? "C" : "F")+"/ %d°"+(fahrenheit ? "C" : "F"),dw_max_temp, dw_min_temp);

                Integer dw_morning_temp_int = (int) Math.round(jDailyTemp_all.getDouble("morn"));
                String dw_morning_temp_all = String.format("%d °"+(fahrenheit ? "C" : "F"),dw_morning_temp_int);

                Integer dw_day_temp_int = (int) Math.round(jDailyTemp_all.getDouble("day"));
                String dw_day_temp_all = String.format("%d °"+(fahrenheit ? "C" : "F"),dw_day_temp_int);

                Integer dw_evening_temp_int = (int) Math.round(jDailyTemp_all.getDouble("eve"));
                String dw_evening_temp_all = String.format("%d °"+(fahrenheit ? "C" : "F"),dw_evening_temp_int);

                Integer dw_night_temp_int = (int) Math.round(jDailyTemp_all.getDouble("night"));
                String dw_night_temp_all = String.format("%d °"+(fahrenheit ? "C" : "F"),dw_night_temp_int);


                JSONArray dw_weather = jDaily_all.getJSONArray("weather");
                JSONObject jDW_Weather = (JSONObject) dw_weather.get(0);
                Integer id = jDW_Weather.getInt("id");
                String dw_weather_id = String.valueOf(id);
                String dw_weather_desc = jDW_Weather.getString("description");

                //Convert weather icon into a Bitmap
                String dw_weather_icon = "_"+jDW_Weather.getString("icon");

                String dw_pop = "(" + jDaily_all.getString("pop") + "% precip.)"; //precipitation

                Integer dw_uv_idx_int = (int) Math.round(jDaily_all.getDouble("uvi"));
                String dw_uv_idx = "UV Index: " + dw_uv_idx_int;

                dailyList.add(new DayRecord(dw_dt_all,dw_minmax_temp,dw_morning_temp_all,dw_day_temp_all,dw_evening_temp_all,
                        dw_night_temp_all,dw_weather_id,dw_weather_desc,dw_weather_icon,dw_pop,dw_uv_idx));
            }

            return new Weather(latitude,longitude,current_dt,current_temp,
                    current_feels_like,weather_desc,current_wind,current_wind_speed,current_wind_gust,current_humidity,
                    current_uv_idx, current_rain_snow,current_visibility,
                    current_sunrise,current_sunset
                    , current_clouds,
                    dw_morning_temp,dw_day_temp,dw_evening_temp,dw_night_temp,weather_icon, timelist,dailyList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
