package com.example.xy.demomdf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xy.demomdf.data.RecipeData;
import com.example.xy.demomdf.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private TextView ingredientsTextView;

    private ArrayList<RecipeData.RecipeIngredient> recipeIngredients;
    private ArrayList<RecipeData.RecipeStep> recipeSteps;

    private ArrayList<String> stepsInShort;



    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        recipeIngredients = new ArrayList<>();
        recipeSteps = new ArrayList<>();
        stepsInShort = new ArrayList<>();
        ingredientsTextView = findViewById(R.id.ingredients_tv);


        Intent intentThatLaunchedMe = getIntent();
        if (intentThatLaunchedMe.hasExtra("recipe_data")){
            RecipeData recipeData = intentThatLaunchedMe.getParcelableExtra("recipe_data");

            getSupportActionBar().setTitle(recipeData.getRecipeName());
        }

        if (intentThatLaunchedMe.hasExtra("ing_list")){
            recipeIngredients =
                    intentThatLaunchedMe.getParcelableArrayListExtra("ing_list");

        }

        if (intentThatLaunchedMe.hasExtra("step_list")){
            recipeSteps =
                    intentThatLaunchedMe.getParcelableArrayListExtra("step_list");

            for (int i = 0 ; i < recipeSteps.size() ; i++){
                stepsInShort.add(recipeSteps.get(i).getShortDescription());
            }

        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        ingredientsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane){
                    Bundle args = new Bundle();
                    args.putParcelableArrayList(IngredientsFragment.ARG_INGREDIENTS_ID,recipeIngredients);
                    IngredientsFragment ingredientsFragment = new IngredientsFragment();

                    ingredientsFragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container,ingredientsFragment)
                            .commit();
                }else{
                    Intent intent = new Intent(getApplicationContext(),IngredientsActivity.class);
                    intent.putParcelableArrayListExtra(IngredientsFragment.ARG_INGREDIENTS_ID,recipeIngredients);
                    startActivity(intent);
                }

            }
        });

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this,
                //DummyContent.ITEMS,
                stepsInShort,
                recipeSteps,
                mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RecipeDetailActivity mParentActivity;
        //private final List<DummyContent.DummyItem> mValues;
        private final ArrayList<String> shortSteps;
        private final ArrayList<RecipeData.RecipeStep> recipeStepsList;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tag = (String) view.getTag();
                //DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    //arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID,tag);
                    arguments.putParcelableArrayList(ItemDetailFragment.ARG_STEPS_LIST_ID,recipeStepsList);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    //intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID,tag);
                    intent.putParcelableArrayListExtra(ItemDetailFragment.ARG_STEPS_LIST_ID,recipeStepsList);
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(RecipeDetailActivity parent,
                                      //List<DummyContent.DummyItem> items,
                                      ArrayList<String> shortSteps,
                                      ArrayList<RecipeData.RecipeStep> recipeSteps,
                                      boolean twoPane) {
//            mValues = items;
            mParentActivity = parent;
            this.shortSteps = shortSteps;
            recipeStepsList = recipeSteps;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_detail_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            //holder.mContentView.setText(mValues.get(position).content);
            holder.mContentView.setText(shortSteps.get(position));

            //holder.itemView.setTag(mValues.get(position));
            holder.itemView.setTag(String.valueOf(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            //return mValues.size();
            return shortSteps.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);

                mContentView = view.findViewById(R.id.content);
            }
        }
    }
}

/*
for (int m = 0; m < recipeIngredients.size(); m++) {
                    Log.i(TAG, "  Ingredient :  " + recipeIngredients.get(m).getIngredient() +
                            "    QUANTITY :  " + recipeIngredients.get(m).getQuantity() +
                            "    MEASURE : " + recipeIngredients.get(m).getMeasure());
                }
 */