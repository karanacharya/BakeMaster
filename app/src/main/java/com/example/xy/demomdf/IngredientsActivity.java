package com.example.xy.demomdf;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class IngredientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        /*
        Toolbar toolbar = findViewById(R.id.ingredients_detail_toolbar);
        setSupportActionBar(toolbar);
         */


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState == null){

            Bundle arguments = new Bundle();

            arguments.putParcelableArrayList(IngredientsFragment.ARG_INGREDIENTS_ID,
                    getIntent().getParcelableArrayListExtra(IngredientsFragment.ARG_INGREDIENTS_ID));

            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container,ingredientsFragment)
                    .commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            NavUtils.navigateUpTo(this, new Intent(this, RecipeDetailActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
