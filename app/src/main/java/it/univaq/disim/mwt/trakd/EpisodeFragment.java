package it.univaq.disim.mwt.trakd;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import it.univaq.disim.mwt.trakd.api.TMDB;
import it.univaq.disim.mwt.trakd.model.Episode;
import it.univaq.disim.mwt.trakd.services.DataParserService;
import it.univaq.disim.mwt.trakd.services.UserCollectionService;
import it.univaq.disim.mwt.trakd.utils.Network;
import it.univaq.disim.mwt.trakd.utils.VolleyRequest;

public class EpisodeFragment extends Fragment {
    private static final String ARG_EPISODE = "arg_episode";

    private Episode episode;
    private Episode episodeDetailed;
    private TextView episodeName;
    private TextView episodeVoteRating;
    private RatingBar episodeRatingBar;
    private TextView episodeOverview;
    private TextView episodeAirDate;
    private ImageView episodeStillPath;
    private ProgressBar progressBar;
    private MaterialButton markEpisodeButton;
    private boolean isTvShowInCollection;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @SuppressLint("StringFormatMatches")
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                switch (action){
                    case DataParserService.FILTER_PARSE_TV_SHOW_EPISODE:
                        if(episodeDetailed == null && episode.equals(intent.getSerializableExtra(DataParserService.EXTRA))){

                            episodeDetailed = (Episode) intent.getSerializableExtra(DataParserService.EXTRA);

                            episodeDetailed.setTv_show_id(episode.getTv_show_id());
                            episodeDetailed.setSeason_id(episode.getSeason_id());
                            progressBar.setVisibility(View.INVISIBLE);

                            episodeName.setText(episodeDetailed.getName());
                            episodeVoteRating.setText(String.format(getString(R.string.vote_structure), episodeDetailed.getVote_average(), episodeDetailed.getVote_count()));
                            if(episodeDetailed.getVote_average() == 0){
                                episodeRatingBar.setRating(0);
                            } else {
                                episodeRatingBar.setRating((float) (episodeDetailed.getVote_average() * 5) / 10);
                            }
                            episodeOverview.setText(episodeDetailed.getOverview());
                            episodeAirDate.setText(getString(R.string.airdate) + episodeDetailed.getAir_date());
                            setTvShowImage(episodeDetailed.getStill_path());

                            if(episodeDetailed.isWatched()){
                                markEpisodeButton.setText(getString(R.string.episode_button_mark_as_unseen));
                                markEpisodeButton.setBackgroundColor(getResources().getColor(R.color.colorMarked, getContext().getTheme()));
                            } else {
                                markEpisodeButton.setText(getString(R.string.episode_button_mark_as_seen));
                                //markEpisodeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary, getContext().getTheme()));
                            }

                        }
                        break;

                    case UserCollectionService.FILTER_IS_TV_SHOW_IN_COLLECTION:
                        if(episodeDetailed == null){

                            /* Check network connection */
                            Network.checkAvailability(getContext(), getFragmentManager());

                            TMDB.requestRemoteTvShowEpisode(getContext(), episode.getTv_show_id(), episode.getSeason_number(), episode.getEpisode_number());

                            isTvShowInCollection = intent.getBooleanExtra(UserCollectionService.EXTRA, false);

                            if(isTvShowInCollection){
                                markEpisodeButton.setEnabled(true);
                                markEpisodeButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(episodeDetailed.isWatched()){

                                            Intent intent = new Intent(getContext(), UserCollectionService.class);
                                            intent.putExtra(UserCollectionService.KEY_ACTION, UserCollectionService.ACTION_DELETE_EPISODE_FROM_COLLECTION);
                                            intent.putExtra(UserCollectionService.KEY_DATA, episodeDetailed);
                                            Objects.requireNonNull(getContext()).startService(intent);

                                            episodeDetailed.setWatched(false);
                                            markEpisodeButton.setText(getString(R.string.episode_button_mark_as_seen));
                                            markEpisodeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary, getContext().getTheme()));
                                        } else {

                                            Intent intent = new Intent(getContext(), UserCollectionService.class);
                                            intent.putExtra(UserCollectionService.KEY_ACTION, UserCollectionService.ACTION_SAVE_EPISODE_TO_COLLECTION);
                                            intent.putExtra(UserCollectionService.KEY_DATA, episodeDetailed);
                                            Objects.requireNonNull(getContext()).startService(intent);

                                            episodeDetailed.setWatched(true);
                                            markEpisodeButton.setText(getString(R.string.episode_button_mark_as_unseen));
                                            markEpisodeButton.setBackgroundColor(getResources().getColor(R.color.colorMarked, getContext().getTheme()));
                                        }
                                    }
                                });
                            }
                        }

                        break;
                    default:
                        break;
                }
            }
        }
    };

    public EpisodeFragment() {
        // Required empty public constructor
    }

    public static EpisodeFragment newInstance(Episode episode) {
        EpisodeFragment fragment = new EpisodeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EPISODE, episode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            episode = (Episode) getArguments().getSerializable(ARG_EPISODE);
        }
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(DataParserService.FILTER_PARSE_TV_SHOW_EPISODE);
        intentFilter.addAction(UserCollectionService.FILTER_IS_TV_SHOW_IN_COLLECTION);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, intentFilter);

        if(episode.getTv_show_id() != 0 && episodeDetailed == null){

            Intent intent = new Intent(getContext(), UserCollectionService.class);
            intent.putExtra(UserCollectionService.KEY_ACTION, UserCollectionService.ACTION_IS_TV_SHOW_IN_COLLECTION);
            intent.putExtra(UserCollectionService.KEY_DATA, episode.getTv_show_id());
            Objects.requireNonNull(getContext()).startService(intent);

            progressBar.setVisibility(View.VISIBLE);
        }

        if(episodeDetailed != null){
            episodeName.setText(episodeDetailed.getName());
            episodeVoteRating.setText(String.format(getString(R.string.vote_structure), episodeDetailed.getVote_average(), episodeDetailed.getVote_count()));
            if(episodeDetailed.getVote_average() == 0){
                episodeRatingBar.setRating(0);
            } else {
                episodeRatingBar.setRating((float) (episodeDetailed.getVote_average() * 5) / 10);
            }
            episodeOverview.setText(episodeDetailed.getOverview());
            episodeAirDate.setText(getString(R.string.airdate) + episodeDetailed.getAir_date());
            setTvShowImage(episodeDetailed.getStill_path());

            if(episodeDetailed.isWatched()){
                markEpisodeButton.setText(getString(R.string.episode_button_mark_as_unseen));
            } else {
                markEpisodeButton.setText(getString(R.string.episode_button_mark_as_seen));
            }

            if(isTvShowInCollection){
                markEpisodeButton.setEnabled(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_episode, container, false);

        episodeName = view.findViewById(R.id.episode_name);

        episodeVoteRating = view.findViewById(R.id.episode_vote_rating);

        episodeRatingBar = view.findViewById(R.id.episode_rating_bar);

        episodeOverview = view.findViewById(R.id.episode_overview);

        episodeAirDate = view.findViewById(R.id.episode_air_date);

        episodeStillPath = view.findViewById(R.id.episode_still_path);

        progressBar = view.findViewById(R.id.episode_progress);

        markEpisodeButton = view.findViewById(R.id.mark_episode_button);

        // Inflate the layout for this fragment
        return view;
    }

    private void setTvShowImage(String imageUrl){

        String baseUrl = getString(R.string.tmdb_image_baselink);

        if(imageUrl != null && !"".equals(imageUrl) && !"null".equals(imageUrl)){
            String requestUrl = baseUrl + imageUrl;

            VolleyRequest.getInstance(getContext()).getImageLoader().get(requestUrl, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    episodeStillPath.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.w(TvShowDetailsActivity.class.getName(), (error.getCause() != null) ? error.getCause().getMessage() : error.getMessage());
                }
            });
        }
    }
}
