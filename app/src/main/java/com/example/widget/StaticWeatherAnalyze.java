package com.example.widget;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class StaticWeatherAnalyze {
    public  static String  getCityField(JSONObject json)
    {
        try {
            return json.getString("name").toUpperCase() + ", " + json.getJSONObject("sys").getString("country");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "NaN";
    }
    public  static String  getLastUpdateTime(JSONObject json)
    {
        try {
            DateFormat df = DateFormat.getDateTimeInstance();
            return  df.format(new Date(json.getLong("dt")*1000));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "NaN";
    }




    public  static String  getDetailsField(JSONObject json)
    {
        try {
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            return  details.getString("description").toUpperCase() +
                    "\n" + "Влажность: " + main.getString("humidity") + "%" +
                    "\n" + "Давление: " + main.getString("pressure") + " hPa";

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "NaN";
    }
    public  static String  getTemperatureField(JSONObject json)
    {
        try {
            JSONObject main = json.getJSONObject("main");

            return  String.format("%.2f", main.getDouble("temp"))+ " ℃";

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "NaN";
    }

}
