package it.univaq.disim.mwt.android_native_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import it.univaq.disim.mwt.android_native_app.api.TMDB;
import it.univaq.disim.mwt.android_native_app.model.Episode;
import it.univaq.disim.mwt.android_native_app.services.DataParserService;
import it.univaq.disim.mwt.android_native_app.services.UserCollectionService;
import it.univaq.disim.mwt.android_native_app.utils.Network;

public class EpisodeFragment extends Fragment {
    private static final String ARG_EPISODE = "arg_episode";

    private Episode episode;
    private Episode episodeDetailed;
    private TextView episodeName;
    private TextView episodeOverview;
    private TextView episodeAirDate;
    private ProgressBar progressBar;
    private MaterialButton markEpisodeButton;
    private boolean isTvShowInCollection;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
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
                            episodeOverview.setText(episodeDetailed.getOverview());
                            episodeAirDate.setText(episodeDetailed.getAir_date());

                            if(episodeDetailed.isWatched()){
                                markEpisodeButton.setText(getString(R.string.episode_button_mark_as_unseen));
                            } else {
                                markEpisodeButton.setText(getString(R.string.episode_button_mark_as_seen));
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
                                        } else {

                                            Intent intent = new Intent(getContext(), UserCollectionService.class);
                                            intent.putExtra(UserCollectionService.KEY_ACTION, UserCollectionService.ACTION_SAVE_EPISODE_TO_COLLECTION);
                                            intent.putExtra(UserCollectionService.KEY_DATA, episodeDetailed);
                                            Objects.requireNonNull(getContext()).startService(intent);

                                            episodeDetailed.setWatched(true);
                                            markEpisodeButton.setText(getString(R.string.episode_button_mark_as_unseen));
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
            episodeOverview.setText(episodeDetailed.getOverview());
            episodeAirDate.setText(episodeDetailed.getAir_date());

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

        episodeOverview = view.findViewById(R.id.episode_overview);

        episodeAirDate = view.findViewById(R.id.episode_air_date);

        progressBar = view.findViewById(R.id.episode_progress);

        markEpisodeButton = view.findViewById(R.id.mark_episode_button);

        // Inflate the layout for this fragment
        return view;
    }
}
