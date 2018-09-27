package com.example.xy.demomdf;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.xy.demomdf.Screen1.RecipeListAdapter;
import com.example.xy.demomdf.data.RecipeData;
import com.example.xy.demomdf.networkUtils.NetworkOps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements RecipeListAdapter.RecipeAdapterOnClickHandler {


    public static ArrayList<RecipeData> recipeDataArrayList;

    public static RecyclerView recipeListRecyclerView;
    public static RecipeListAdapter recipeListAdapter;

    public final String LIST_STATE_KEY = "list_state_key";
    private static Bundle mBundleRecyclerViewState;

    private Snackbar snackbar;
    private Toast noResultToast,noConnectionToast;

    public static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipeListRecyclerView = findViewById(R.id.recipe_list_recycler_view);
        recipeListRecyclerView.setHasFixedSize(true);

        if (findViewById(R.id.main_coordinator_layout_tablet) == null) {
            recipeListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            snackbar = Snackbar.make(findViewById(R.id.main_coordinator_layout),
                    "No Internet connection!", Snackbar.LENGTH_INDEFINITE);
        } else {
//            Tablet mode
            recipeListRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
            snackbar = Snackbar.make(findViewById(R.id.main_coordinator_layout_tablet),
                    "No Internet connection!", Snackbar.LENGTH_INDEFINITE);
        }


        recipeDataArrayList = new ArrayList<>();
        recipeListAdapter = new RecipeListAdapter(
                getApplicationContext(),recipeDataArrayList, this
        );



        noResultToast = Toast.makeText(getApplicationContext(), "No results!",Toast.LENGTH_LONG);

        noConnectionToast = Toast.makeText(getApplicationContext(), "No connectivity manager!",Toast.LENGTH_LONG);

        progressBar = findViewById(R.id.mProgressBar);
        startWorking();

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("onPause","in onPause");

        mBundleRecyclerViewState = new Bundle();
        Log.i("MainActivity","Bundle initiated");
        Parcelable listState = recipeListRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(LIST_STATE_KEY,listState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("onResume","in onResume");

        if (mBundleRecyclerViewState != null){
            Parcelable listState = mBundleRecyclerViewState.getParcelable(LIST_STATE_KEY);
            recipeListRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            recipeListRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            recipeListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void startWorking(){
        if (appIsConnectedToTheInternet()){
            if (snackbar.isShownOrQueued()){
                snackbar.dismiss();
            }
            /*
            offlineTextView.animate().alpha(0f).setDuration(2000);
            offlineTextView.setVisibility(View.INVISIBLE);
             */

            new DownloadBakingRecipesTask().execute(NetworkOps.getSourceUrl().toString());

        } else{

            /*
            offlineTextView.setVisibility(View.VISIBLE);
            offlineTextView.animate().alphaBy(1f).setDuration(2000);
             */

            snackbar.setActionTextColor(ContextCompat.getColor(getApplicationContext(),
                    R.color.brightRandomColor));

            snackbar.setAction("Try again!", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Re-Check if internet connection is there and if yes, call async task
                    startWorking();
                }
            }).show();
        }

    }


    public boolean appIsConnectedToTheInternet(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork;
        boolean isConnected = false;

        if (connectivityManager != null) {
            activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } else{
            noConnectionToast.show();
        }


        return isConnected;

    }

    @Override
    public void onClick(RecipeData recipeData,
                        ArrayList<RecipeData.RecipeIngredient> recipeIngredients,
                        ArrayList<RecipeData.RecipeStep> recipeSteps) {

        Intent launchDetailScreenIntent = new Intent(this,RecipeDetailActivity.class);

        launchDetailScreenIntent.putExtra("recipe_data",recipeData);
        launchDetailScreenIntent.putParcelableArrayListExtra("ing_list",recipeIngredients);
        launchDetailScreenIntent.putParcelableArrayListExtra("step_list",recipeSteps);

        startActivity(launchDetailScreenIntent);

    }



    public static class DownloadBakingRecipesTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d("TAG","Downloading data...");
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressBar.setVisibility(View.GONE);

            if (result != null){
//            if recipes present online,


                try {
                    JSONArray recipesJsonArray = new JSONArray(result);

                    if (!recipeDataArrayList.isEmpty()){
                        recipeDataArrayList.clear();
                    }

                    for (int i = 0 ; i < recipesJsonArray.length() ; i++){

                        JSONObject recipeJsonObject =
                                recipesJsonArray.getJSONObject(i);

                        String recipeId = recipeJsonObject.getString("id");
                        String recipeName = recipeJsonObject.getString("name");

                        RecipeData recipeData = new RecipeData(Parcel.obtain());
                        recipeData.setRecipeIndex(Integer.valueOf(recipeId));
                        recipeData.setRecipeName(recipeName);


//                        For ingredients array

                        JSONArray ingredientsJsonArray = recipeJsonObject
                                .getJSONArray("ingredients");

                        ArrayList<RecipeData.RecipeIngredient> recipeIngredientArrayList = new ArrayList<>();

                        for (int j = 0 ; j < ingredientsJsonArray.length() ; j++){
                            JSONObject ingredientJsonObject =
                                    ingredientsJsonArray.getJSONObject(j);

                            double quantity = ingredientJsonObject.getDouble("quantity");
                            String measure = ingredientJsonObject.getString("measure");
                            String ingredient = ingredientJsonObject.getString("ingredient");


                            RecipeData.RecipeIngredient recipeIngredient =
                                    new RecipeData.RecipeIngredient(Parcel.obtain());

                            recipeIngredient.setQuantity(quantity);
                            recipeIngredient.setMeasure(measure);
                            recipeIngredient.setIngredient(ingredient);

                            recipeIngredientArrayList.add(recipeIngredient);

                        }

                        recipeData.setRecipeIngredientArrayList(recipeIngredientArrayList);

//                        For steps array

                        JSONArray stepsJsonArray = recipeJsonObject.getJSONArray("steps");
                        ArrayList<RecipeData.RecipeStep> recipeStepArrayList = new ArrayList<>();

                        for (int k = 0; k < stepsJsonArray.length() ; k++) {
                            JSONObject stepJsonObject = stepsJsonArray.getJSONObject(k);

                            int id = stepJsonObject.getInt("id");
                            String shortDescription = stepJsonObject.getString("shortDescription");
                            String mainDescription = stepJsonObject.getString("description");
                            String videoUrl = stepJsonObject.getString("videoURL");

                            RecipeData.RecipeStep recipeStep =
                                    new RecipeData.RecipeStep(Parcel.obtain());
                            recipeStep.setId(id);
                            recipeStep.setShortDescription(shortDescription);
                            recipeStep.setMainDescription(mainDescription);
                            recipeStep.setVideoUrl(videoUrl);

                            recipeStepArrayList.add(recipeStep);
                        }
                        recipeData.setRecipeStepArrayList(recipeStepArrayList);

                        recipeDataArrayList.add(recipeData);

                    }



                    recipeListRecyclerView.setAdapter(recipeListAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
