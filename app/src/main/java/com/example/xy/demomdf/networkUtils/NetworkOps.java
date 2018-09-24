package com.example.xy.demomdf.networkUtils;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.xy.demomdf.data.RecipeData;

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

public class NetworkOps {



    public static final String SOURCE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static URL getSourceUrl(){

        Uri sourceUri = Uri.parse(SOURCE_URL).buildUpon()
                .build();

        URL sourceUrl = null;

        try {
            sourceUrl = new URL(sourceUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return sourceUrl;
    }



}
