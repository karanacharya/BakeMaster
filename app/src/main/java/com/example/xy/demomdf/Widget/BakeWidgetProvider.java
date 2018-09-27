package com.example.xy.demomdf.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.xy.demomdf.ItemDetailActivity;
import com.example.xy.demomdf.MainActivity;
import com.example.xy.demomdf.R;
import com.example.xy.demomdf.data.RecipeData;

import java.util.ArrayList;
import java.util.Arrays;

public class BakeWidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.i("ExampleWidget",  "Updating widgets " + Arrays.asList(appWidgetIds));
       BakeWidgetService.startActionRecipe(context);
    }

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId,
                                String recipeIngredients){

        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.bake_widget_layout
        );

        Intent intent = new Intent(context, MainActivity.class);
        /*
        intent.putExtra(context.getString(R.string.from_widget),
                "Came from Widget");
         */

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context,0,intent,0);

        if (recipeIngredients == null   ||
                recipeIngredients.equals("")){
            recipeIngredients = "No ingredients found!";
        }

        views.setTextViewText(R.id.widget1label,recipeIngredients);
        views.setOnClickPendingIntent(R.id.widget1label,pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId,views);

    }

    public static void updateWidgetIngredients(Context context,
                                               AppWidgetManager appWidgetManager,
                                               int[] appWidgetIds,
                                               String recipeIngredientsString){
        for (int appWidgetId : appWidgetIds){
            updateAppWidget(context,appWidgetManager,appWidgetId,recipeIngredientsString);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
