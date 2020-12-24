package com.example.widget;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectFetch {
    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
    private static final String OPEN_WEATHER_ICON = "http://openweathermap.org/img/wn/{icon}@2x.png";
    private OnConnectionCompleteListener listener;

    public ConnectFetch(Context context, String city, OnConnectionCompleteListener listener)
    {
        this.listener = listener;
    }

    private void updateWeatherData(final String city, final Context context){
        new Thread(){
            public void run(){
                final JSONObject json = ConnectFetch.getJSON(context, city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            listener.onFail(city);
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            listener.onSuccess(json);
                        }
                    });
                }
            }
        }.start();
    }

    public static JSONObject getJSON(Context context, String city){
        try {
            String urlString = String.format(OPEN_WEATHER_MAP_API, city,context.getString(R.string.weather_api_key));
            URL url = new URL(urlString);

            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

//            connection.addRequestProperty("x-api-key",
//                    context.getString(R.string.weather_api_key));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();


            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }
    public static String getIconUrl(JSONObject json)
    {
        try {

//          первый элемент массива метеорологических данных.
            JSONObject details =  json.getJSONArray("weather").getJSONObject(0) ;
            String icon = details.getString("icon");
            return String.format(OPEN_WEATHER_ICON, icon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    public interface OnConnectionCompleteListener {
        void onSuccess(JSONObject response);
        void onFail(String message);
    }
}
