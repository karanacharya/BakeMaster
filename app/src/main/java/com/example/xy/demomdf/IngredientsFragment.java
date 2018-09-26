package com.example.xy.demomdf;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xy.demomdf.data.RecipeData;

import java.util.ArrayList;

public class IngredientsFragment extends Fragment {

    public static final String ARG_INGREDIENTS_ID = "ingredients_list_id";
    private ArrayList<RecipeData.RecipeIngredient> recipeIngredients;


    public IngredientsFragment(){
//        Mandatory empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeIngredients = new ArrayList<>();


        Activity activity = this.getActivity();
        net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout appBarLayout =
                activity.findViewById(R.id.ingredients_toolbar_layout);

        if (appBarLayout != null) {
            //appBarLayout.setTitle(mItem.content);
            appBarLayout.setTitle("Ingredients");

        }
        if (getArguments().containsKey(ARG_INGREDIENTS_ID)){
            recipeIngredients = getArguments().getParcelableArrayList(ARG_INGREDIENTS_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.ingredients_list,container,false);

        TextView ingList = rootView.findViewById(R.id.list_ing);
        if (recipeIngredients != null   || recipeIngredients.size() !=0){
            ingList.setText(recipeIngredients.get(0).getIngredient());
        }

        return rootView;
    }
}
