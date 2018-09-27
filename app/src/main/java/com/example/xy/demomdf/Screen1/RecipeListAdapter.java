package com.example.xy.demomdf.Screen1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xy.demomdf.R;
import com.example.xy.demomdf.Widget.BakeWidgetService;
import com.example.xy.demomdf.data.RecipeData;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeListAdapter extends
        RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {

    private Context context;
    private ArrayList<RecipeData> recipeDataArrayList;

    public final RecipeAdapterOnClickHandler mClickHandler;

    public RecipeListAdapter(Context context, ArrayList<RecipeData> recipeDataArrayList, RecipeAdapterOnClickHandler mClickHandler){
        this.context = context;
        this.recipeDataArrayList = recipeDataArrayList;
        this.mClickHandler = mClickHandler;
    }

    public interface RecipeAdapterOnClickHandler{
        void onClick(RecipeData recipeData,
                     ArrayList<RecipeData.RecipeIngredient> recipeIngredients,
                     ArrayList<RecipeData.RecipeStep> recipeSteps);
    }

    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.recipe_list_content;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(layoutId,parent,shouldAttachToParentImmediately);
        return new RecipeListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeListViewHolder holder, int position) {
        holder.recipeNameText.setText(recipeDataArrayList.get(position).getRecipeName());
    }

    @Override
    public int getItemCount() {
        if (recipeDataArrayList == null || recipeDataArrayList.size() == 0){
            return 0;
        } else {
            return recipeDataArrayList.size();
        }
    }

    class RecipeListViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{

        TextView recipeNameText;

        public RecipeListViewHolder(View itemView) {
            super(itemView);

            recipeNameText = itemView.findViewById(R.id.recipeNameText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            RecipeData recipeData = new RecipeData(Parcel.obtain());

            int recipeIndex = recipeDataArrayList.get(adapterPosition).getRecipeIndex();
            recipeData.setRecipeIndex(recipeIndex);

            String recipeName = recipeDataArrayList.get(adapterPosition).getRecipeName();
            recipeData.setRecipeName(recipeName);


            ArrayList<RecipeData.RecipeIngredient> recipeIngredients =
                    recipeDataArrayList.get(adapterPosition).getRecipeIngredientArrayList();


            ArrayList<RecipeData.RecipeStep> recipeSteps =
                    recipeDataArrayList.get(adapterPosition).getRecipeStepArrayList();

            SharedPreferences.Editor editor =
                    context.getSharedPreferences(context.getString(R.string.recipe_ingredients),
                            Context.MODE_PRIVATE
                    ).edit();

            StringBuilder builder = new StringBuilder();
            for (RecipeData.RecipeIngredient ingredient : recipeIngredients){
                String quantity = String.valueOf(ingredient.getQuantity());
                String measure = ingredient.getMeasure();
                String ingredientName = ingredient.getIngredient();

                String line = "\u2022" + ingredientName + " (" + quantity + " " + measure + " ) ";
                builder.append(line + "\n");
            }
            editor.putString(context.getString(R.string.ingredients_extra),builder.toString());
            editor.apply();

            if (Build.VERSION.SDK_INT > 25){
                BakeWidgetService.startActionRecipeO(context);
            } else{
                BakeWidgetService.startActionRecipe(context);
            }


            mClickHandler.onClick(recipeData,recipeIngredients,recipeSteps);


        }
    }
}
