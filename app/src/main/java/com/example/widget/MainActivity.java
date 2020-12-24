package com.example.widget;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import static com.example.widget.ConnectFetch.getIconUrl;
import static com.example.widget.StaticWeatherAnalyze.getCityField;
import static com.example.widget.StaticWeatherAnalyze.getDetailsField;
import static com.example.widget.StaticWeatherAnalyze.getLastUpdateTime;
import static com.example.widget.StaticWeatherAnalyze.getTemperatureField;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setInfo();
            }
        });
        setInfo();
    }

    private void setInfo() {
        new  ConnectFetch(this, new CityPreference(this).getCity(), new ConnectFetch.OnConnectionCompleteListener() {
            @Override
            public void onSuccess(JSONObject response) {
                renderWeather(response);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFail(String message) {

                Toast.makeText(MainActivity.this,
                        message,
                        Toast.LENGTH_LONG).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void changeCity(String city){
        new CityPreference(this).setCity(city);
        setInfo();
    }

    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Измените город:");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }

    private void renderWeather(JSONObject json){
        try {

//            Ответы:
//                      json.getString("name") - название города
//                      json.getJSONObject("sys").getString("country") - название страны
//                      JSONObject details =  json.getJSONArray("weather").getJSONObject(0) - первый элемент массива метеорологических данных.
//                           details.getInt("id")  - идентификатор погоды\
//                           details.getString("description") - краткое описание погоды

//                      JSONObject main = json.getJSONObject("main"); - узел main
//                          main.getString("humidity")  - влажность
//                          main.getString("pressure")  - давление
//                            main.getDouble("temp")    - температура

//                      DateFormat df = DateFormat.getDateTimeInstance();
//                      String updatedOn = df.format(new Date(json.getLong("dt")*1000)); - время получения информации системой

//                      json.getJSONObject("sys").getLong("sunrise") - время восхода
//                      json.getJSONObject("sys").getLong("sunset") - время заката
            Glide
                    .with(this)
                    .load(getIconUrl(json))
                    .into((ImageView)findViewById(R.id.weather_icon));
            ((TextView)findViewById(R.id.city_field)).setText(getCityField(json));
            ((TextView)findViewById(R.id.updated_field)).setText(getLastUpdateTime(json));
            ((TextView)findViewById(R.id.details_field)).setText(getDetailsField(json));
            ((TextView)findViewById(R.id.current_temperature_field)).setText(getTemperatureField(json));

            //            JSONObject details =  json.getJSONArray("weather").getJSONObject(0);
//            ((TextView)findViewById(R.id.weather)).setText(details.getString("description").toUpperCase());

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    public void setCity(View view) {
        showInputDialog();
    }
}

