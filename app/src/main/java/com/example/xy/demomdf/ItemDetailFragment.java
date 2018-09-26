package com.example.xy.demomdf;

import android.app.Activity;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xy.demomdf.data.RecipeData;
import com.example.xy.demomdf.dummy.DummyContent;
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
import java.util.ArrayList;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link RecipeDetailActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment
implements ExoPlayer.EventListener{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    private SimpleExoPlayerView exoPlayerView;
    private SimpleExoPlayer exoPlayer;

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

        TextView mainDescriptionTextView = rootView.findViewById(R.id.item_detail);

        exoPlayerView = rootView.findViewById(R.id.exo_player_view);

        // Show the dummy content as text in a TextView.
        /*
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
        }
         */


        if (recipeSteps != null || recipeSteps.size() != 0) {
            String videoUrl = recipeSteps.get(Integer.valueOf(tag)).getVideoUrl();
            if (videoUrl != null    || !videoUrl.equals(""))
                setupExoPlayer(videoUrl);
            String mainDescription = recipeSteps.get(Integer.valueOf(tag)).getMainDescription();
            mainDescriptionTextView.setText(mainDescription);
        }
        return rootView;
    }


    private void setupExoPlayer(String videoURL){
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
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer(){
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {


    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
