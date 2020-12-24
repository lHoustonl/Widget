package com.example.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;

import org.json.JSONObject;

import java.util.Arrays;

import static com.example.widget.ConnectFetch.getIconUrl;
import static com.example.widget.StaticWeatherAnalyze.getTemperatureField;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {
    final String LOG_TAG = "myLogs";

    static void updateAppWidget(final Context context, AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        new  ConnectFetch(context, "Orenburg", new ConnectFetch.OnConnectionCompleteListener() {
            @Override
            public void onSuccess(JSONObject response) {
                renderWeather(response,context,remoteViews,appWidgetId);
            }

            @Override
            public void onFail(String message) {
            }
        });
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

        Log.d(LOG_TAG, "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled

        Log.d(LOG_TAG, "onDisabled");
    }

    public static void pushWidgetUpdate(Context context, RemoteViews rv) {
        ComponentName myWidget = new ComponentName(context, AppWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, rv);
    }

    public static void renderWeather(JSONObject json, Context context, RemoteViews remoteViews, int appWidgetId){
        try {
            AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, remoteViews, R.id.weather_icon, appWidgetId);

            Glide.with(context.getApplicationContext())
                    .load(getIconUrl(json))
                    .asBitmap().
                    into( appWidgetTarget );
            remoteViews.setTextViewText(R.id.details_field, getTemperatureField(json));
            pushWidgetUpdate(context, remoteViews);
        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }
}

