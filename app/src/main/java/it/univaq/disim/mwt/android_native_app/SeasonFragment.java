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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.univaq.disim.mwt.android_native_app.adapters.RecyclerViewEpisodeAdapter;
import it.univaq.disim.mwt.android_native_app.api.TMDB;
import it.univaq.disim.mwt.android_native_app.model.Episode;
import it.univaq.disim.mwt.android_native_app.model.Season;
import it.univaq.disim.mwt.android_native_app.services.DataParserService;
import it.univaq.disim.mwt.android_native_app.services.UserCollectionService;

// TODO: resolve bug see/unsee episode in two different season fragments
public class SeasonFragment extends Fragment {
    private static final String ARG_SEASON = "arg_season";

    private Season season;
    private Season seasonDetailed;
    private List<Episode> data = new ArrayList<>();
    private TextView seasonName;
    private TextView seasonOverview;
    private RecyclerView recyclerView;
    private RecyclerViewEpisodeAdapter recyclerViewEpisodeAdapter;
    private ProgressBar progressBar;
    private boolean isTvShowInCollection;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                switch (action){
                    case DataParserService.FILTER_PARSE_TV_SHOW_SEASON:
                        if(seasonDetailed == null && season.equals(intent.getSerializableExtra(DataParserService.EXTRA))){
                            seasonDetailed = (Season) intent.getSerializableExtra(DataParserService.EXTRA);

                            progressBar.setVisibility(View.INVISIBLE);

                            data.clear();

                            seasonName.setText(seasonDetailed.getName());
                            seasonOverview.setText(seasonDetailed.getOverview());

                            for(Episode episode : seasonDetailed.getEpisodes()){
                                episode.setTv_show_id(season.getTv_show_id());
                            }

                            data.addAll(seasonDetailed.getEpisodes());
                            recyclerViewEpisodeAdapter.notifyDataSetChanged();
                        }
                        break;
                    case UserCollectionService.FILTER_IS_TV_SHOW_IN_COLLECTION:
                        if(seasonDetailed == null){
                            isTvShowInCollection = intent.getBooleanExtra(UserCollectionService.EXTRA, false);

                            recyclerViewEpisodeAdapter = new RecyclerViewEpisodeAdapter(getContext(), data, isTvShowInCollection);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(recyclerViewEpisodeAdapter);

                            TMDB.requestRemoteTvShowSeason(getContext(), season.getTv_show_id(), season.getSeason_number());
                        }
                        break;
                    case UserCollectionService.FILTER_GET_EPISODES_BY_SEASON:
                        if(isTvShowInCollection && (season.equals(seasonDetailed))){
                            ArrayList<Episode> episodes = (ArrayList<Episode>) intent.getSerializableExtra(UserCollectionService.EXTRA);
                            boolean dirty = false;
                            if(episodes != null){
                                for(Episode e : episodes){
                                    if(data.contains(e)){
                                        data.get(data.indexOf(e)).setWatched(true);
                                        dirty = true;
                                    }
                                }
                                if(dirty){
                                    recyclerViewEpisodeAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public SeasonFragment() {
        // Required empty public constructor
    }

    public static SeasonFragment newInstance(Season season) {
        SeasonFragment fragment = new SeasonFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SEASON, season);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            season = (Season) getArguments().getSerializable(ARG_SEASON);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(recyclerViewEpisodeAdapter != null){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(recyclerViewEpisodeAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(DataParserService.FILTER_PARSE_TV_SHOW_SEASON);
        intentFilter.addAction(UserCollectionService.FILTER_IS_TV_SHOW_IN_COLLECTION);
        intentFilter.addAction(UserCollectionService.FILTER_GET_EPISODES_BY_SEASON);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, intentFilter);

        if(season.getTv_show_id() != 0 && seasonDetailed == null){

            Intent intent = new Intent(getContext(), UserCollectionService.class);
            intent.putExtra(UserCollectionService.KEY_ACTION, UserCollectionService.ACTION_IS_TV_SHOW_IN_COLLECTION);
            intent.putExtra(UserCollectionService.KEY_DATA, season.getTv_show_id());
            Objects.requireNonNull(getContext()).startService(intent);

            progressBar.setVisibility(View.VISIBLE);
        }

        if(seasonDetailed != null && isTvShowInCollection){
            seasonName.setText(seasonDetailed.getName());
            seasonOverview.setText(seasonDetailed.getOverview());

            Intent intent = new Intent(getContext(), UserCollectionService.class);
            intent.putExtra(UserCollectionService.KEY_ACTION, UserCollectionService.ACTION_GET_EPISODES_BY_SEASON);
            intent.putExtra(UserCollectionService.KEY_DATA, seasonDetailed);
            Objects.requireNonNull(getContext()).startService(intent);
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

        View view = inflater.inflate(R.layout.fragment_season, container, false);

        seasonName = view.findViewById(R.id.season_name);

        seasonOverview = view.findViewById(R.id.season_overview);

        recyclerView = view.findViewById(R.id.episodes_recycle_view);

        progressBar = view.findViewById(R.id.season_progress);

        // Inflate the layout for this fragment
        return view;
    }
}
