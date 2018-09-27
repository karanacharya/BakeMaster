package com.example.xy.demomdf.Widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.xy.demomdf.R;

public class BakeWidgetService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public static final String ACTION_OPEN_RECIPE =
            "com.example.xy.demomdf.bake_widget_service";

    public BakeWidgetService(String name) {
        super(name);
    }
    public BakeWidgetService(){
        super(BakeWidgetService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Bake Widget Service",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
            if (intent != null){
                String action = intent.getAction();
                if (ACTION_OPEN_RECIPE.equals(action)){
                    handleActionRecipe();
                }
            }
    }

    private void handleActionRecipe(){

        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.recipe_ingredients),MODE_PRIVATE);

        String recipeIngredientsString = sharedPreferences.getString(getString(R.string.ingredients_extra),"");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,BakeWidgetProvider.class));

        BakeWidgetProvider.updateWidgetIngredients(this,appWidgetManager,appWidgetIds,recipeIngredientsString);



    }

    public static void startActionRecipe(Context context){
        Intent intent = new Intent(context, BakeWidgetService.class);
        intent.setAction(ACTION_OPEN_RECIPE);
        context.startService(intent);
    }

//    For Android O and further versions
    public static void startActionRecipeO(Context context){
        Intent intent = new Intent(context, BakeWidgetService.class);
        intent.setAction(ACTION_OPEN_RECIPE);
        ContextCompat.startForegroundService(context,intent);
    }
}
