package com.example.widget;

import android.app.Activity;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class CityPreference {

    SharedPreferences prefs;

    public CityPreference(AppCompatActivity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    String getCity(){
        return prefs.getString("city", "Orenburg");
    }

    void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }

}

