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

import it.univaq.disim.mwt.android_native_app.api.TMDB;
import it.univaq.disim.mwt.android_native_app.model.Episode;
import it.univaq.disim.mwt.android_native_app.services.DataParserService;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EpisodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EpisodeFragment extends Fragment {
    private static final String ARG_EPISODE = "arg_episode";

    private Episode episode;
    private Episode episodeDetailed;
    private TextView episodeName;
    private TextView episodeOverview;
    private TextView episodeAirDate;
    private ProgressBar progressBar;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                switch (action){
                    case DataParserService.FILTER_PARSE_TV_SHOW_EPISODE:
                        episodeDetailed = (Episode) intent.getSerializableExtra(DataParserService.EXTRA);

                        if(episode.equals(episodeDetailed)){
                            progressBar.setVisibility(View.INVISIBLE);

                            episodeName.setText(episodeDetailed.getName());
                            episodeOverview.setText(episodeDetailed.getOverview());
                            episodeAirDate.setText(episodeDetailed.getAir_date());
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
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, intentFilter);

        if(episode.getTv_show_id() != 0 && episodeDetailed == null){
            TMDB.requestRemoteTvShowEpisode(getContext(), episode.getTv_show_id(), episode.getSeason_number(), episode.getEpisode_number());

            progressBar.setVisibility(View.VISIBLE);
        }

        if(episodeDetailed != null){
            episodeName.setText(episodeDetailed.getName());
            episodeOverview.setText(episodeDetailed.getOverview());
            episodeAirDate.setText(episodeDetailed.getAir_date());
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

        // Inflate the layout for this fragment
        return view;
    }
}
