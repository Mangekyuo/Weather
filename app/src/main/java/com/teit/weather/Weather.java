package com.teit.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Weather extends Activity {
    public RequestQueue mQue;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.between);

        mQue= Volley.newRequestQueue(Weather.this);

        //get city name from intent ==>
        String cityname = getIntent().getStringExtra("cityname");
        String CITYJSON =  "http://api.openweathermap.org/data/2.5/weather?q="+cityname+"&appid=2f8f68816b91a94a4c0c6ab4d0f7cecb";
        ArrayList<String> dataofall = new ArrayList<>();
        jsonparse(CITYJSON, dataofall , cityname);
    }
    public void jsonparse(String stringurl , final ArrayList<String> data , final String city){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringurl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String weatherJSONArray = String.valueOf(response.getJSONArray("weather"));
                    int indexofmain_weather = weatherJSONArray.indexOf("main");
                    int indexofmain_description = weatherJSONArray.indexOf("description");
                    int indexoficon = weatherJSONArray.indexOf("icon");

                    //weather now
                    String weather_now = weatherJSONArray.substring(indexofmain_weather+7,indexofmain_description-3);
                    data.add(weather_now);
                    //weather description
                    String weather_description = weatherJSONArray.substring(indexofmain_description+14,indexoficon-3);
                    data.add(weather_description);

                    String windJSONArray = String.valueOf(response.getString("wind"));
                    int indexspeed = windJSONArray.indexOf("speed");
                    int indexdeg = windJSONArray.indexOf("deg");
                    String windspeed=windJSONArray.substring(indexspeed+7,indexdeg-2);
                    data.add(windspeed);

                    JSONObject mainJSONObj = response.getJSONObject("main");
                    String temperature= String.valueOf(mainJSONObj.getDouble("temp"));
                    String temperature_feelslike = String.valueOf(mainJSONObj.getDouble("feels_like"));
                    double temperature_int=Double.parseDouble(temperature);
                    double temperature_degree=Math.round((temperature_int -273.15));
                    //temperature outside
                    int temperature_degree_int =(int)temperature_degree;

                    double temperature_int_feelslike = Double.parseDouble(temperature_feelslike);
                    double temperature_degree_feelslike = Math.round((temperature_int_feelslike-273.15));
                    //temperature feels like
                    int temperature_degree_feelslike_int =(int)temperature_degree_feelslike;
                    //chance of rain
                    int humid = mainJSONObj.getInt("humidity");
                    //atmposhpere pressure
                    int pres = mainJSONObj.getInt("pressure");
                    //get new JSONobject ==>
                    //add values to array
                    data.add(String.valueOf(temperature_degree_int));
                    data.add(String.valueOf(temperature_degree_feelslike_int));
                    data.add(String.valueOf(humid));
                    data.add(String.valueOf(pres));
                    data.add(city);
                    buildWeather(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQue.add(request);
    }
    public void buildWeather(ArrayList<String> data){
        String weathernow = data.get(0);
        String weather_description = data.get(1);
        String wind_speed=data.get(2);
        String temperature = data.get(3);
        String temperature_feel = data.get(4);
        String chanceofrain = data.get(5);
        String pressure = data.get(6);
        String cityname = data.get(7);
        cityname = cityname.replace("%20"," ");
        //set content view
        setContent(weather_description);
        //Authorize text fields
        TextView Temperature =(TextView)findViewById(R.id.Temperature);
        TextView Fl =(TextView)findViewById(R.id.FL);
        TextView WS =(TextView)findViewById(R.id.WS);
        TextView Humidity =(TextView)findViewById(R.id.Humidity);
        TextView Pressure =(TextView)findViewById(R.id.Pressure);
        TextView City =(TextView)findViewById(R.id.City);
        TextView return_tv = (TextView)findViewById(R.id.Return);
        return_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start new intent ==>
                Intent intent = new Intent(com.teit.weather.Weather.this,Main.class);
                startActivity(intent);
                finish();
            }
        });
        //Just fill xml file with that data
        City.setText(cityname);
        Temperature.setText(temperature+"°");
        Fl.setText(" feels like - "+temperature_feel+"°");
        WS.setText("wind speed - "+wind_speed+"m/s");
        Humidity.setText("humidity - "+chanceofrain+"%");
        Pressure.setText("pressure - "+pressure+"hhGm");
    }
    public void setContent(String weather){
        //firstly check device time ==>
        Date currentTime = Calendar.getInstance().getTime();
        Integer hour = Integer.valueOf(String.valueOf(currentTime).substring(11,13));
        if(weather.equals("few clouds")){
            if (hour>18 | hour <6 | hour <6){
                setContentView(R.layout.fewclouds_night);
            }
            else {
                setContentView(R.layout.fewclouds_day);
            }

        }
        if(weather.equals("scattered clouds")){
            if (hour>18 | hour <6 | hour <6){
                setContentView(R.layout.scateredclouds_night);
            }
            else {
                setContentView(R.layout.scateredclouds_day);
            }
        }
        if(weather.equals("overcast clouds")){
            if (hour>18 | hour <6){
                setContentView(R.layout.overcastclouds_night);
            }
            else {
                setContentView(R.layout.overcastclouds_day);
            }
        }
        if(weather.equals("broken clouds")){
            if (hour>18 | hour <6){
                setContentView(R.layout.brokenclouds_night);
            }
            else {
                setContentView(R.layout.brokenclouds_day);
            }
        }
        if(weather.equals("clear sky")){
            if (hour>18 | hour <6){
                setContentView(R.layout.clearsky_night);
            }
            else {
                setContentView(R.layout.clearsky_day);
            }
        }
        if(weather.equals("light rain")){
            if (hour>18 | hour <6){
                setContentView(R.layout.lightrain_night);
            }
            else {
                setContentView(R.layout.lightrain_day);
            }
        }
        if(weather.equals("moderate rain")){
            if (hour>18 | hour <6){
                setContentView(R.layout.moderaterain_night);
            }
            else {
                setContentView(R.layout.moderaterain_day);
            }
        }
        if(weather.equals("very heavy rain")){
            if (hour>18 | hour <6){
                setContentView(R.layout.heavyrain_night);
            }
            else {
                setContentView(R.layout.heavyrain_day);
            }
        }
    }
}
