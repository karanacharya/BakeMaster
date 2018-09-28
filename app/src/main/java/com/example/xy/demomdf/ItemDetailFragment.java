package com.example.xy.demomdf;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xy.demomdf.data.RecipeData;
import com.example.xy.demomdf.dummy.DummyContent;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;

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

    private SimpleExoPlayerView exoPlayerView;
    private SimpleExoPlayer exoPlayer;
    private CardView exoCardView;
    private TextView mainDescriptionTextView;

    public static final String ARG_ITEM_ID = "item_id";

    public static final String ARG_STEPS_LIST_ID = "steps_list_id";

    private String videoUrl;
    private long currentPosition;
    private ImageView thumbnailImageView;

    /**
     * The dummy content this fragment is presenting.
     */
//    private DummyContent.DummyItem mItem;

    private ArrayList<RecipeData.RecipeStep> recipeSteps;
    private String tag;
    private String appBarLayoutTitle;

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

        if (getArguments().containsKey(ARG_STEPS_LIST_ID)) {
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
            appBarLayoutTitle = recipeSteps.get(Integer.valueOf(tag)).getShortDescription();
            if (appBarLayout != null) {
                appBarLayout.setTitle(appBarLayoutTitle);
            }

        }



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        currentPosition = C.TIME_UNSET;
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getLong("CURRENT_POSITION",C.TIME_UNSET);
        }

        mainDescriptionTextView = rootView.findViewById(R.id.item_detail);
        thumbnailImageView = rootView.findViewById(R.id.thumbnail_iv);

        exoPlayerView = rootView.findViewById(R.id.exo_player_view);
        exoCardView = rootView.findViewById(R.id.exo_card_view);

        // Show the dummy content as text in a TextView.
        /*
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
        }
         */


        if (recipeSteps != null || recipeSteps.size() != 0) {
            videoUrl = recipeSteps.get(Integer.valueOf(tag)).getVideoUrl();
            if (videoUrl != null || !videoUrl.equals("")) {
                setupExoPlayer(videoUrl);
            }
            String mainDescription = recipeSteps.get(Integer.valueOf(tag)).getMainDescription();
            mainDescriptionTextView.setText(mainDescription);

            String thumbnailUrl = recipeSteps.get(Integer.valueOf(tag)).getThumbnailUrl();
            if (thumbnailUrl.isEmpty() || thumbnailUrl.equals("") || thumbnailUrl == null){
                Log.i("ItemDetailFragment","Thumbnail Url is null");
            } else {
                thumbnailImageView.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(thumbnailUrl)
                        .into(thumbnailImageView);
            }

        }

        return rootView;


    }


    private void setupExoPlayer(String videoURL) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        if (exoPlayer == null) {
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            Uri videoURI = Uri.parse(videoURL);

            DefaultHttpDataSourceFactory dataSourceFactory =
                    new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(
                    videoURI,
                    dataSourceFactory,
                    extractorsFactory,
                    null,
                    null
            );

            exoPlayerView.setPlayer(exoPlayer);
            if (currentPosition != C.TIME_UNSET){
                exoPlayer.seekTo(currentPosition);
            }
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("CURRENT_POSITION",currentPosition);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentPosition = C.TIME_UNSET;
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getLong("CURRENT_POSITION",C.TIME_UNSET);
        }

    }


    private void releasePlayer() {
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (videoUrl != null){
            setupExoPlayer(videoUrl);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (exoPlayer != null){
            currentPosition = exoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }
}

