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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.univaq.disim.mwt.android_native_app.adapters.RecyclerViewTvShowCharacterCardAdapter;
import it.univaq.disim.mwt.android_native_app.api.TMDB;
import it.univaq.disim.mwt.android_native_app.model.TvShowCharacter;
import it.univaq.disim.mwt.android_native_app.model.TvShowDetails;
import it.univaq.disim.mwt.android_native_app.services.DataParserService;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TvShowDetailsInfoCastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TvShowDetailsInfoCastFragment extends Fragment {
    private static final String ARG_TV_SHOW_DETAILS = "tv_show_details";

    private TextView name;
    private TextView overview;
    private TextView originalLanguage;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TvShowDetails tvShowDetails;
    private ArrayList<TvShowCharacter> data = new ArrayList<>();
    private RecyclerViewTvShowCharacterCardAdapter recyclerViewTvShowCharacterCardAdapter;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                switch (action){
                    case DataParserService.FILTER_PARSE_TV_SHOW_CREDITS:
                        progressBar.setVisibility(View.INVISIBLE);
                        data.addAll(intent.<TvShowCharacter>getParcelableArrayListExtra(DataParserService.EXTRA));
                        recyclerViewTvShowCharacterCardAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public TvShowDetailsInfoCastFragment() {
        // Required empty public constructor
    }

    public static TvShowDetailsInfoCastFragment newInstance(TvShowDetails tvShowDetails) {
        TvShowDetailsInfoCastFragment fragment = new TvShowDetailsInfoCastFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TV_SHOW_DETAILS, tvShowDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tvShowDetails = (TvShowDetails) getArguments().getSerializable(ARG_TV_SHOW_DETAILS);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        name.setText(tvShowDetails.getName());
        overview.setText(tvShowDetails.getOverview());
        originalLanguage.setText(tvShowDetails.getOriginal_language());

        data.clear();

        recyclerViewTvShowCharacterCardAdapter = new RecyclerViewTvShowCharacterCardAdapter(getContext(), data);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(recyclerViewTvShowCharacterCardAdapter);

        IntentFilter intentFilter = new IntentFilter(DataParserService.FILTER_PARSE_TV_SHOW_CREDITS);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, intentFilter);

        TMDB.requestRemoteTvShowCredits(getContext(), tvShowDetails.getTv_show_id());

        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_show_details_info_cast, container, false);

        name = view.findViewById(R.id.name);

        overview = view.findViewById(R.id.overview);

        originalLanguage = view.findViewById(R.id.original_language);

        recyclerView = view.findViewById(R.id.cast_recycle_view);

        progressBar = view.findViewById(R.id.cast_recycler_view_progress);

        // Inflate the layout for this fragment
        return view;
    }
}
