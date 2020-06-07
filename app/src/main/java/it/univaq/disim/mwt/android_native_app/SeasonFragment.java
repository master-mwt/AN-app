package it.univaq.disim.mwt.android_native_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.mwt.android_native_app.adapters.RecyclerViewEpisodeAdapter;
import it.univaq.disim.mwt.android_native_app.api.TMDB;
import it.univaq.disim.mwt.android_native_app.model.Episode;
import it.univaq.disim.mwt.android_native_app.model.Season;
import it.univaq.disim.mwt.android_native_app.services.DataParserService;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeasonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeasonFragment extends Fragment {
    private static final String ARG_SEASON = "arg_season";

    private Season season;
    private Season seasonDetailed;
    private List<Episode> data = new ArrayList<>();
    private TextView seasonName;
    private TextView seasonOverview;
    private RecyclerView recyclerView;
    private RecyclerViewEpisodeAdapter recyclerViewEpisodeAdapter;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                switch (action){
                    case DataParserService.FILTER_PARSE_TV_SHOW_SEASON:
                        //progressBar.setVisibility(View.INVISIBLE);
                        seasonDetailed = (Season) intent.getSerializableExtra(DataParserService.EXTRA);
                        if(seasonDetailed.equals(season)){
                            data.clear();
                            data.addAll(seasonDetailed.getEpisodes());
                            recyclerViewEpisodeAdapter.notifyDataSetChanged();
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
        recyclerViewEpisodeAdapter = new RecyclerViewEpisodeAdapter(getContext(), data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewEpisodeAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(DataParserService.FILTER_PARSE_TV_SHOW_SEASON);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, intentFilter);

        if(season.getTv_show_id() != 0 && seasonDetailed == null){
            seasonName.setText(season.getName());
            seasonOverview.setText(season.getOverview());
            TMDB.requestRemoteTvShowSeason(getContext(), season.getTv_show_id(), season.getSeason_number());

            //progressBar.setVisibility(View.VISIBLE);
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

        // Inflate the layout for this fragment
        return view;
    }
}
