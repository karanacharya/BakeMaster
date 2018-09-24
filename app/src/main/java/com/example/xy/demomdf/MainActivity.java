package com.example.xy.demomdf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


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
    public static ArrayList<RecipeData.RecipeIngredient> recipeIngredientArrayList;
    public static ArrayList<RecipeData.RecipeStep> recipeStepArrayList;

    public static RecyclerView recipeListRecyclerView;
    public static RecipeListAdapter recipeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipeListRecyclerView = findViewById(R.id.recipe_list_recycler_view);
        recipeListRecyclerView.setHasFixedSize(true);

        recipeListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipeDataArrayList = new ArrayList<>();
        recipeListAdapter = new RecipeListAdapter(
                getApplicationContext(),recipeDataArrayList, this
        );

        recipeIngredientArrayList = new ArrayList<>();
        recipeStepArrayList = new ArrayList<>();

        new DownloadBakingRecipesTask().execute(NetworkOps.getSourceUrl().toString());


    }

    @Override
    public void onClick(RecipeData recipeData) {

        Intent launchDetailScreenIntent = new Intent(this,RecipeDetailActivity.class);
        launchDetailScreenIntent.putExtra("recipe_data",recipeData);
        startActivity(launchDetailScreenIntent);


    }

    public static class DownloadBakingRecipesTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d("TAG","Downloading data...");
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
                        recipeDataArrayList.add(recipeData);

//                        For ingredients array

                        JSONArray ingredientsJsonArray = recipeJsonObject.getJSONArray("ingredients");

                        if (!recipeIngredientArrayList.isEmpty()){
                            recipeIngredientArrayList.clear();
                        }

                        for (int j = 0 ; j < ingredientsJsonArray.length() ; j++){
                            JSONObject ingredientJsonObject =
                                    ingredientsJsonArray.getJSONObject(j);

                            double quantity = ingredientJsonObject.getDouble("quantity");
                            String measure = ingredientJsonObject.getString("measure");
                            String ingredient = ingredientJsonObject.getString("ingredient");


                            RecipeData.RecipeIngredient recipeIngredient =
                                    new RecipeData.RecipeIngredient();

                            recipeIngredient.setQuantity(quantity);
                            recipeIngredient.setMeasure(measure);
                            recipeIngredient.setIngredient(ingredient);

                            recipeIngredientArrayList.add(recipeIngredient);

                        }

//                        For steps array

                        JSONArray stepsJsonArray = recipeJsonObject.getJSONArray("steps");
                        if (!recipeStepArrayList.isEmpty()){
                            recipeStepArrayList.clear();
                        }

                        for (int k = 0; k < stepsJsonArray.length() ; k++) {
                            JSONObject stepJsonObject = stepsJsonArray.getJSONObject(k);

                            int id = stepJsonObject.getInt("id");
                            String shortDescription = stepJsonObject.getString("shortDescription");
                            String mainDescription = stepJsonObject.getString("description");
                            String videoUrl = stepJsonObject.getString("videoURL");

                            RecipeData.RecipeStep recipeStep = new RecipeData.RecipeStep();
                            recipeStep.setId(id);
                            recipeStep.setShortDescription(shortDescription);
                            recipeStep.setMainDescription(mainDescription);
                            recipeStep.setVideoUrl(videoUrl);

                            recipeStepArrayList.add(recipeStep);
                        }

                    }

                    recipeListRecyclerView.setAdapter(recipeListAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
