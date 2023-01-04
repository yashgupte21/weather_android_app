package com.example.openweatherapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private final ArrayList<TimeRecord> timeRecordArrayList = new ArrayList<>();
    private final ArrayList<DayRecord> dayRecordArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HourlyAdapter hourlyAdapter;
    private static final String TAG ="Main activity";
    public boolean fahrenheit = false;
    private boolean rain =true;
    private SharedPreferences.Editor editor;
    private String city;
    private String latitude = "41.8675766";
    private String longitude= "-87.61623";
    private ActivityResultLauncher<Intent> activityDailyLauncher;
    private Weather weather;
    boolean hasInternetConnection = false;
    private SwipeRefreshLayout swiper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.hourly_weather_rv);
        hourlyAdapter = new HourlyAdapter(timeRecordArrayList,this);
        recyclerView.setAdapter(hourlyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        Log.d(TAG, "onCreate: "+ hourlyAdapter);


        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
//        latitude = "41.8675766";
//        longitude = "-87.61623";

        if (!sharedPref.contains("FAHRENHEIT")) {
            editor.putBoolean("FAHRENHEIT", false);
            editor.putString("LATITUDE", latitude);
            editor.putString("LONGITUDE",longitude);
            editor.apply();
        }

        latitude = sharedPref.getString("LATITUDE", latitude);
        longitude = sharedPref.getString("LONGITUDE", longitude);
        fahrenheit = sharedPref.getBoolean("FAHRENHEIT",false);

        TextView city_tv = findViewById(R.id.city_tv);
        city = city_tv.getText().toString().trim().replaceAll(", ", ",");

        if(hasNetworkConnection(this)){
            hasInternetConnection = true;
//            latitude = "41.8675766";
//            longitude = "-87.61623";
            editor.putString("LATITUDE",latitude);
            editor.putString("LONGITUDE", longitude);
            editor.putBoolean("FAHRENHEIT", fahrenheit);
            editor.apply();
            runThread(city,latitude,longitude,this.fahrenheit);
        }
        else {
            setNoInternetConnection();
        }


        swiper = findViewById(R.id.swiper);
        swiper.setOnRefreshListener(()->{
            swiper.setRefreshing(false);
            if (hasNetworkConnection(this)) {
                hasInternetConnection = true;
                editor.putString("LATITUDE",latitude);
                editor.putString("LONGITUDE", longitude);
                editor.putBoolean("FAHRENHEIT", fahrenheit);
                editor.apply();
                runThread(city,latitude,longitude,fahrenheit);
            } else {
                hasInternetConnection = false;
//                city ="";
//                latitude="";
//                longitude="";
                runThread(this.city,latitude,longitude,fahrenheit);
                setNoInternetConnection();
                Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void runThread(String city, String  latitude, String longitude, boolean fahrenheit){
        WeatherRunnable weatherRunnable = new WeatherRunnable(this, city,latitude,longitude , fahrenheit);
        new Thread(weatherRunnable).start();
    }


    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu,menu);
        return true;
    }

    //Menu items selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.toggle_units_menu:
                if(hasInternetConnection){
                Toast.makeText(this, "F/C menu selected", Toast.LENGTH_SHORT).show();
                fahrenheit = !fahrenheit;
                editor.putBoolean("FAHRENHEIT",fahrenheit);
                editor.apply();
                if(fahrenheit == false)
                    item.setIcon(getResources().getDrawable(R.drawable.units_f));
                else
                    item.setIcon(getResources().getDrawable(R.drawable.units_c));
                editor.apply();
                runThread(city,latitude,longitude,fahrenheit);
                }
                //run weather thread runnable
                return true;
            case R.id.daily_forecast_menu:
                if(hasInternetConnection) {
                    Toast.makeText(this, "Daily forecast menu selected", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, DailyActivity.class);
                    intent.putExtra("temp", dayRecordArrayList);
                    intent.putExtra("location", city);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.change_location_menu:
                if(hasInternetConnection) {
                    click3();
                    Toast.makeText(this, "Change Location menu selected", Toast.LENGTH_SHORT).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onLongClick(View view) {
        Toast.makeText(this, "Nothing Interesting Happens\nHere on Long Click", Toast.LENGTH_SHORT).show();
        return true;
    }

//    public void updateHourlyData(ArrayList<TimeRecord> timeRecordArrayList) {
//        this.timeRecordArrayList.addAll(timeRecordArrayList);
//        hourlyAdapter.notifyItemRangeChanged(0, timeRecordArrayList.size());
//    }

    public void updateData(Weather weather){
        if (weather == null) {
            Toast.makeText(this, "Please Enter a Valid City Name", Toast.LENGTH_SHORT).show();
            return;
        }

        this.weather = weather;
        hasInternetConnection = true;

        TextView city = findViewById(R.id.city_tv);
        //covert lat and lon into city and country name
        String latlon = String.format("%s,%s",weather.getLatitude(),weather.getLongitude());
        city.setText(getLocationName(this,latlon));
        this.city = getLocationName(this,latlon);

        TextView dt = findViewById(R.id.dt_tv);
        dt.setText(String.format("%s", weather.getDt()));

        TextView current_temp = findViewById(R.id.current_temp_tv);
        current_temp.setText(String.format("%.0f° " + (fahrenheit ? "C" : "F"), Double.parseDouble(weather.getCurrent_temp())));

        TextView current_feels_like = findViewById(R.id.current_feels_like_tv);
        current_feels_like.setText(String.format("Feels Like: %s°"+ (fahrenheit ? "C" : "F"), weather.getCurrent_feels_like()));


        TextView current_weather_desc = findViewById(R.id.current_weather_desc_tv);
        current_weather_desc.setText(String.format("%s ( %s %% clouds)", weather.getCurrent_weather_desc(), weather.getCurrent_clouds()));

        TextView current_wind = findViewById(R.id.current_wind_tv);
        Double wind_degrees =  Double.parseDouble(weather.getCurrent_wind());
        //Double wind_gust = Double.parseDouble(weather.getCurrent_wind_gust());
        String wind_gust = weather.getCurrent_wind_gust();
        if(wind_gust.equals("")) {
            current_wind.setText(String.format("Winds: %s at %s  " + (fahrenheit ? "mps" : "mph"), getDirection(wind_degrees), weather.getCurrent_wind_speed()));
        }
        else{
            current_wind.setText(String.format("Winds: %s at %s " + (fahrenheit ? "mps" : "mph")+ " gusting to %s " +(fahrenheit ? "mps" : "mph"), getDirection(wind_degrees), weather.getCurrent_wind_speed(),weather.getCurrent_wind_gust()));
        }

        TextView current_humidity = findViewById(R.id.current_humidity_tv);
        current_humidity.setText(String.format("Humidity: %s %%", weather.getCurrent_humidity()));

        TextView current_uv_idx = findViewById(R.id.current_uv_idx_tv);
        current_uv_idx.setText(String.format("UV Index: %s", weather.getCurrent_uv_idx()));


        TextView current_rain_snow = findViewById(R.id.current_rain_snow_tv);
        if(!weather.getCurrent_rain_snow().isEmpty()) {
            current_rain_snow.setText(String.format((rain ? "Rain" : "Snow") + " last hour: %s in", weather.getCurrent_rain_snow()));
        } else{
            current_rain_snow.setText("");
        }

        TextView current_visibility = findViewById(R.id.current_visibility_tv);
        current_visibility.setText(String.format("Visibility: %s mi", weather.getCurrent_visibility()));


        TextView morning_temp = findViewById(R.id.morning_temp_tv);
        morning_temp.setText(String.format("%.0f° " + (fahrenheit ? "C" : "F"), Double.parseDouble(weather.getMorning_temp())));


        TextView daytime_temp = findViewById(R.id.daytime_temp_tv);
        daytime_temp.setText(String.format("%.0f° " + (fahrenheit ? "C" : "F"), Double.parseDouble(weather.getDaytime_temp())));


        TextView evening_temp = findViewById(R.id.evening_temp_tv);
        evening_temp.setText(String.format("%.0f° " + (fahrenheit ? "C" : "F"), Double.parseDouble(weather.getEvening_temp())));

        TextView night_temp = findViewById(R.id.night_temp_tv);
        night_temp.setText(String.format("%.0f° " + (fahrenheit ? "C" : "F"), Double.parseDouble(weather.getNight_temp())));


        TextView morning_time = findViewById(R.id.morning_time_tv);
        morning_time.setText("8 am");

        TextView daytime_time = findViewById(R.id.daytime_time_tv);
        daytime_time.setText("1 pm");

        TextView evening_time = findViewById(R.id.evening_time_tv);
        evening_time.setText("5 pm");

        TextView night_time = findViewById(R.id.night_time_tv);
        night_time.setText("11 pm");

        TextView current_sunrise = findViewById(R.id.current_sunrise_tv);
        current_sunrise.setText(String.format("Sunrise: %s am", weather.getCurrent_sunrise()));

        TextView current_sunset = findViewById(R.id.current_sunset_tv);
        current_sunset.setText(String.format("Sunset: %s pm", weather.getCurrent_sunset()));

        ImageView current_weather_icon = findViewById(R.id.current_weather_icon_imv);
        int iconResId = this.getResources().getIdentifier(weather.getCurrent_weather_icon(), "drawable", this.getPackageName());
        current_weather_icon.setImageResource(iconResId);

        if(timeRecordArrayList.size()==0){
            timeRecordArrayList.addAll(weather.getTimeRecordArrayList());
        }
        else {
            timeRecordArrayList.clear();
            timeRecordArrayList.addAll(weather.getTimeRecordArrayList());
        }
        hourlyAdapter.notifyDataSetChanged();

        if(dayRecordArrayList.size() == 0)
            dayRecordArrayList.addAll(weather.getDayRecordList());
        else{
            dayRecordArrayList.clear();
            dayRecordArrayList.addAll(weather.getDayRecordList());
        }

    }

    public void handleError(String finalMsg) {
    }

    public static String getLocationName(Context context, String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(context); // Here, “this” is an Activity
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                // Nothing returned!
                return null;
            }
            String country = address.get(0).getCountryCode();
            String p1 = "";
            String p2 = "";
            if (country.equals("US")) {
                p1 = address.get(0).getLocality();
                p2 = address.get(0).getAdminArea();
            } else {
                p1 = address.get(0).getLocality();
                if (p1 == null)
                    p1 = address.get(0).getSubAdminArea();
                p2 = address.get(0).getCountryName();
            }
            String locale = p1 + ", " + p2;
            return locale;
        } catch (IOException e) {
            // Failure to get an Address object
            return null;
        }
    }

    public static double[] getLatLon(Context context, String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(context); // Here, “this” is an Activity
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                // Nothing returned!
                return null;
            }
            double lat = address.get(0).getLatitude();
            double lon = address.get(0).getLongitude();

            return new double[]{lat, lon};
        } catch (IOException e) {
            // Failure to get an Address object
            return null;
        }
    }

    public static String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }


    public void click3() {
        // Dialog with a layout
        // Inflate the dialog's layout
        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams")
        final View view = inflater.inflate(R.layout.alert_dailog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("For US locations, enter as 'City', or 'City,State'"+"\n\n" + "For International locations, enter as 'City,Country'");
        builder.setTitle("Enter a Location");

        // Set the inflated view to be the builder's view
        builder.setView(view);

        builder.setPositiveButton("OK", (dialog, id) -> {
            //getlatlon and run thread
            EditText et1 = view.findViewById(R.id.alert_location_et);
            String location_name = et1.getText().toString();
            location_name.replaceAll("[*0-9]", "");
            try{
                if(!location_name.isEmpty() && location_name!=null && !location_name.equals("")){
                    double[] latlon = getLatLon(this, location_name);
                    if(!location_name.isEmpty() && !location_name.equals("") && latlon.length>0 && latlon!= null ) {
                        latitude = Double.toString(latlon[0]);
                        longitude = Double.toString(latlon[1]);
                        editor.putString("LATITUDE",latitude);
                        editor.putString("LONGITUDE", longitude);
                        editor.putBoolean("FAHRENHEIT", fahrenheit);
                        editor.apply();
                        runThread(city, latitude, longitude, fahrenheit);
                        Toast.makeText(MainActivity.this, "OK!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Please enter location to get weather data.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Please enter location to get weather data.", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("CANCEL", (dialog, id) -> {
            Toast.makeText(MainActivity.this, "Clicked Cancel", Toast.LENGTH_SHORT).show();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private  boolean hasNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = context.getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    private void setNoInternetConnection() {
        hasInternetConnection = false;
//        city="";
//        latitude="";
//        longitude="";

        editor.putString("LATITUDE",latitude);
        editor.putString("LONGITUDE", longitude);
        editor.putBoolean("FAHRENHEIT", fahrenheit);
        editor.apply();
        runThread(city,latitude,longitude,fahrenheit);

        TextView dt = findViewById(R.id.dt_tv);
        dt.setText("No internet connection");

        TextView current_temp = findViewById(R.id.current_temp_tv);
        current_temp.setText("");

        TextView current_feels_like = findViewById(R.id.current_feels_like_tv);
        current_feels_like.setText("");


        TextView current_weather_desc = findViewById(R.id.current_weather_desc_tv);
        current_weather_desc.setText("");

        TextView current_wind = findViewById(R.id.current_wind_tv);
        current_wind.setText("");

        TextView current_humidity = findViewById(R.id.current_humidity_tv);
        current_humidity.setText("");

        TextView current_uv_idx = findViewById(R.id.current_uv_idx_tv);
        current_uv_idx.setText("");


        TextView current_rain_snow = findViewById(R.id.current_rain_snow_tv);
        current_rain_snow.setText("");

        TextView current_visibility = findViewById(R.id.current_visibility_tv);
        current_visibility.setText("");


        TextView morning_temp = findViewById(R.id.morning_temp_tv);
        morning_temp.setText("");


        TextView daytime_temp = findViewById(R.id.daytime_temp_tv);
        daytime_temp.setText("");

        TextView evening_temp = findViewById(R.id.evening_temp_tv);
        evening_temp.setText("");

        TextView night_temp = findViewById(R.id.night_temp_tv);
        night_temp.setText("");


        TextView morning_time = findViewById(R.id.morning_time_tv);
        morning_time.setText("");

        TextView daytime_time = findViewById(R.id.daytime_time_tv);
        daytime_time.setText("");

        TextView evening_time = findViewById(R.id.evening_time_tv);
        evening_time.setText("");

        TextView night_time = findViewById(R.id.night_time_tv);
        night_time.setText("");


        TextView current_sunrise = findViewById(R.id.current_sunrise_tv);
        current_sunrise.setText("");

        TextView current_sunset = findViewById(R.id.current_sunset_tv);
        current_sunset.setText("");

        ImageView current_weather_icon = findViewById(R.id.current_weather_icon_imv);
        current_weather_icon.setImageResource(android.R.color.transparent);

        timeRecordArrayList.clear();
        hourlyAdapter.notifyDataSetChanged();
    }

    public void openCal(long millis) {
        try {
            Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
            builder.appendPath("time");
            ContentUris.appendId(builder, millis*1000L);
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setData(builder.build());
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    //onResume
    @Override
    protected void onResume() {
        runThread(city,latitude,longitude,fahrenheit);
        //androidNotesAdapter.notifyDataSetChanged();
        super.onResume();
    }



}