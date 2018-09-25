package com.example.xy.demomdf;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xy.demomdf.data.RecipeData;
import com.example.xy.demomdf.dummy.DummyContent;

import java.util.ArrayList;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link RecipeDetailActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    public static final String ARG_STEPS_LIST_ID = "steps_list_id";

    /**
     * The dummy content this fragment is presenting.
     */
//    private DummyContent.DummyItem mItem;

    private ArrayList<RecipeData.RecipeStep> recipeSteps;
    private String tag;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeSteps = new ArrayList<>();

        if (getArguments().containsKey(ARG_STEPS_LIST_ID)){
            recipeSteps = getArguments().getParcelableArrayList(ARG_STEPS_LIST_ID);
        }




        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout appBarLayout =
                    activity.findViewById(R.id.toolbar_layout);

            tag = getArguments().getString(ARG_ITEM_ID);
            if (appBarLayout != null) {
                //appBarLayout.setTitle(mItem.content);
                appBarLayout.setTitle(recipeSteps.get(Integer.valueOf(tag)).getShortDescription());

            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        TextView videoUrlTextView = rootView.findViewById(R.id.video_url_tv);
        TextView mainDescriptionTextView = rootView.findViewById(R.id.item_detail);

        // Show the dummy content as text in a TextView.
        /*
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
        }
         */

        if (recipeSteps != null  || recipeSteps.size() != 0){
            String videoUrl = recipeSteps.get(Integer.valueOf(tag)).getVideoUrl();
            videoUrlTextView.setText(videoUrl);
            String mainDescription = recipeSteps.get(Integer.valueOf(tag)).getMainDescription();
            mainDescriptionTextView.setText(mainDescription);
        }
        return rootView;
    }
}
